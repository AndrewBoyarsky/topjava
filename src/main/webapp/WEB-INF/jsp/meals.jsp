<%@ page contentType="text/html;charset=UTF-8"%>

<%@ page language="java"%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>

<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section class="jumbotron">
	<div class="container">
		<div class="shadow">
			<h3><spring:message code="meals.title"/></h3>
			<div class="row">
				<div class="col-sm-3 col-sm-offset-0">
					<a class="btn btn-info" onclick="add()">
						<i class="glyphicon glyphicon-plus"></i></a>
				</div>
			</div>
			<div class="container">
				
				<form class="form" method="post" id="filterForm">
					<div class="form-group">
						<div class="row">
							
							<div class="col-sm-3">
								<label class="control-label" for="startDate">
									<spring:message
											code="meals.startDate"/></label>
							</div>
							<div class="col-sm-3">
								<input class="form-control" type="date" name="startDate"
								       value="${param.startDate}"
								       id="startDate">
							</div>
							<div class="col-sm-3">
								<label class="control-label " for="endDate"><spring:message
										code="meals.endDate"/>:</label>
							</div>
							<div class="col-sm-3">
								<input class="form-control" type="date" name="endDate"
								       value="${param.endDate}"
								       id="endDate">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							
							<div class="col-sm-3">
								<label class="control-label "
								       for="startTime"><spring:message
										code="meals.startTime"/>:</label>
							</div>
							<div class="col-sm-3">
								
								<input class="form-control" type="time" name="startTime"
								       value="${param.startTime}"
								       id="startTime">
							</div>
							<div class="col-sm-3">
								<label class="control-label" for="endTime"><spring:message
										code="meals.endTime"/>:</label>
							</div>
							<div class="col-sm-3">
								
								<input class="form-control" type="time" name="endTime"
								       value="${param.endTime}" id="endTime">
							</div>
						</div>
					</div>
					<div class="form-group text-center">
						<div class="btn-group">
							<button class="btn btn-primary btn"
							        type="submit"><i class="glyphicon glyphicon-filter"></i>
							</button>
							<button class="btn btn-danger btn"
							        type="button" onclick="updateTable()"><i class="glyphicon glyphicon-remove"></i>
							</button>
						</div>
					</div>
				</form>
			</div>
			
			<table id="meal-datatable" class="table table-striped">
				<thead>
				<tr>
					<th><spring:message code="meals.dateTime"/></th>
					<th><spring:message code="meals.description"/></th>
					<th><spring:message code="meals.calories"/></th>
					<th></th>
					<th></th>
				</tr>
				</thead>
				<c:forEach items="${meals}" var="meal">
					<jsp:useBean id="meal" scope="page"
					             type="ru.javawebinar.topjava.to.MealWithExceed"/>
					<tr
class="${meal.exceed ? 'exceeded' : 'normal'}">
						<td>
								<%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>*/
								<%--TimeUtil.toString(meal.getDateTime())--%>*/*/
								${fn:formatDateTime(meal.dateTime)}
						</td>
						<td>${meal.description}</td>
						<td>${meal.calories}</td>
						<td><a
								class="btn btn-warning btn-xs"
							<%--onclick="save(${meal.id})"--%>
							<%--id="${meal.id}"--%>
							
							<%--href="meals/update?id=${meal.id}">--%>><i
								class="glyphicon glyphicon-edit"></i>
						</a>
						</td>
						<td><a
								class="btn btn-danger btn-delete btn-xs"
								onclick="deleteRow(${meal.id})">
								<%--id="${meal.id}" &lt;%&ndash;href="meals/delete?id=${meal.id}"&ndash;%&gt;><i--%>
							<i class="glyphicon glyphicon-remove"></i> </a>
						</td>
					</tr>
				</c:forEach>
			</table>
</section>

<%--modal form--%>
<div id="modal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title"><spring:message code="${'meals.add'}"/></h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" id="newDataForm">
					<input type="hidden" id="id" name="id" value="${meal.id}">
					
					<div class="form-group">
						<label class="control-label col-xs-3 " for="dateTime"><spring:message
								code="meals.dateTime"/>:</label>
						<div class="col-xs-8">
							<input id="dateTime" class="form-control" type="datetime-local"
							       value="${meal.dateTime}" name="dateTime">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-xs-3" for="description"><spring:message
								code="meals.description"/>:</label>
						<div class="col-xs-8">
							<input id="description" class="form-control" type="text"
							       value="${meal.description}" size=40 name="description">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-xs-3" for="calories"><spring:message
								code="meals.calories"/>:</label>
						<div class="col-xs-8">
							
							<input class="form-control" id="calories" type="number"
							       value="${meal.calories}" name="calories">
						</div>
					</div>
					<div class="form-group">
						<div class="col-xs-4 col-xs-offset-4">
							<div class="btn-group-lg">
								<button class="btn btn-primary btn-lg" type="submit"><i
										class="glyphicon glyphicon-ok"></i>
								</button>
								<button class="btn btn-danger btn-lg" type="button"
								        data-dismiss="modal"><i
										class="glyphicon glyphicon-remove"></i>
								</button>
							</div>
						</div>
					</div>
				</form>
			</div>
			<%--<div class="modal-footer">--%>
			<%--<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>--%>
			<%--</div>--%>
		</div>
	
	</div>
</div>
</div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
