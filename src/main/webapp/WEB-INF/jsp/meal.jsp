<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title><spring:message code="meals.title"/></title>
</head>
<body>
<jsp:include page="fragments/headTag.jsp"/>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
	    <c:if test="${meal.id == 0}">
	        <spring:message code="meals.update"/>
		    <c:otherwise>
		    <spring:message code="meals.create"/>
		    </c:otherwise>
		    </c:if>
	    </h2>
    <hr>
    <form method="post" action="${pageContext.request.contextPath}/meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meals.date_time"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories"></dd>
        </dl>
        <button type="submit"><spring:message code="meals.save"/></button>
        <button onclick="window.history.back()"><spring:message code="meals.cancel"/> </button>
    </form>
</section>
</body>
<jsp:include page="fragments/footer.jsp"/>
</html>
