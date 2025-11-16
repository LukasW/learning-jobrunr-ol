package ch.css.learning.jobrunr.batch.model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class MailSendingRepository {

    @PersistenceContext(unitName = "jpa-unit")
    private EntityManager em;

    public MailSending findById(Long id) {
        return em.find(MailSending.class, id);
    }

    public void save(MailSending mailSending) {
        if (mailSending.getId() == null) {
            em.persist(mailSending);
        } else {
            em.merge(mailSending);
        }
    }

    public void delete(Long id) {
        MailSending entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public List<MailSending> findAll() {
        return em.createQuery("SELECT m FROM MailSending m", MailSending.class).getResultList();
    }
}

