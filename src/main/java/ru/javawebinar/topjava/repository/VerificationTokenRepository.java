package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.VerificationToken;

import java.util.List;

public interface VerificationTokenRepository {
    VerificationToken save(VerificationToken t, int userId);

//    VerificationToken getByUser(User u);

    boolean delete(int id);

    VerificationToken getByToken(String t);

    List<VerificationToken> getAll();

    int deleteAllExpired();
}
