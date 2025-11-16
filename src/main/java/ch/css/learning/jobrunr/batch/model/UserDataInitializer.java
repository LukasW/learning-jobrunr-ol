package ch.css.learning.jobrunr.batch.model;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@ApplicationScoped
public class UserDataInitializer {
    @Inject
    UserRepository userRepository;

    @Transactional
    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        if (userRepository.count() == 0) {
            IntStream.rangeClosed(1, 100).forEach(i -> {
                User user = new User();
                user.setUsername("user" + i);
                user.setEmail("user" + i + "@example.com");
                user.setPassword("password" + i);
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            });
        }
    }
}

