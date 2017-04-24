package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
//        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
//        System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

//        UserRepository userRepository = (UserRepository) appCtx.getBean("mockUserRepository");
//        UserRepository userRepository = appCtx.getBean(UserRepository.class);
//        userRepository.getAll();
//
//        UserService userService = appCtx.getBean(UserService.class);
//        userService.save(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
//
//        appCtx.close();
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx2 = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx2.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx2.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
            MealRestController mealController = appCtx2.getBean(MealRestController.class);
            mealController.save(new Meal(LocalDateTime.now(), "meal", 1000));
            System.out.println(mealController.getAll());
        }
    }
}