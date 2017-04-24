package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.stream().limit(3).forEach(meal -> save(meal, 1));
        MealsUtil.MEALS.stream().skip(3).forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else {
            Meal userMeal = get(meal.getId(), userId);
            if (userMeal == null) return null;
        }
        meal.setUserId(userId);
        repository.put(meal.getId(), meal);
        return meal;
    }
    @Override
    public List<Meal> getAllFiltered(int userId, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId().equals(userId) && DateTimeUtil.isBetweenDateTime(meal.getDate(), fromDate, toDate) &&
                DateTimeUtil.isBetweenDateTime(meal.getTime(), fromTime, toTime))
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = get(id, userId);
        return (meal != null) && (repository.remove(id) != null);

    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        Meal userMeal = getAllFiltered(userId, LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX).contains(meal) ? meal : null;
        return ValidationUtil.checkUserHasMeal(userId, userMeal) ? userMeal : null;
    }



}

