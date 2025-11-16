package ch.css.learning.jobrunr.batch;

import ch.css.learning.jobrunr.batch.model.User;
import ch.css.learning.jobrunr.batch.model.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.JobBuilder;
import org.jobrunr.scheduling.JobScheduler;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.jobrunr.scheduling.JobBuilder.aJob;

@ApplicationScoped
public class MailCampaignService {

    @Inject
    UserRepository userRepository;

    @Inject
    SendMailJob sendMailJob;

    @Inject
    JobScheduler jobScheduler;

    public void startMailCampagneBatch(String mailTemplateKey) {
        jobScheduler.startBatch(() -> this.enqueueChildren(mailTemplateKey));
    }

    @Transactional
    @Job(name = "Send E-Mails to all subscribers", retries = 1)
    public void enqueueChildren(String mailTemplateKey) {
        AtomicInteger counter = new AtomicInteger();

        Stream<JobBuilder> jobBuilderStream = userRepository
                .findAll()
                .stream()
                .map(User::getId)
                .peek(userId -> {
                    int count = counter.incrementAndGet();
                    if (count % 1000 == 0) {
                        System.out.println("Processed " + count + " userIds, current: " + userId);
                    }
                })
                .map(id -> aJob()
                        .withId(UUID.randomUUID())
                        .withAmountOfRetries(0)
                        .withName("Send mail to user " + id)
                        .withLabels("batch:" + "blubb")
                        .withDetails(() -> sendMailJob.send(id, mailTemplateKey, JobContext.Null))
                );

        jobScheduler.create(jobBuilderStream);

        System.out.println("Enqueued emails to all subscribers with template: " + mailTemplateKey);
    }
}
