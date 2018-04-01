<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron">
	<div class="container">
		<div class="col-xs-10 col-xs-offset-1">
			
			<h3><spring:message code="user.email.resend"/></h3>
			<form:form class="form" method="post" action="resendToken">
				<div class="form-group">
					<label for="email" class="control-label col-xs-3"><spring:message
							code="user.email"/></label>
					<div class="col-xs-7">
						<input class="form-control" id="email" name="email" type="email"
						       placeholder="<spring:message code="user.email"/>">
					</div>
				</div>
				<button class="btn btn-primary col-xs-2" type="submit"><spring:message
						code="app.send"/></button>
			</form:form>
		</div>
	</div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>