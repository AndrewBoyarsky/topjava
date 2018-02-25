package ru.javawebinar.topjava;

import org.springframework.context.support.GenericXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

import static ru.javawebinar.topjava.TestUtil.mockAuthorize;
import static ru.javawebinar.topjava.UserTestData.USER;

public class SpringMain {
    public static void main(String[] args) {
        try (GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext()) {
            appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
            appCtx.load("spring/spring-app.xml", "spring/mock.xml");
            appCtx.refresh();

            mockAuthorize(USER);

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email", "password", 1500, Role.ROLE_ADMIN));
        }
    }
}