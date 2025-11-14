package ch.css.learning.jobrunr.lib;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jobrunr.dashboard.JobRunrDashboardWebServer;
import org.jobrunr.server.BackgroundJobServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class JobRunrServerStarter {
    private static final Logger logger = LoggerFactory.getLogger(JobRunrServerStarter.class);

    @Inject
    BackgroundJobServer backgroundJobServer;

    @Inject
    JobRunrDashboardWebServer jobRunrDashboardWebServer;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        jobRunrDashboardWebServer.start();
        backgroundJobServer.start();
        logger.info("JobRunr BackgroundJobServer started.");
    }
}
