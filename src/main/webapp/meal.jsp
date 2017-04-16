<%--
  Created by IntelliJ IDEA.
  User: zandr
  Date: 15.04.2017
  Time: 13:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
	<title>Meal</title>
	<style type="text/css">
		.tr_red td {
			background-color: darkcyan;
			color: red;
		}
		
		.tr_green td {
			background-color: cornflowerblue;
			color: green;
		}
	</style>
</head>
<body>
<h2 align="center">Your meal:</h2>
<table border="1" align="center">
	<jsp:useBean id="mealList" type="java.util.List<ru.javawebinar.topjava.model.MealWithExceed>"
	             scope="request"/>
	<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
	
	<tr bgcolor="#ff7f50">
		<th>Id</th>
		<th>Date</th>
		<th>Description</th>
		<th>Calories</th>
		<th>Edit</th>
		<th>Delete</th>
	</tr>
	<c:forEach var="meal" items="${mealList}">
		<c:choose>
			<c:when test="${meal.exceed==true}">
				<tr bgcolor="#dc143c">
			</c:when>
			<c:when test="${meal.exceed==false}">
				<tr bgcolor="#00c600">
			</c:when>
		</c:choose>
		<td width="50">${meal.id}</td>
		<td width="150"><javatime:format value="${meal.dateTime}" pattern="dd.MM.yyyy
		HH:mm"/></td>
		<td width="200">${meal.description}</td>
		<td width="100">${meal.calories}</td>
		<td>
			<a href="${pageContext.request.contextPath}/meals?action=edit&id=${meal.id}">Edit</a>
		</td>
		<td><a href="${pageContext.request.contextPath}/meals?action=delete&id=${meal.id}">Delete
		</a>
		</td>
		</tr>
	</c:forEach>
</table>
<form action="${pageContext.request.contextPath}/meals" method="post">
	<table align="center">
		<tr>
			<td>
				<c:if test="${meal.id != 0}">
				<input readonly="readonly" type="text" name="id" value="${meal.id}" title="Id">
				</c:if>
			</td>
			<td>
				<input type="datetime-local" pattern="dd.MM.yyyy HH:mm" name="dateTime"
				       value="<c:out
				value="${meal.id != 0 ? meal.dateTime : ''}"/>" title="Data and Time"/>
			</td>
			<td>
				<input type="text" name="description" title="Description" value="<c:out
				value="${meal.id != 0 ? meal.description : ''}" />"/>
			</td>
			<td>
				<input type="text" name="calories" title="Calories" value="<c:out
				value="${meal.id != 0 ? meal.calories : ''}"/>"/>
			</td>
			<td>
				<input type="submit" name="Submit"/>
			</td>
		</tr>
	</table>

</form>
</body>
</html>
