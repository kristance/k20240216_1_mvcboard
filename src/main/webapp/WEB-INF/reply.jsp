<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<title>답변 하기</title>
</head>
<body>


<!-- 질문글을 보여주는 테이블 -->
<div class="m-3">
	<table 
	class="table table-hover"
	style="width: 600px; 
	margin-left: auto; 
	margin-right: auto;
	">
<!--  -->	
		<tr class="table-danger">
			<th class="align-middle text-center" colspan="4" style="font-size: 30px;">
				질문글 보기
			</th>
		</tr>
<!--  -->		
		<tr class="table">
			<td class="align-middle text-center" style="width: 100px;">글번호</td>
			<td class="align-middle text-center" style="width: 240px; text-align: center;">이름</td>
			<td class="align-middle text-center" style="width: 160px; text-align: center;">작성일</td>
			<td class="align-middle text-center"  style="width: 100px; text-align: center;">조회수</td>
		</tr>
<!--  -->	
		<tr>
			<td class="align-middle text-center">
					
				${vo.idx}
						
			</td>
			<td class="align-middle text-center">
					
				<c:set var="name" value="${fn:replace(vo.name, '<', '&lt;')}"></c:set>
				<c:set var="name" value="${fn:replace(name, '>', '&gt;')}"></c:set>
				${name}
						
			</td>
			<td class="align-middle text-center">
			
				<jsp:useBean id="date" class="java.util.Date"/>
				<fmt:formatDate var="today" value="${date}" pattern="yyyy.MM.dd"/> 
				<fmt:formatDate var="writeDate" value="${vo.writeDate}" pattern="yyyy.MM.dd"/>
				
				<c:if test="${today == writeDate }">
					<fmt:formatDate value="${date}" pattern="HH:MM:ss"/>
				</c:if>
				<c:if test="${today != writeDate }"> 
					<fmt:formatDate value="${date}" pattern="yyyy.MM.dd(E)"/>
				</c:if>
						
			</td>
			<td class="align-middle text-center">
					
				${vo.hit}
						
			</td>
		</tr>
<!--  -->
		<tr>
		
			<th class="align-middle">
				<label for="subject">제목</label>
			</th>
			<td colspan="3">
				<input 
					id="subject" 
					class="form-control form-control-sm" 
					type="text" 
					value="${vo.subject}"
					readonly="readonly"
				/>
			</td>
			
		</tr>
<!--  -->
		<tr>
		
			<th class="align-middle">
				<label for="content">내용</label>
			</th>
			<td colspan="3">
				<textarea 
					id="content"
					class="form-control form-control-sm" 
					rows="10"
					style="resize: none;"
					readonly="readonly"
				>${vo.content}</textarea>
			</td>
			
		</tr>
<!--  -->
	</table>
	
</div><br/>

<!-- 답글을 입력하는 테이블 -->
<form class="m-3" action="replyInsert.nhn" method="post">

	<table class="table table-hover" style="width: 600px; margin-left: auto; margin-right: auto;">
<!--  -->
		<tr style="display: none;">
			<td colspan="2">
			
				<input name="idx" type="text" value="${vo.idx}" size="1"/> <!-- 질문글의 글번호 -->
				<input name="currentPage" type="text" value="${currentPage}" size="1"/> <!-- 답글 입력 후 돌아갈 페이지 번호 -->
				<input name="gup" type="text" value="${vo.gup}" size="1"/> <!-- 글 그룹 -->
				<input name="lev" type="text" value="${vo.lev}" size="1"/> <!-- 글 레벨 -->
				<input name="seq" type="text" value="${vo.seq}" size="1"/> <!-- 같은 글 그룹에서 글 출력 순서 -->
	
			</td>
		</tr>
		
		<tr class="table-primary">
		
			<th class="align-middle text-center" colspan="2" style="font-size: 30px;">
				답글 입력
			</th>
			
		</tr>
<!--  -->
		<tr>
		
			<th class="align-middle" width="100px">
				<label for="name">이름</label>
			</th>
			<td width="500px">
				<input 
					id="name"
					class="form-control form-control-sm"
					type="text"
					name="name"
					style="width: 200px;"
				/>
			</td>
					
		</tr>
<!--  -->
		<tr>
		
			<th class="align-middle">
				<label for="subject">제목</label>
			</th>
			<td>
				<input 
					id="subject" 
					class="form-control form-control-sm" 
					type="text" 
					name="subject" 
				/>
			</td>
			
		</tr>
<!--  -->
		<tr>
		
			<th class="align-middle">
				<label for="content">내용</label>
			</th>
			<td>
				<textarea 
					id="content"
					class="form-control form-control-sm" 
					name="content"
					rows="10"
					style="resize: none;"
				>
				</textarea>
			</td>
			
		</tr>
<!--  -->	
		<tr class="table-secondary">
		
			<th colspan="2" class="align-middle text-center">
				<input
					class="btn btn-outline-primary btn-sm"
					type="submit"
					value="저장하기"
					style="font-size: 13px;"
				/>
				<input
					class="btn btn-outline-danger btn-sm"
					type="reset"
					value="다시쓰기"
					style="font-size: 13px;"
				/>
				<input
					class="btn btn-outline-success btn-sm"
					type="button"
					value="돌아가기"
					style="font-size: 13px;"
					onclick="history.back()"
				/>
				<input
					class="btn btn-outline-dark btn-sm"
					type="button"
					value="목록보기"
					style="font-size: 13px;"
					onclick="location.href='list.nhn?currentPage=${currentPage}'"
				/>
			</th>
			
		</tr>	
<!--  -->	
	</table>
	
</form>



</body>
</html>















