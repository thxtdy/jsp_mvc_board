<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
</head>
<body>
	<h2>게시글 목록</h2>
	<div class="action">
		<a href="${pageContext.request.contextPath}/board/create">새 글 작성하기</a>
		<a href="${pageContext.request.contextPath}/index.jsp">홈 화면</a>
	</div>
	
	<!-- board list 생성 -->
	<!-- 반복문 사용 예정 -->
	<div class="board-item">
	
	</div>
	<br>
	<div>
		<span><a href="${pageContext.request.contextPath}/board/list?page=1">1</a></span>
		<span><a href="${pageContext.request.contextPath}/board/list?page=2">2</a></span>
		<span><a href="${pageContext.request.contextPath}/board/list?page=3">3</a></span>
		<span><a href="${pageContext.request.contextPath}/board/list?page=4">4</a></span>
		
	</div>
</body>
</html>