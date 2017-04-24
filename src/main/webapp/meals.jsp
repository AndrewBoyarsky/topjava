<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="ru.javawebinar.topjava.util.DateTimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
	<title>Meal list</title>
	<style>
		.normal {
			color: green;
		}
		
		.exceeded {
			color: red;
		}
	</style>
</head>
<body>
<section>
	<h2><a href="index.html">Home</a></h2>
	<h2>Meal list</h2>
	<a href="meals?action=create">Add Meal</a>
	<hr>
	<table border="1" cellpadding="8" cellspacing="0">
		<thead>
		<tr>
			<th>Date</th>
			<th>Description</th>
			<th>Calories</th>
			<th></th>
			<th></th>
		</tr>
		</thead>
		<jsp:useBean id="meals" type="java.util.List<ru.javawebinar.topjava.to.MealWithExceed>" scope="request"/>
		<c:forEach items="${meals}" var="meal">
			<jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
			<tr class="${meal.exceed ? 'exceeded' : 'normal'}">
				<td>
						<%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
						<%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
						${fn:formatDateTime(meal.dateTime)}
				</td>
				<td>${meal.description}</td>
				<td>${meal.calories}</td>
				<td><a href="meals?action=update&id=${meal.id}">Update</a></td>
				<td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
			</tr>
		</c:forEach>
		<c:if test="${empty meals}">
			<c:out value="Nothing to show."/>
		</c:if>
	</table>
</section>
<br>
<form method="post" action="<c:url value="/meals"/>">
	<input type="date" name="fromDate" value="<%=LocalDate.now()%>">
	<input type="date" name="toDate" value="<%=LocalDate.now()%>">
	<input type="submit" name="filterByDate" value="filter">
</form>
<br>
<form method="post" action="<c:url value="/meals"/>">
	<input type="time" name="fromTime" value="<%=LocalTime.now().format(DateTimeUtil.TIME_FORMATTER)%>">
	<input type="time" name="toTime" value="<%=LocalTime.now().format(DateTimeUtil.TIME_FORMATTER)%>">
	<input type="submit" name="filterByTime" value="filter">
</form>
</body>
</html>