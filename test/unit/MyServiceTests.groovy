import grails.spring.*
import breaker.*

class MyServiceTests extends grails.test.GrailsUnitTestCase
{
	void setUp()
	{
		super.setUp()
		mockLogging(MyService)
	}
	
	void testAroundAdviceWithMetaprogramming()
	{
		def circuitBreaker = MetaclassCircuitBreaker.wrap(MyService, 'testCallOne', [Boolean])
		
		assertEquals CircuitBreakerClosedState, circuitBreaker.state.class
		assertEquals 10, circuitBreaker.state.failureThresholdCount
		assertEquals 0, circuitBreaker.state.failureCount
		assertEquals 5000, circuitBreaker.openState.timeoutMillis

		circuitBreaker.failureThresholdCount = 3
		assertEquals 3, circuitBreaker.state.failureThresholdCount


		def myService = new MyService()

		def result = myService.testCallOne(false)
		
		assertEquals 'Success', result
		assertEquals CircuitBreakerClosedState, circuitBreaker.state.class

		shouldFail {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerClosedState, circuitBreaker.state.class
		assertEquals 1, circuitBreaker.state.failureCount
		
		shouldFail {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerClosedState, circuitBreaker.state.class
		assertEquals 2, circuitBreaker.state.failureCount
		
		// as configured above, third failure should trip the breaker
		shouldFail {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerOpenState, circuitBreaker.state.class

		Thread.currentThread().sleep(1000)

		// still open
		assertEquals CircuitBreakerOpenState, circuitBreaker.state.class

		Thread.currentThread().sleep(4000)

		// now we should be ready to attempt to reset the breaker
		// another error should throw a failed reset exception
		shouldFail(CircuitBreakerFailedResetException) {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerOpenState, circuitBreaker.state.class

		Thread.currentThread().sleep(5001)

		// now we should be ready to attempt to reset the breaker 
		// and a successful attempt should fully reset to a closed state
		result = myService.testCallOne(false)
		assertEquals 'Success', result
		
		// everything is back to normal when the first call succeeds in a reset attempt
		assertEquals CircuitBreakerClosedState, circuitBreaker.state.class
		assertEquals 0, circuitBreaker.state.failureCount
	}
	
	void testAroundAdviceWithAspect()
	{
		def bb = new BeanBuilder()

		bb.beans {
			xmlns aop:"http://www.springframework.org/schema/aop"

			myService(MyService) 
			myServiceCircuitBreakerAspect(CircuitBreakerAspect) {
				id = 'myServiceAspect'
				failureThresholdCount = 3 // trip breaker after three failures
				timeoutMillis = 5000 // fail fast for 5 seconds (giving the service a chance to heal itself), then try again
			}

			aop.config("proxy-target-class":true) {
				aspect( id:"mySvcAspectId", ref:"myServiceCircuitBreakerAspect" ) {
					around method:"invoke", pointcut: "execution(* MyServiceInterface.testCallOne(..))"
				}
			}
		}


		def appCtx = bb.createApplicationContext()

		// check service
		def myService = appCtx.getBean("myService")
		CircuitBreaker myAspect = appCtx.getBean('myServiceCircuitBreakerAspect')

		assertEquals 'myServiceAspect', myAspect.id
		assertEquals CircuitBreakerClosedState, myAspect.state.class
		assertEquals 3, myAspect.state.failureThresholdCount
		assertEquals 0, myAspect.state.failureCount
		assertEquals 5000, myAspect.openState.timeoutMillis



		def result = myService.testCallOne(false)
		
		assertEquals 'Success', result
		assertEquals CircuitBreakerClosedState, myAspect.state.class

		shouldFail {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerClosedState, myAspect.state.class
		assertEquals 1, myAspect.state.failureCount
		
		shouldFail {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerClosedState, myAspect.state.class
		assertEquals 2, myAspect.state.failureCount
		
		// as configured above, third failure should trip the breaker
		shouldFail {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerOpenState, myAspect.state.class

		Thread.currentThread().sleep(1000)

		// still open
		assertEquals CircuitBreakerOpenState, myAspect.state.class

		Thread.currentThread().sleep(4000)

		// now we should be ready to attempt to reset the breaker
		// another error should throw a failed reset exception
		shouldFail(CircuitBreakerFailedResetException) {
			myService.testCallOne(true)
		}
		assertEquals CircuitBreakerOpenState, myAspect.state.class

		Thread.currentThread().sleep(5001)

		// now we should be ready to attempt to reset the breaker 
		// and a successful attempt should fully reset to a closed state
		result = myService.testCallOne(false)
		assertEquals 'Success', result
		
		// everything is back to normal when the first call succeeds in a reset attempt
		assertEquals CircuitBreakerClosedState, myAspect.state.class
		assertEquals 0, myAspect.state.failureCount

	}

}
