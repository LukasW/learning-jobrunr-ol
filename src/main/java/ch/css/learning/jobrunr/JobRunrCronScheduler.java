package ch.css.learning.jobrunr;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jobrunr.scheduling.JobScheduler;

@ApplicationScoped
public class JobRunrCronScheduler {
    @Inject
    JobScheduler jobScheduler;

    @Inject
    ExampleJobService exampleJobService;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        System.out.println("Scheduling Cron Job");
        //jobScheduler.scheduleRecurrently(Cron.every15seconds(), () -> exampleJobService.runJob());
    }
}
