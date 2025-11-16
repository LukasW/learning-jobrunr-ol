package ch.css.learning.jobrunr.lib;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;
import org.jobrunr.configuration.JobRunrConfiguration;
import org.jobrunr.configuration.JobRunrPro;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.BackgroundJobServer;
import org.jobrunr.server.JobActivator;
import org.jobrunr.server.JobActivatorShutdownException;
import org.jobrunr.server.configuration.RoundRobinDynamicQueuePolicy;
import org.jobrunr.storage.sql.common.SqlStorageProviderFactory;

import javax.sql.DataSource;

import static org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration.usingStandardDashboardConfiguration;
import static org.jobrunr.server.BackgroundJobServerConfiguration.usingStandardBackgroundJobServerConfiguration;

@ApplicationScoped
public class JobRunrProInitializer {

    // Inject the DataSource you defined in server.xml
    @Resource(lookup = "jdbc/postgres-jobrunr-ds")
    private DataSource dataSource;

    private BackgroundJobServer backgroundJobServer;
    private JobScheduler jobScheduler;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        JobRunrConfiguration.JobRunrConfigurationResult jobRunrConfigurationResult = JobRunrPro.configure()
                .useJobActivator(new JobActivator() {
                    @Override
                    public <T> T activateJob(Class<T> type) throws JobActivatorShutdownException {
                        return CDI.current().select(type).get();
                    }
                })
                .useStorageProvider(SqlStorageProviderFactory.using(
                        dataSource))
                .useBackgroundJobServer(usingStandardBackgroundJobServerConfiguration()
                        .andWorkerCount(20)
                        .andDynamicQueuePolicy(new RoundRobinDynamicQueuePolicy("batch:")))
                .useDashboard(usingStandardDashboardConfiguration()
                        .andDynamicQueueConfiguration("Batch", "batch:"))
                .initialize();

        this.jobScheduler = jobRunrConfigurationResult.getJobScheduler();
        System.out.println("JobRunr BackgroundJobServer and Dashboard initialized.");
    }

    // This method will be called when Open Liberty shuts down your app
    @PreDestroy
    public void onShutdown() {
        if (backgroundJobServer != null) {
            backgroundJobServer.stop();
            System.out.println("JobRunr BackgroundJobServer stopped.");
        }
    }

    // This makes the JobScheduler available for injection in other beans
    @Produces
    @Dependent
    public JobScheduler jobScheduler() {
        if (this.jobScheduler == null) {
            throw new IllegalStateException("JobScheduler is not initialized yet. Ensure JobRunrProInitializer.onStart has completed before injection.");
        }
        return this.jobScheduler;
    }
}