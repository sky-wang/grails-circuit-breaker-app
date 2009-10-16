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

		def message2
		
		try
		{
			message2 = myService.testCallTwo(fail)
		}
		catch(Exception e)
		{
			message2 = e.message
		}

		def timeout = breakerAspectOne.timeoutMillis
		def threshold = breakerAspectOne.failureThresholdCount
		def failures = breakerAspectOne.currentFailureCount

		return [
			lastCallFailed:fail, 
			message:message, 
			timeout:timeout, 
			threshold:threshold, 
			message2:message2, 
			timeout2:7000, // timeout2, 
			threshold2:3, // threshold2, 
			failures:failures
		]
	}
}