package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by zandr on 02.06.2017.
 */
public class MealRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MealService service;

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/rest/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getWithExceeded(MEALS, AuthorizedUser.getCaloriesPerDay())));
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/rest/meals/" + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    //
    @Test
    public void testCreateWithLocation() throws Exception {
        Meal meal = new Meal(LocalDateTime.now(), "Ужин", 300);
        ResultActions action = mockMvc.perform(post("/rest/meals")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(meal)))
                .andExpect(status().is(201));
        Meal returned = MATCHER.fromJsonAction(action);
        meal.setId(returned.getId());
        MATCHER.assertEquals(meal, returned);
        List<Meal> meals = new ArrayList<>();
        meals.add(meal);
        meals.addAll(MEALS);
        MATCHER.assertCollectionEquals(meals, service.getAll(AuthorizedUser.id()));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal meal = new Meal(MEAL1_ID, MEAL1.getDateTime(), MEAL1.getDescription(), MEAL1.getCalories());
        meal.setCalories(1023);
        meal.setDescription("Breakfast");
        mockMvc.perform(put("/rest/meals/" + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(meal)))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertEquals(meal, service.get(MEAL1_ID, AuthorizedUser.id()));
    }

    @Test
    public void testGetBetween() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2015, Month.MAY, 30, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, Month.MAY, 31, 18, 0);
        mockMvc.perform(post("/rest/meals/filter")
                .param("startDateTime", startDateTime.toString())
                .param("endDateTime", endDateTime.toString()))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil
                        .getFilteredWithExceeded(MEALS, startDateTime.toLocalTime(), endDateTime.toLocalTime(), AuthorizedUser.getCaloriesPerDay())
                        .stream()
                        .filter(m -> DateTimeUtil.isBetween(m.getDateTime().toLocalDate(), startDateTime.toLocalDate(), endDateTime.toLocalDate()))
                        .collect(Collectors.toList())));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/rest/meals/" + MEAL5.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL4, MEAL3, MEAL2, MEAL1), service.getAll(AuthorizedUser.id()));
    }

}