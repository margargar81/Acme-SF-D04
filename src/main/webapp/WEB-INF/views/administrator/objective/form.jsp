<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.objective.form.label.title" path="title" />
	<acme:input-textbox code="administrator.objective.form.label.description" path="description" />
	<acme:input-moment code="administrator.objective.form.label.instantiationMoment" path="instantiationMoment"/>	
	<acme:input-moment code="administrator.objective.form.label.startDuration" path="startDuration" />
	<acme:input-moment code="administrator.objective.form.label.finishDuration" path="finishDuration" />
	<jstl:if test="${_command == 'create'}">
		<acme:input-select code="administrator.objective.form.label.priority" path="priority" choices="${priorities}"/>
	</jstl:if>
	<acme:input-checkbox code="administrator.objective.form.label.criticalStatus" path="criticalStatus" />
	<acme:input-url code="administrator.objective.form.label.optionalLink" path="optionalLink" />
	<jstl:if test="${_command == 'create'}">
	<acme:input-checkbox code="administrator.objective.form.label.confirmation" path="confirmation" />
	</jstl:if>
	
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="administrator.objective.form.button.create" action="/administrator/objective/create"/>
	</jstl:if>	
</acme:form>