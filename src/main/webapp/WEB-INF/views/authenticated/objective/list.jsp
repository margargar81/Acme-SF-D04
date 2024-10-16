<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.objective.list.label.title" path="title" width="25%"/>
	<acme:list-column code="authenticated.objective.list.label.startDuration" path="startDuration" width="25%"/>
	<acme:list-column code="authenticated.objective.list.label.finishDuration" path="finishDuration" width="25%"/>
	<acme:list-column code="authenticated.objective.list.label.priority" path="priority" width="25%"/>
	<acme:list-payload path="payload"/>
</acme:list>