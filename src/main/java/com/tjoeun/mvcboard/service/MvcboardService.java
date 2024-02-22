package com.tjoeun.mvcboard.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;

import com.tjoeun.mvcboard.dao.MvcboardDAO;
import com.tjoeun.mvcboard.mybatis.MySession;
import com.tjoeun.mvcboard.vo.MvcboardList;
import com.tjoeun.mvcboard.vo.MvcboardVO;

public class MvcboardService {
	
	private static MvcboardService instance = new MvcboardService();
	private MvcboardService() {
	}
	public static MvcboardService getInstance () {
		return instance;
	}
	
//	Service 클래스에서 사용할 DAO 클래스 객체를 얻어온다.
	private MvcboardDAO dao = MvcboardDAO.getInstance();
	
	
//	컨트롤러에 insertOK.nhn이라는 요청이 들어오면 컨트롤러에서 호출하는 메소드로 테이블에 저장할
//	메인글 정보가 저장된 HttpServletRequest 인터페이스 객체를 넘겨받고, mapper를 얻어온 후 
//	MvcboardDAO 클래스의 메인글을 저장하는 insert sql 명령을 실행하는 메소드를 호출하는 메소드
	public void insert(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스 insert() 실행");
		SqlSession mapper = MySession.getSession();
		
//		insert.jsp에서 입력한 HttpServletRequest 인터페이스 객체에 저장되어 넘어오는 데이터를
//		받아서 MvcboardVO 클래스 객체에 저장한다.
		MvcboardVO vo = new MvcboardVO();
		vo.setName(request.getParameter("name"));
		vo.setSubject(request.getParameter("subject"));
		vo.setContent(request.getParameter("content"));
//		System.out.println(vo);
		
//		MvcboardDAO 클래스의 insert.jsp에서 입력한 데이터를 테이블에 저장하는 insert sql 명령을
//		실행하는 메소드를 호출한다.
		dao.insert(mapper, vo);
		
		mapper.commit();
		mapper.close();
	}
	
	
//	컨트롤러에 list.nhn이라는 요청이 들어오면 컨트롤러에서 호출하는 메소드로 mapper를 얻어온 후
//	MvcboardDAO 클래스의 브라우저에 출력할 1페이지 분량의 글 목록과 페이징 작업에 사용할 8개의
//	변수가 저장된 클래스 객체를 만들어 request 영역에 저장하는 메소드
	public void selectList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스 selectList() 실행");
		SqlSession mapper = MySession.getSession();
		
//		list.nhn이 요청될 때 넘어오는 브라우저에 표시할 페이지 번호를 받는다.
//		브라우저에 표시할 페이지 번호가 정상적으로 넘어왔다면 넘어온 페이지 번호로, 정상적으로 넘어오지 않았다면
//		1로 브라우저에 표시할 페이지 번호를 설정한다.
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		} catch (NumberFormatException e) {
			
		}
		
//		1페이지에 표시할 글의 갯수를 정한다.
		int pageSize = 10;
//		테이블에 저장된 전체 글의 갯수를 얻어온다.
		int totalCount = 0;
		totalCount = dao.selectCount(mapper);
		System.out.println(totalCount);
//		1페이지 분량의 글 목록과 페이징 작업에 사용되는 8개의 변수를 초기화시키는 MvcboardList 클래스 객체를 만든다.
		MvcboardList mvcboardList = new MvcboardList(pageSize, totalCount, currentPage);
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		hmap.put("startNo", mvcboardList.getStartNo());
		hmap.put("endNo", mvcboardList.getEndNo());
//		System.out.println(mvcboardList);
//		1페이지 분량의 글 목록을 얻어와서 MvcboardList 클래스의 ArrayList에 넣어준다.
		mvcboardList.setList( dao.selectList(mapper, hmap) );
//		ArrayList 클래스 객체를 request 영역에 저장한다.
		request.setAttribute("mvcboardList", mvcboardList);
		
		
		mapper.commit();
		mapper.close();
	}
	
	
//	컨트롤러에 increment.nhn이라는 요청이 들어오면 컨트롤러에서 호출하는 메소드로 조회수를 증가시킨
//	글번호와 돌아갈 페이지 번호를 넘겨받고 mapper를 얻어온 후 MvcboardDAO 클래스의 조회수를 증가시키는
//	update sql 명령을 실행하는 메소드를 호출하는 메소드
	public void increment(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스 increment() 실행");
		SqlSession mapper = MySession.getSession();
		
//		request 객체로 넘어온 조회수를 증가시킬 글번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
//		조회수를 증가시키는 메소드를 호출한다.
		dao.increment(mapper, idx);
		
		mapper.commit();
		mapper.close();
	}
	
	
