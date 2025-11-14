package ch.css.learning.jobrunr.lib;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Singleton;
import org.jobrunr.dashboard.JobRunrDashboardWebServer;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.jobs.queues.Queues;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.BackgroundJobServer;
import org.jobrunr.server.JobActivator;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider;
import org.jobrunr.utils.mapper.JsonMapper;
import org.jobrunr.utils.mapper.jsonb.JsonbJsonMapper;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class JobRunrProvider {

    @Produces
    @Singleton
    public JobRunrDashboardWebServer dashboardWebServer(StorageProvider storageProvider, JsonMapper jsonMapper) {
        return new JobRunrDashboardWebServer(storageProvider, jsonMapper, new Queues());
    }

    @Produces
    @Singleton
    public BackgroundJobServer backgroundJobServer(StorageProvider storageProvider, JobActivator jobActivator, JsonMapper jsonMapper) {
        return new BackgroundJobServer(storageProvider, jsonMapper, jobActivator);
    }

    @Produces
    @Singleton
    public JobActivator jobActivator() {
        return new JobActivator() {
            @Override
            public <T> T activateJob(Class<T> aClass) {
                return CDI.current().select(aClass).get();
            }
        };
    }

    @Produces
    @Singleton
    public JobScheduler jobScheduler(StorageProvider storageProvider) {
        final JobScheduler jobScheduler = new JobScheduler(storageProvider);
        BackgroundJob.setJobScheduler(jobScheduler);
        return jobScheduler;
    }

    @Produces
    @Singleton
    public StorageProvider storageProvider(DataSource dataSource, JobMapper jobMapper) {
        final PostgresStorageProvider postgresStorageProvider = new PostgresStorageProvider(dataSource);
        postgresStorageProvider.setJobMapper(jobMapper);
        return postgresStorageProvider;
    }

    @Produces
    @Singleton
    public JobMapper jobMapper(JsonMapper jsonMapper) {
        return new JobMapper(jsonMapper);
    }

    @Produces
    @Singleton
    public JsonMapper jsonMapper() {
        return new JsonbJsonMapper();
    }

    @Produces
    @Singleton
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"localhost"});
        dataSource.setPortNumbers(new int[]{5432});
        dataSource.setDatabaseName("postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("your_strong_password");
        return dataSource;
    }
}
