<html>

<body>

<g:if test="${params.id}">
<h1>Circuit Breaker: ${params.id}</h1>
</g:if>
<g:else>
<h1>Circuit Breakers</h1>
</g:else>


<g:if test="${error}">
<div>${error.message} (view source for stack trace)
	<div style='display:none'>
		<%
		def stack = new StringWriter()
		error.printStackTrace(new PrintWriter(stack))
		%>
${stack?.encodeAsHTML()}
	</div>
</div>
</g:if>

<g:if test="${circuitBreakerBeansMap}">
<table border='1' cellspacing='0'>
	<tr>
		<th>&nbsp;</th>
		<th colspan=''>Circuit Breaker</th>
	</tr>
	<tr>
		<th>Bean name</th>
		<th>id</th>
		<th>State</th>
		<th>Failure Threshold</th>
		<th>Failure Count</th>
		<th>Open state timeout</th>
	</tr>
	<g:each var='circuitBreakerBeanName' in="${circuitBreakerBeansMap.keySet().sort()}">
		<g:set var='circuitBreaker' value="${circuitBreakerBeansMap[circuitBreakerBeanName]}" />

		<g:set var='circuitBreakerId' value="${circuitBreaker.id}" />
		<g:set var='stateAsString' value="${circuitBreaker.stateAsString}" />
		<g:set var='threshold' value="${circuitBreaker.failureThresholdCount}" />
		<g:set var='failures' value="${circuitBreaker.currentFailureCount}" />
		<g:set var='timeout' value="${circuitBreaker.timeoutMillis}" />

		<tr>
			<td>
				<g:if test="${params.id}">
					${circuitBreakerBeanName}
				</g:if>
				<g:else>
					<g:link action='show' id='${circuitBreakerBeanName}'>${circuitBreakerBeanName}</g:link>
				</g:else>
			</td>
			<td>${circuitBreakerId}</td>
			<td>${stateAsString}</td>
			<td>${threshold}</td>
			<td>${failures}</td>
			<td>${timeout}</td>
		</tr>
	</g:each>
</table>
</g:if>
<g:else>
	There are no Circuit Breaker spring beans defined.
</g:else>
</body>
</html>