//	컨트롤러에 contentView.nhn라는 요청이 들어오면 컨트롤러에서 호출되는 메소드로 조회수를 증가시킨
//	글번호와 돌아갈 페이지 번호를 넘겨받고 mapper를 얻어온 후 MvcboardDAO 클래스의 조회수를 증가시킨
//	글 1건을 얻어오는 select sql 명령을 실행하고 조회수를 증가시킨 글 1건을 request 영역에 저장하는 메소드
	public void selectByIdx(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스 selectByIdx() 실행");
		SqlSession mapper = MySession.getSession();
		
//		request 객체로 넘어오는 조회수를 증가시킨 글번호와 돌아갈 페이지 번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
		int currentPage = Integer.parseInt(request.getParameter("currentPage"));
//		조회수를 증가시킨 글 1건을 얻어와서 MvcboardVO 클래스 객체에 저장한다.
		MvcboardVO vo = dao.selectByIdx(mapper, idx);
//		System.out.println(vo);
//		브라우저에 표시할 글이 저장된 객체, 돌아갈 페이지, 줄바꿈에 사용할 이스케이프 시퀀스를
//		request 영역에 저장한다.
		
		request.setAttribute("vo", vo);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("enter", "\r\n");
		
		mapper.commit();
		mapper.close();
	}
	
	
//	컨트롤러에 update.nhn이라는 요청이 들어오면 컨트롤러에서 호출되는 메소드로 수정할 정보가 저장된
//	request 객체를 넘겨받고 mapper를 얻어온 후 MvcboardDAO 클래스의 글 1건을 수정하는 update sql
//	명령을 실행하는 메소드
	public void update(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스 update() 실행");
		SqlSession mapper = MySession.getSession();
		
//		request 객체로 넘어온 수정할 데이터를 받아서 MvcboardVO 클래스 객체에 저장한다.
		MvcboardVO vo = new MvcboardVO();
		vo.setIdx(Integer.parseInt( request.getParameter("idx") ));
		vo.setSubject(request.getParameter("subject"));
		vo.setContent(request.getParameter("content"));
//		System.out.println(vo);
		
//		글 1건을 수정하는 메소드를 호출한다.
		dao.update(mapper, vo);
		
		mapper.commit();
		mapper.close();
	}
	
	
//	컨트롤러에 delete.nhn이라는 요청이 들어오면 컨트롤러에서 호출되는 메소드로 삭제할 정보가 저장된
//	request 객체를 넘겨받고 mapper를 얻어온 후 MvcboardDAO 클래스의 글 1건을 삭제하는 delete sql
//	명령을 실행하는 메소드
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스 delete() 실행");
		SqlSession mapper = MySession.getSession();
		
//		request 객체로 넘어온 삭제할 글 번호를 받는다.
		int idx = Integer.parseInt( request.getParameter("idx") );
//		글 1건을 삭제하는 메소드를 호출한다.
		dao.delete(mapper, idx);
		
		mapper.commit();
		mapper.close();
	}
	
	
//	컨트롤러에 replyInsert.nhn 이라는 요청이 들어오면 컨트롤러에서 호출되는 메소드로 답글 정보가
//	저장된 request 객체를 넘겨받고 mapper를 얻어온 후 MvcboardDAO 클래스의 조건에 만족하는 seq를
//	1씩 증가시키는 update sql 명령을 실행하고 답글을 저장하는 insert sql 명령을 실행하는 메소드를 호출하는 메소드
	
	public void replyInsert(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("MvcboardService 클래스 replyInsert() 실행");
		SqlSession mapper = MySession.getSession();
		
//		request 객체로 넘어오는 답글 정보를 받아서 MvcboardVO 클래스 객체에 저장한다.
		int idx = Integer.parseInt(request.getParameter("idx"));
		int currentPage = Integer.parseInt(request.getParameter("currentPage"));
		int gup = Integer.parseInt(request.getParameter("gup"));
		int lev = Integer.parseInt(request.getParameter("lev"));
		int seq = Integer.parseInt(request.getParameter("seq"));
		String subject = request.getParameter("subject");
		String name = request.getParameter("name");
		String content = request.getParameter("content");
		
//		MvcboardVO 클래스 객체의 lev와 seq를 1씩 증가시킨다.
		MvcboardVO vo = new MvcboardVO();
		vo.setIdx(idx);
		vo.setSubject(subject);
		vo.setContent(content);
		vo.setLev(lev + 1);
		vo.setGup(gup);
		vo.setSeq(seq + 1);
		vo.setName(name);
		System.out.println(vo);
		
		
//		같은 글 그룹(gup)에서 답글이 표시될 위치(seq) 이후의 모든 글이 출력될 위치를 1씩 증가시키는
//		메소드를 호출한다.
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		hmap.put("gup", vo.getGup());
		hmap.put("seq", vo.getSeq());
		dao.incrementSeq(mapper, hmap);
		
//		답글을 저장하는 메소드를 호출한다.
		dao.replyInsert(mapper, vo);
		
		mapper.commit();
		mapper.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
