package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MealTestData {

    public static final List<Meal> userMeals = MealsUtil.MEALS.stream().limit(3).collect(Collectors.toList());
    public static final List<Meal> adminMeals = MealsUtil.MEALS.stream().skip(3).collect(Collectors.toList());
    public static final ModelMatcher<Meal> MEAL_MODEL_MATCHER = new ModelMatcher<>((expected, actual) -> {
        if (expected == actual) return true;
        return (Objects.equals(expected.getId(), actual.getId())
                && Objects.equals(expected.getCalories(), actual.getCalories())
                && Objects.equals(expected.getDateTime(), actual.getDateTime())
                && Objects.equals(expected.getDescription(), actual.getDescription()));
    });
}
