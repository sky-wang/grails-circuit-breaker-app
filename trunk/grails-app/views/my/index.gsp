<html>
<body>
	
	<p>
	<strong>Last request:</strong>
	Message: ${message}<br />
	<br />
	<strong>Next request: </strong>
	<g:link action='index' params="${[fail:true]}">Fail</g:link>
	 | 
	<g:link action='index'>Success</g:link>
	
	</p>
	<p>
		<ul>
		<li>Calls should fail fast after <strong>${threshold} failures</strong> within <strong>${timeout}ms</strong> of the first failure. </li>
		<li>There are currently <strong>${failures} failures</strong>. </li>
		</ul>
	</p>	
	
</body>
</html>
