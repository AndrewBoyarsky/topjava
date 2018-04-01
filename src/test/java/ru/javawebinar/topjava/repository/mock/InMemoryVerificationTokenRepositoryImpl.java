package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryVerificationTokenRepositoryImpl implements VerificationTokenRepository {
    private static final Logger LOG = getLogger(InMemoryVerificationTokenRepositoryImpl.class);
    private static final Map<Integer, VerificationToken> repository = new ConcurrentHashMap<>();
    private static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public List<VerificationToken> getAll() {
        return repository.values().stream().collect(Collectors.toList());
    }

    @Override
    public int deleteAllExpired() {
        int numberOfAffected = repository.size();
        repository.values().removeIf(verificationToken -> verificationToken.getExpiryDate().isBefore(LocalDateTime.now()));

        return numberOfAffected - repository.size(); // calculate number of affected records in repository as difference between old and current size of repository

    }

    @Override
    public VerificationToken save(VerificationToken t, int userId) {
        LOG.info("save {}", t);
        t.setId(counter.incrementAndGet());
        return repository.put(userId, t);
    }

//    @Override
//    public VerificationToken getByUser(User u) {
//
//        VerificationToken verificationToken = repository.get(u.getId());
//        LOG.info("get token {} for {}", verificationToken, u);
//        return verificationToken;
//    }

//    @Override
//    public boolean deleteByUser(User u) {
//        VerificationToken token = repository.remove(u.getId());
//        LOG.info("delete token {} for user {}", token, u);
//        return token != null;
//    }

    @Override
    public boolean delete(int id) {
        boolean isRemoved = repository.values().removeIf(v -> v.getId() == id);
        LOG.info("token is deleted \"{}\" by id {}", isRemoved, id);
        return isRemoved;
    }

    @Override
    public VerificationToken getByToken(String t) {
        VerificationToken verificationToken = repository.values().stream().filter(v -> v.getToken().equals(t)).findFirst().get();
        LOG.info("get token {}->{}", t, verificationToken);
        return verificationToken;
    }
}
