package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class DataJpaMealRepositoryImpl implements MealRepository {

    @Autowired
    private CrudMealRepository mealRepository;
    @Autowired
    private CrudUserRepository userRepository;
    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        Objects.requireNonNull(meal);
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
            User user = userRepository.getOne(userId);
            meal.setUser(user);
            return mealRepository.save(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return mealRepository.deleteByIdAndUser_Id(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepository.findByIdAndUser_Id(id, userId);
    }
    @Override
    public Meal getWithUser(int id, int userId) {
        return mealRepository.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAllByUser_IdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        return mealRepository.findAllByDateTimeBetweenAndUser_IdOrderByDateTimeDesc(startDate, endDate, userId);
    }
}
