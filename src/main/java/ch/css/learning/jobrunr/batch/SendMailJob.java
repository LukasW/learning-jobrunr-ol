package ch.css.learning.jobrunr.batch;

import ch.css.learning.jobrunr.batch.model.MailSending;
import ch.css.learning.jobrunr.batch.model.MailSendingRepository;
import ch.css.learning.jobrunr.batch.model.User;
import ch.css.learning.jobrunr.batch.model.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jobrunr.jobs.context.JobContext;

import java.io.IOException;
import java.util.Random;

@ApplicationScoped
public class SendMailJob {

    private static final Random rnd = new Random();

    @Inject
    private MailSendingRepository mailSendingRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public void send(Long userId, String mailTemplateKey, JobContext jobContext) throws IOException {
        System.out.println("[" + jobContext.getJobId() + "] Sending email to " + userId + " (" + mailTemplateKey + ")");
        User user = userRepository.findById(userId);
        MailSending mailSending = new MailSending();
        mailSending.setUser(user);
        mailSending.setAddress(user.getEmail());
        mailSending.setMailTemplateKey(mailTemplateKey);
        mailSendingRepository.save(mailSending);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (rnd.nextDouble() < 0.05) {
            throw new IOException("Simulated email sending failure");
        }
    }
}
