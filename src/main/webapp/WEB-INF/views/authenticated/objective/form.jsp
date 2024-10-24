<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.objective.form.label.title" path="title"/>
	<acme:input-textarea code="authenticated.objective.form.label.description" path="description"/>
	<acme:input-select code="authenticated.objective.form.label.priority" path="priority" choices="${priorities}"/>
	<acme:input-checkbox code="authenticated.objective.form.label.criticalStatus" path="criticalStatus"/>
	<acme:input-moment code="authenticated.objective.form.label.instantiationMoment" path="instantiationMoment"/>
	<acme:input-moment code="authenticated.objective.form.label.startDuration" path="startDuration"/>
	<acme:input-moment code="authenticated.objective.form.label.finishDuration" path="finishDuration"/>
	<acme:input-url code="authenticated.objective.form.label.optionalLink" path="optionalLink"/>
</acme:form>