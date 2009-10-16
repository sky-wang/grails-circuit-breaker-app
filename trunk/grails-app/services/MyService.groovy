class MyService implements MyServiceInterface
{
	def testCallOne(boolean fail)
	{
		log.debug "MyService.testCallOne(${fail})"
		
		if(fail)
		{
			throw new RuntimeException('Exception caused in MyService.testCallOne()')
		}
		
		return "Success"
	}

	// Not in interface
	def testCallTwo(boolean fail)
	{
		log.debug "MyService.testCallTwo(${fail})"
		
		if(fail)
		{
			throw new RuntimeException('Exception caused in MyService.testCallTwo()')
		}
		
		return "Success"
	}
}
