package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * Created by zandr on 06.05.2017.
 */
@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {
    private static final Logger LOG = getLogger(MealServiceTest.class);

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;
    @Autowired
    private DbPopulator populator;

    @Before
    public void setUp() {
        populator.execute();
    }

    @Test
    public void testSave() {
        Meal meal = new Meal(LocalDateTime.now(), "Break", 2100);
        Meal newMeal = service.save(meal, ADMIN_ID);
        meal.setId(newMeal.getId());
        List<Meal> list = new ArrayList<>(adminMeals);
        list.add(0, meal);
        MEAL_MODEL_MATCHER.assertCollectionEquals(list, service.getAll(ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateAnother() {
        service.update(userMeals.get(0), ADMIN_ID);
    }

    @Test
    public void testUpdate() {
        Meal meal = new Meal(userMeals.get(2).getId(), LocalDateTime.now(), "Meal", 1500);
        Meal updatedMeal = service.update(meal, USER_ID);
        MEAL_MODEL_MATCHER.assertEquals(meal, updatedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAnother() {
        service.delete(userMeals.get(1).getId(), ADMIN_ID);
    }


    @Test(expected = NotFoundException.class)
    public void testDeleteNotExist() {
        service.delete(START_SEQ + 10, ADMIN_ID);
    }

    @Test
    public void testDelete() {
        service.delete(adminMeals.get(2).getId(), ADMIN_ID);
        MEAL_MODEL_MATCHER.assertCollectionEquals(adminMeals.subList(0, 2), service.getAll(ADMIN_ID));
    }

    @Test
    public void testGetAll() {
        MEAL_MODEL_MATCHER.assertCollectionEquals(adminMeals, service.getAll(ADMIN_ID));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetAllFromNotExistedUser() {
        service.getAll(ADMIN_ID + 3).get(0);
    }

    @Test(expected = NotFoundException.class)
    public void testGetFromAnotherUser() {
        service.get(userMeals.get(1).getId(), ADMIN_ID);
    }

    @Test
    public void testGet() {
        Meal meal = service.get(adminMeals.get(0).getId(), ADMIN_ID);
        MEAL_MODEL_MATCHER.assertEquals(meal, adminMeals.get(0));
    }

    @Test
    public void testGetBetweenDates() {
        MEAL_MODEL_MATCHER.assertCollectionEquals(userMeals, service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30), LocalDate.of(2015, Month.MAY, 30),
                USER_ID));
        MEAL_MODEL_MATCHER.assertCollectionEquals(Collections.emptyList(), service.getBetweenDates(LocalDate.of(2015, Month.JUNE, 1), LocalDate.now(),
                ADMIN_ID));
    }

    @Test
    public void testGetBetweenDateTime() {
        MEAL_MODEL_MATCHER.assertCollectionEquals(userMeals, service.getBetweenDateTimes(LocalDateTime.of(DateTimeUtil.MIN_DATE, LocalTime.MIN), LocalDateTime.of
                (DateTimeUtil.MAX_DATE, LocalTime.MAX), USER_ID));
        MEAL_MODEL_MATCHER.assertCollectionEquals(Collections.emptyList(), service.getBetweenDateTimes(LocalDateTime.of(DateTimeUtil.MIN_DATE, LocalTime.MIN),
                LocalDateTime.of(DateTimeUtil.MIN_DATE, LocalTime.MIN), ADMIN_ID));
        MEAL_MODEL_MATCHER.assertCollectionEquals(service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 10, 1),
                LocalDateTime.of(2015, Month.MAY, 30, 19, 59), USER_ID), Collections.singletonList(userMeals.get(1)));
        MEAL_MODEL_MATCHER.assertCollectionEquals(userMeals, service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
                LocalDateTime.of(2015, Month.MAY, 30, 20, 0), USER_ID));
    }
}
