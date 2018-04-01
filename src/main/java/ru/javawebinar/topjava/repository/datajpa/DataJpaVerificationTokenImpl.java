package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaVerificationTokenImpl implements VerificationTokenRepository {

    @Autowired
    private CrudUserRepository userRepository;
    @Autowired
    private CrudVerificationTokenRepository verificationTokenRepository;


    @Transactional
    @Modifying
    @Override
    public VerificationToken save(VerificationToken verificationToken, int userId) {
        User user = userRepository.getOne(userId);
        verificationToken.setUser(user);
        return verificationTokenRepository.save(verificationToken);
    }

//    @Override
//    public VerificationToken getByUser(User u) {
//        return verificationTokenRepository.findByUser(u);
//    }

//    @Override
//    public boolean deleteByUser(User u) {
//        return verificationTokenRepository.deleteByUser(u) != 0;
//    }

    @Override
    public boolean delete(int id) {
        return verificationTokenRepository.delete(id) != 0;
    }

    @Override
    public VerificationToken getByToken(String t) {
        return verificationTokenRepository.findByToken(t);
    }

    @Override
    public List<VerificationToken> getAll() {
        return verificationTokenRepository.findAll();
    }

    @Override
    public int deleteAllExpired() {
        return verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}
