package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.repository.VerificationTokenRepository;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Lazy(value = false) //beans are lazy by default
public class Scheduler {
        private static final Logger LOG = getLogger(Scheduler.class);

    private VerificationTokenRepository repository;

    @Autowired
    public Scheduler(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 4 1 * ?")
    public void deleteExpiredTokens() {
        int numberOfDeleted = repository.deleteAllExpired();
        LOG.info("Expired tokens were deleted - {}", numberOfDeleted);
    }

}
