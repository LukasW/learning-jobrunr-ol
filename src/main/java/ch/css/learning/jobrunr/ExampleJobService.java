package ch.css.learning.jobrunr;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExampleJobService {
    public void runJob() {
        System.out.println("JobRunr Cron-Job wurde ausgeführt: " + java.time.LocalDateTime.now());
        // Hier kann die eigentliche Logik des Jobs implementiert werden
    }
}

