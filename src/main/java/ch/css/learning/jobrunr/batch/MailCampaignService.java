package ch.css.learning.jobrunr.batch;

import ch.css.learning.jobrunr.batch.model.MailSending;
import ch.css.learning.jobrunr.batch.model.MailSendingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MailCampaignService {

    @Inject
    MailSendingRepository mailSendingRepository;

    @Transactional
    public void startEmailsToAllSubscribersBatch(String mailTemplateKey) {
        mailSendingRepository.save(MailSending.builder().address("asdad").build());
    }
}
