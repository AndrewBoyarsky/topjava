package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> filteredMealWithExceedList = new ArrayList<>();
        List<LocalDate> uniqueDateList = mealList.stream().map(userMeal -> userMeal.getDateTime().toLocalDate()).distinct().collect(Collectors.toList());
        uniqueDateList.forEach(date -> {
            List<UserMeal> mealsForCurrentDate = mealList.stream().filter(p -> date.isEqual(p.getDateTime().toLocalDate())).collect(Collectors.toList());
            int currentDateCalories = mealsForCurrentDate.stream().mapToInt(UserMeal::getCalories).sum();
            mealsForCurrentDate.stream().
                    filter(p -> p.getDateTime().toLocalTime().isBefore(endTime) && p.getDateTime().toLocalTime().
                            isAfter(startTime)).forEach(meal -> {
                if (caloriesPerDay < currentDateCalories) {
                    filteredMealWithExceedList.add(new UserMealWithExceed(meal, true));
                } else {
                    filteredMealWithExceedList.add(new UserMealWithExceed(meal, false));
                }
            });
        });
        return filteredMealWithExceedList;
    }
}
