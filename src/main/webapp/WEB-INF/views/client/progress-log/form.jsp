<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.progress-log.form.label.recordId" path="recordId" placeholder="PG-AA-1234"/>
	<acme:input-double code="client.progress-log.form.label.completeness" path="completeness" placeholder="0 - 100.00"/>
	<acme:input-textarea code="client.progress-log.form.label.comment" path="comment"/>
	<acme:input-textbox code="client.progress-log.form.label.responsiblePerson" path="responsiblePerson" />
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true  }">
			<acme:submit code="client.progress-log.form.button.update" action="/client/progress-log/update"/>
			<acme:submit code="client.progress-log.form.button.delete" action="/client/progress-log/delete"/>
			<acme:submit code="client.progress-log.form.button.publish" action="/client/progress-log/publish"/>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'create')}">
			<acme:submit code="client.progress-log.form.button.create" action="/client/progress-log/create?masterId=${masterId}"/>

		</jstl:when>
	</jstl:choose>

</acme:form>