package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.repository.VerificationTokenRepository;

import java.util.List;
//todo implementation
@Repository
@Transactional(readOnly = true)
public class VerificationTokenRepositoryImpl implements VerificationTokenRepository {

    @Override
    public VerificationToken save(VerificationToken t, int userId) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public VerificationToken getByToken(String t) {
        return null;
    }

    @Override
    public List<VerificationToken> getAll() {
        return null;
    }

    @Override
    public int deleteAllExpired() {
        return 0;
    }
}

