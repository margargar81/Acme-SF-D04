<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2><acme:message code="manager.dashboard.form.title.project-indicators"/></h2>

<!-- Project Cost Indicators -->
<table class="table table-sm">
	<!-- Average Project Cost -->
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.avg-project-cost"/></th>
	</tr>
	<jstl:forEach var="entry" items="${avgProjectCost}">
		<tr>
			<td><acme:print value="${entry.value}"/> <acme:print value="${entry.key}"/></td>
		</tr>
	</jstl:forEach>

	<!-- Deviation Project Cost -->
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.dev-project-cost"/></th>
	</tr>
	<jstl:forEach var="entry" items="${devProjectCost}">
		<tr>
			<td><acme:print value="${entry.value}"/> <acme:print value="${entry.key}"/></td>
		</tr>
	</jstl:forEach>

	<!-- Minimum Project Cost -->
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.min-project-cost"/></th>
	</tr>
	<jstl:forEach var="entry" items="${minProjectCost}">
		<tr>
			<td><acme:print value="${entry.value}"/> <acme:print value="${entry.key}"/></td>
		</tr>
	</jstl:forEach>

	<!-- Maximum Project Cost -->
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.max-project-cost"/></th>
	</tr>
	<jstl:forEach var="entry" items="${maxProjectCost}">
		<tr>
			<td><acme:print value="${entry.value}"/> <acme:print value="${entry.key}"/></td>
		</tr>
	</jstl:forEach>
</table>

<h2><acme:message code="manager.dashboard.form.title.user-story-indicators"/></h2>

<!-- User Story Estimated Cost Indicators -->
<table class="table table-sm">
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.avg-user-story-estimated-cost"/></th>
		<td><acme:print value="${avgUserStoryEstimatedCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.dev-user-story-estimated-cost"/></th>
		<td><acme:print value="${devUserStoryEstimatedCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.min-user-story-estimated-cost"/></th>
		<td><acme:print value="${minUserStoryEstimatedCost}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="manager.dashboard.form.label.max-user-story-estimated-cost"/></th>
		<td><acme:print value="${maxUserStoryEstimatedCost}"/></td>
	</tr>
</table>

<h2><acme:message code="manager.dashboard.form.title.user-story-priorities"/></h2>

<!-- User Story Priority Chart -->
<div>
	<canvas id="priorityChart"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		// Data for the priority chart
		var data = {
			labels: ["MUST", "SHOULD", "COULD", "WONT"],
			datasets: [{
				data: [
					<jstl:out value="${mustUserStories}"/>, 
					<jstl:out value="${shouldUserStories}"/>, 
					<jstl:out value="${couldUserStories}"/>, 
					<jstl:out value="${wontUserStories}"/>
				],
				backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"]
			}]
		};

		// Options for the priority chart
		var options = {
			scales: {
				yAxes: [{
					ticks: { suggestedMin: 0, suggestedMax: 3 }
				}]
			},
			legend: { display: false }
		};

		// Initialize the chart
		var ctx = document.getElementById("priorityChart").getContext("2d");
		new Chart(ctx, { type: "bar", data: data, options: options });
	});
</script>

<acme:return/>
