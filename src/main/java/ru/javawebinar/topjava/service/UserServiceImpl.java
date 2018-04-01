package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.VerificationTokenRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.UserUtil.prepareToSave;
import static ru.javawebinar.topjava.util.UserUtil.updateFromTo;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        return userRepository.save(prepareToSave(user));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id), id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return checkNotFoundWithId(userRepository.get(id), id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(userRepository.getByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        userRepository.save(prepareToSave(user));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @Override
    public void update(UserTo userTo) {
        User user = updateFromTo(get(userTo.getId()), userTo);
        userRepository.save(prepareToSave(user));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void evictCache() {
        // only for evict cache
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.getByEmail(email.toLowerCase());
        if (u == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(u);
    }

    @Override
    public User getWithMeals(int id) {
        return checkNotFoundWithId(userRepository.getWithMeals(id), id);
    }

    @Override
    public VerificationToken createVerificationToken(String token, int userId) {
        Assert.hasText(token, "token must not be empty");
        return verificationTokenRepository.save(new VerificationToken(token), userId);
    }

    @Override
    public VerificationToken getVerificationToken(String token) throws NotFoundException {
        Assert.hasText(token, "token must not be empty");
        return checkNotFound(verificationTokenRepository.getByToken(token), "token isnt exist");
    }

    @Override
    public User getByVerificationToken(String token) throws NotFoundException {
        Assert.hasText(token, "token must not be empty");
        return getVerificationToken(token).getUser();
    }

    @Override
    public void deleteVerificationToken(int id) {
        checkNotFound(verificationTokenRepository.delete(id), " token is not exist");
    }

    @Override
    public List<VerificationToken> getAllVerificationTokens() {
        return verificationTokenRepository.getAll();
    }

    @Override
    public VerificationToken saveVerificationToken(VerificationToken verificationToken, int userId) {
        Assert.notNull(verificationToken, " verification token must not be null");
        return verificationTokenRepository.save(verificationToken,userId );
    }

    @Override
    public void deleteAllExpiredVerificationTokens() {
        verificationTokenRepository.deleteAllExpired();
    }
}