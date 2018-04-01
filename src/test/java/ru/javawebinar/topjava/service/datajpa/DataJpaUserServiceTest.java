package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.service.AbstractJpaUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.VerificationTokenTestData.*;

@ActiveProfiles(DATAJPA)
@Sql(scripts = {"classpath:db/populateDB.sql", "classpath:populateVerificationTokens.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class DataJpaUserServiceTest extends AbstractJpaUserServiceTest {
    @Test
    public void testGetWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        MATCHER.assertEquals(USER, user);
        MealTestData.MATCHER.assertCollectionEquals(MealTestData.MEALS, user.getMeals());
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithMealsNotFound() throws Exception {
        service.getWithMeals(1);
    }

    @Test
    public void testCreateVerificationToken() {
        User newUser = new User(USER);
        newUser.setId(null);
        newUser.setEmail("random@example.com");
        User savedUser = service.save(newUser);
        VerificationToken token = service.createVerificationToken("NEW_TOKEN", savedUser.getId());
        VERIFICATION_TOKEN_MATHER.assertEquals(Arrays.asList(ADMIN_VERIFICATION_TOKEN, USER_VERIFICATION_TOKEN, token), service.getAllVerificationTokens());
        service.delete(savedUser.getId());
    }

//    @Test
//    @Sql(scripts = "classpath:populateVerificationTokens.sql", config = @SqlConfig(encoding = "UTF-8"))
//    public void testFindByToken() {
//        service.createVerificationToken(ADMIN_TOKEN_STRING, ADMIN_ID);
//        MATCHER.assertEquals(ADMIN, service.getByVerificationToken(ADMIN_TOKEN_STRING));
//    }

    @Test
    public void testFindToken() {
        VERIFICATION_TOKEN_MATHER.assertEquals(ADMIN_VERIFICATION_TOKEN, service.getVerificationToken(ADMIN_TOKEN_STRING));
    }

    @Test
    public void testDeleteToken() {
        service.deleteVerificationToken(ADMIN_TOKEN_ID);
        VERIFICATION_TOKEN_MATHER.assertCollectionEquals(Arrays.asList(USER_VERIFICATION_TOKEN), service.getAllVerificationTokens());
    }

    @Test
    public void testGetAllTokens() {
        VERIFICATION_TOKEN_MATHER.assertCollectionEquals(Arrays.asList(ADMIN_VERIFICATION_TOKEN, USER_VERIFICATION_TOKEN), service.getAllVerificationTokens());
    }

    @Test
    @Sql(scripts = "classpath:populateExpiredVerificationTokens.sql")
    public void testDeleteAllExpiredTokens() {
        service.deleteAllExpiredVerificationTokens();
        Assert.assertTrue(service.getAllVerificationTokens().isEmpty());
    }

    @Test
    public void testSaveVerificationToken() {
        User newUser = new User(USER);
        newUser.setId(null);
        newUser.setEmail("random@example.com");
        User savedUser = service.save(newUser);
        VerificationToken token = service.saveVerificationToken(new VerificationToken("token"), savedUser.getId());
        VERIFICATION_TOKEN_MATHER.assertCollectionEquals(Arrays.asList(ADMIN_VERIFICATION_TOKEN, USER_VERIFICATION_TOKEN, token), service.getAllVerificationTokens());
    }

    @Test
    @Sql(scripts = "classpath:populateExpiredVerificationTokens.sql")
    public void testVerificationTokenExpired() {
        User user = new User(null, "newUser", "exampleuser@info.com", "password", 2000, Role.ROLE_USER);
        User savedUser = service.save(user);
        String tokenString = "random token";
        VerificationToken token = service.createVerificationToken(tokenString, savedUser.getId());
    }
}