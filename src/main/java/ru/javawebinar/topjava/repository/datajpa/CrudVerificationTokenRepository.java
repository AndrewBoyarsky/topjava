package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.VerificationToken;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {


    @Override
    VerificationToken save(VerificationToken item);
//    @Query("SELECT v FROM  VerificationToken v WHERE v.token=?1")
    VerificationToken findByToken(String token);

//    VerificationToken findByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from VerificationToken v where v.id=:id ")
    int delete(@Param("id") int id);

    @Override
    List<VerificationToken> findAll();

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationToken v WHERE v.expiryDate < :dateTime")
    int deleteAllByExpiryDateBefore(@Param("dateTime") LocalDateTime dateTime);
    //    @Transactional
//    @Modifying
//    int delete(int id);
}
