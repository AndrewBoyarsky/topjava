package ru.javawebinar.topjava.service;


import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

public interface UserService {
    User save(User user);

    void delete(int id) throws NotFoundException;

    User get(int id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    void update(UserTo user);

    List<User> getAll();

    void update(User user);

    void evictCache();

    void enable(int id, boolean enable);

    User getWithMeals(int id);

    default VerificationToken createVerificationToken(String token, int userId) {
        return saveVerificationToken(new VerificationToken(token), userId);
    }

    VerificationToken getVerificationToken(String token) throws NotFoundException;

    User getByVerificationToken(String token) throws NotFoundException;

    void deleteVerificationToken(int id) throws NotFoundException;

    @Transactional
    default User saveAndDeleteVerificationToken(User user, int tokenId) {
        deleteVerificationToken(tokenId);
        return save(user);
    }

    List<VerificationToken> getAllVerificationTokens();

    VerificationToken saveVerificationToken(VerificationToken verificationToken, int userId);

    void deleteAllExpiredVerificationTokens();
//    User registerUser(User user);
}