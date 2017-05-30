<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
</head>
<body>
<jsp:include page="fragments/headTag.jsp"/>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
	<h3><spring:message code="meals.title"/></h3>
	<form method="post" action="${pageContext.request.contextPath}/meals/filter">
		<dl>
			<dt><spring:message code="meals.from_date"/>:</dt>
			<dd><input type="date" name="startDate" value="${param.startDate}"></dd>
		</dl>
		<dl>
			<dt><spring:message code="meals.to_date"/>:</dt>
			<dd><input type="date" name="endDate" value="${param.endDate}"></dd>
		</dl>
		<dl>
			<dt><spring:message code="meals.from_time"/>:</dt>
			<dd><input type="time" name="startTime" value="${param.startTime}"></dd>
		</dl>
		<dl>
			<dt><spring:message code="meals.to_time"/>:</dt>
			<dd><input type="time" name="endTime" value="${param.endTime}"></dd>
		</dl>
		<button type="submit"><spring:message code="meals.filter"/></button>
	</form>
	<hr>
	<a href="${pageContext.request.contextPath}/meals/add"><spring:message code="meals.add"/></a>
	<hr>
	<table border="1" cellpadding="8" cellspacing="0">
		<thead>
		<tr>
			<th><spring:message code="meals.date_time"/></th>
			<th><spring:message code="meals.description"/></th>
			<th><spring:message code="meals.calories"/></th>
			<th></th>
			<th></th>
		</tr>
		</thead>
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
				<td><a href="meals/edit/${meal.id}"><spring:message code="meals.edit"/></a></td>
				<td><a href="meals/remove/${meal.id}"><spring:message code="meals.delete"/></a></td>
			</tr>
		</c:forEach>
	</table>
</section>
</body>
<jsp:include page="fragments/footer.jsp"/>
</html>