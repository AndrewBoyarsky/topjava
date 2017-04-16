package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by zandr on 15.04.2017.
 */
public interface MealDao {
    void add(Meal meal);

    void update(Meal meal);

    void remove(long id);

    Meal getById(long id);

    List<Meal> list();
}
