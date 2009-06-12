
beans = {

	xmlns aop:"http://www.springframework.org/schema/aop"

	breakerAspectOne(breaker.CircuitBreakerAspect)
	{
		id = 'breaker-one'
		failureThresholdCount = 3 // trip breaker after three failures
		timeoutMillis = 5000 // fail fast for 5 seconds (giving the service a chance to heal itself), then try again
	}
	
	breakerAspectTwo(breaker.CircuitBreakerAspect)
	{
		id = 'breaker-two'
	}
	
	breakerAspectThree(breaker.CircuitBreakerAspect)
	{
		id = 'breaker-three'
	}
	
	aop.config("proxy-target-class":true) { 
		aspect(id:"serviceTestCallOneBreaker", ref:"breakerAspectOne" ) { 
			around method:"invoke", pointcut: "execution(* MyServiceInterface.testCallOne(..))"
		} 
	}
    
}