
class CircuitBreakerController
{
	def list = {
		
		Map circuitBreakerBeansMap
		Throwable e
		
		try
		{
			circuitBreakerBeansMap = grailsApplication.mainContext.getBeansOfType(breaker.CircuitBreakerAspect /* , boolean includeNonSingletons, boolean allowEagerInit */ )
		}
		catch(Throwable ex)
		{
			e = ex
		}
		
		return [circuitBreakerBeansMap:circuitBreakerBeansMap, error:e]
	}
	
	def show = {
		
		Map circuitBreakerBeansMap
		Throwable e
		
		try
		{
			def circuitBreaker = grailsApplication.mainContext.getBean(params.id)

			circuitBreakerBeansMap = [:]
			circuitBreakerBeansMap[params.id] = circuitBreaker
		}
		catch(Throwable ex)
		{
			e = ex
		}		
		
		render(view:'list', model:[circuitBreakerBeansMap:circuitBreakerBeansMap, error:e]) 
	}
}
