package ru.javawebinar.topjava.web;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.service.UserService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.TestUtil.userAuth;
import static ru.javawebinar.topjava.UserTestData.*;

//todo create tests for resend
public class RootControllerTest extends AbstractControllerTest {
    @Autowired
    UserService userService;

    @Test
    public void testUsers() throws Exception {
        mockMvc.perform(get("/users")
                .with(userAuth(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    public void testUnAuth() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testMeals() throws Exception {
        mockMvc.perform(get("/meals")
                .with(userAuth(USER)))
                .andDo(print())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"));
    }

    @Test
    @Sql("classpath:db/populateDB.sql")
    public void testRegister() throws Exception {
        User user = new User(USER_ID + 10, "newUser", "exampleuser@info.com", "passwoed", 2000, Role.ROLE_USER);
        mockMvc.perform(post("/register")
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .param("caloriesPerDay", String.valueOf(user.getCaloriesPerDay()))
                .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login?message=app.registered"))
        ;
        MATCHER.assertCollectionEquals(Arrays.asList(USER, ADMIN, user), userService.getAll().stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList()));
        Assert.assertTrue(userService.getAllVerificationTokens().stream().filter(vt -> vt.getUser().getId().equals(user.getId())).collect(Collectors.toList()).size() == 1);
    }

    @Test
    @Sql("classpath:db/populateDB.sql")
    public void testEmailConfirmation() throws Exception {
        User user = new User(null, "newUser", "exampleuser@info.com", "password", 2000, Role.ROLE_USER);
        User savedUser = userService.save(user);
        String tokenString = "random token";
        VerificationToken token = userService.createVerificationToken(tokenString, savedUser.getId());
        mockMvc.perform(get("/confirmEmail")
                .param("token", tokenString))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login?message=user.email.confirmed"))
        ;
        User confirmedUser = userService.get(savedUser.getId());
        Assert.assertTrue(confirmedUser.isEmailConfirmed());
        Optional<VerificationToken> deletedToken = userService.getAllVerificationTokens().stream().filter(vt -> vt.getUser().getId().equals(confirmedUser.getId())).findFirst();
        Assert.assertTrue(!deletedToken.isPresent());
    }

}