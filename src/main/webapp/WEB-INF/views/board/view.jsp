<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%-- <link rel = "stylesheet" type = "text/css" href="${pageContext.request.contextPath}/resources/css/common.css"> --%>
<link rel = "stylesheet" type = "text/css" href="${pageContext.request.contextPath}/resources/css/view.css">
</head>
<body>
	<div class="container">
	<h2>${board.title}</h2>
	<p>${board.content}</p>
	<p><fmt:formatDate value="${board.createdAt}" pattern="yyyy-MM-dd HH:mm"/></p>
	</div>
	<c:if test="${board.userId == principal.id }">
		<a href="${pageContext.request.contextPath}/board/edit?id=${board.id}" class="btn btn-edit">수정</a>
		<a href="${pageContext.request.contextPath}/board/delete?id=${board.id}" class="btn btn-delete">삭제</a>
	</c:if>
	
	<a href="${pageContext.request.contextPath}/board/list?page=1" class="btn btn-back">목록으로 돌아가기</a>
	
	<h3>댓글</h3>
	<!-- 댓글 리스트 작성 -->
	
	<!-- 댓글 작성 폼 -->
	 <div class="comment-form">
        <form action="${pageContext.request.contextPath}/board/addComment" method="post">
            <textarea name="comment" placeholder="댓글을 입력하세요..." required></textarea>
            <br>
            <button type="submit" class="btn-submit">댓글 작성</button>
        </form>
    </div>
	
</body>
</html>