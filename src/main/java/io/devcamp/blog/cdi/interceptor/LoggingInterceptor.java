package io.devcamp.blog.cdi.interceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * @author Daniel Sachse
 * @date 04.02.14 21:25
 */

@Interceptor
@Logging
public class LoggingInterceptor {

    @Inject
    private Logger logger;

    @AroundInvoke
    public Object processLogging(InvocationContext ic) throws Exception {
        long before = System.currentTimeMillis();
        Object o = ic.proceed();
        long after = System.currentTimeMillis();

        logger.info("Duration of method call to " + ic.getMethod() + ": " + (after - before) + "ms");

        return o;
    }
}
