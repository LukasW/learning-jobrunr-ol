package ch.css.learning.jobrunr.lib;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import org.jobrunr.server.JobActivator;

@ApplicationScoped
public class CdiJobActivator implements JobActivator {

    @Override
    public <T> T activateJob(Class<T> type) {
        // Use CDI to find and return a managed instance of the requested class
        return CDI.current().select(type).get();
    }
}
