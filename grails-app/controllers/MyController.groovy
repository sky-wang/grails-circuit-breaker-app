class MyController 
{
	def myService
	def breakerAspectOne
	
	def index = {
		
		def fail = params.fail ? true : false
		
		log.debug "MyController.index: about to fail: ${fail}"

		def message
		
		try
		{
			message = myService.testCallOne(fail)
		}
		catch(Exception e)
		{
			message = e.message
		}

		def timeout = breakerAspectOne.timeoutMillis
		def threshold = breakerAspectOne.failureThresholdCount
		def failures = breakerAspectOne.currentFailureCount

		return [message:message, lastCallFailed:fail, timeout:timeout, threshold:threshold, failures:failures]
	}
}