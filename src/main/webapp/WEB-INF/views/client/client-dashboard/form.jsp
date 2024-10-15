<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2><acme:message code="client.dashboard.form.title.general-indicators"/></h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:message code="client.dashboard.form.label.progress-log-below-25"/></th>
		<td><acme:print value="${totalNumProgressLogBelow25}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="client.dashboard.form.label.progress-log-between-25-50"/></th>
		<td><acme:print value="${totalNumProgressLogBetween25And50}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="client.dashboard.form.label.progress-log-between-50-75"/></th>
		<td><acme:print value="${totalNumProgressLogBetween50And75}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="client.dashboard.form.label.progress-log-above-75"/></th>
		<td><acme:print value="${totalNumProgressLogAbove75}"/></td>
	</tr>
</table>

<jstl:forEach var="currency" items="${supportedCurrencies}">
    <h2>
        <acme:message code="client.dashboard.form.label.contract-indicators"/>
        <acme:message code="${currency}"/>
    </h2>

    <table class="table table-sm">
        <tr>
            <th scope="row"><acme:message code="client.dashboard.form.label.contract-average"/></th>
            <td><acme:print value="${averageBudgetPerCurrency[currency]}"/></td>
        </tr>
        <tr>
            <th scope="row"><acme:message code="client.dashboard.form.label.contract-deviation"/></th>
            <td><acme:print value="${deviationBudgetPerCurrency[currency]}"/></td>
        </tr>
        <tr>
            <th scope="row"><acme:message code="client.dashboard.form.label.contract-minimum"/></th>
            <td><acme:print value="${minimumBudgetPerCurrency[currency]}"/></td>
        </tr>   
        <tr>
            <th scope="row"><acme:message code="client.dashboard.form.label.contract-maximum"/></th>
            <td><acme:print value="${maximumBudgetPerCurrency[currency]}"/></td>
        </tr>
    </table>
</jstl:forEach>