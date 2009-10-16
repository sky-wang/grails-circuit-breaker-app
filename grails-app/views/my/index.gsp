<html>
<body>
	
	<p>
	<strong>Last request:</strong>
	Message: ${message}<br />
	Message2: ${message2}<br />
	<br />
	<strong>Next request: </strong>
	<g:link action='index' params="${[fail:true]}">Fail</g:link>
	 | 
	<g:link action='index'>Success</g:link>
	
	</p>
	<p>
		<ul>
		<li>1: Calls should fail fast after <strong>${threshold} failures</strong> within <strong>${timeout}ms</strong> of the first failure. </li>
		<li>2: Calls should fail fast after <strong>${threshold2} failures</strong> within <strong>${timeout2}ms</strong> of the first failure. </li>
		<li>There are currently <strong>${failures} failures</strong>. </li>
		</ul>
	</p>	
	
</body>
</html>
