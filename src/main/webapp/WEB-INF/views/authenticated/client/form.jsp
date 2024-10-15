<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.client.form.label.identification" path="identification" placeholder="TTT-0123"/>
	<acme:input-textbox code="authenticated.client.form.label.companyName" path="companyName"/>
	<acme:input-select code="authenticated.client.form.label.type" path="type" choices="${types}"/>
	<acme:input-textbox code="authenticated.client.form.label.email" path="email"/>	
	<acme:input-url code="authenticated.client.form.label.link" path="optionalLink"/>
	
	<acme:submit test="${_command == 'create'}" code="authenticated.client.form.button.create" action="/authenticated/client/create"/>
	<acme:submit test="${_command == 'update'}" code="authenticated.client.form.button.update" action="/authenticated/client/update"/>
</acme:form>