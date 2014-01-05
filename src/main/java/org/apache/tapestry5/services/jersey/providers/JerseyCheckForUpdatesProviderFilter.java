package org.apache.tapestry5.services.jersey.providers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Invokable;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.ConcurrentBarrier;
import org.apache.tapestry5.ioc.util.TimeInterval;
import org.apache.tapestry5.services.UpdateListenerHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a barrier that periodically asks the {@link org.apache.tapestry5.services.UpdateListenerHub} to check for
 * updates to files. The UpdateListenerHub is invoked from a write method, meaning that when it is called, all other
 * threads will be blocked.
 * <p/>
 * Adapted from org.apache.tapestry5.internal.services.CheckForUpdatesFilter
 */

@Provider
@PreMatching
@Priority(100)
public class JerseyCheckForUpdatesProviderFilter implements ContainerRequestFilter
{

    private final Logger log = LoggerFactory.getLogger(JerseyCheckForUpdatesProviderFilter.class);

    private final TimeInterval checkInterval;

    private final TimeInterval updateTimeout;

    private final UpdateListenerHub updateListenerHub;

    private final ConcurrentBarrier barrier = new ConcurrentBarrier();

    private final Runnable checker = new Runnable()
    {
        public void run()
        {
            // On a race condition, multiple threads may hit this method briefly. If we've
            // already done a check, don't run it again.

            if (System.currentTimeMillis() - lastCheck >= checkInterval.milliseconds())
            {
                // Fire the update event which will force a number of checks and then
                // corresponding invalidation events.

                log.debug("Firing check for updates...");

                updateListenerHub.fireCheckForUpdates();
                lastCheck = System.currentTimeMillis();
            }
        }
    };

    private long lastCheck = 0;

    /**
     * @param checkInterval     interval between checks
     * @param updateTimeout     time to wait to obtain update lock.
     * @param updateListenerHub invoked, at intervals, to spur the process of detecting changes
     */
    public JerseyCheckForUpdatesProviderFilter(@Symbol(SymbolConstants.FILE_CHECK_INTERVAL) TimeInterval checkInterval,
                                               @Symbol(SymbolConstants.FILE_CHECK_UPDATE_TIMEOUT) TimeInterval updateTimeout,
                                               UpdateListenerHub updateListenerHub)
    {
        this.updateListenerHub = updateListenerHub;
        this.checkInterval = checkInterval;
        this.updateTimeout = updateTimeout;
    }

    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        Invokable<Boolean> invokable = new Invokable<Boolean>()
        {
            public Boolean invoke()
            {
                if (System.currentTimeMillis() - lastCheck >= checkInterval.milliseconds())
                {
                    return barrier.tryWithWrite(checker, updateTimeout.milliseconds(), TimeUnit.MILLISECONDS);
                }

                return false;
            }
        };

        // Obtain a read lock while handling the request. This will not impair parallel operations,
        // except when a file check is needed (the exclusive write lock will block threads attempting
        // to get a read lock).

        boolean result = barrier.withRead(invokable);
    }

}
