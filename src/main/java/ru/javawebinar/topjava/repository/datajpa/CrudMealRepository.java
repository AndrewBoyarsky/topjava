package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    int deleteByIdAndUser_Id(int id, int userId);

    Meal findByIdAndUser_Id(int id, int userId);

    List<Meal> findAllByUser_IdOrderByDateTimeDesc(int userId);

    @Override
    Meal save(Meal meal);

    List<Meal> findAllByDateTimeBetweenAndUser_IdOrderByDateTimeDesc(LocalDateTime startTime, LocalDateTime endTime, int userId);

    @Query(value = "SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:user_id")
    Meal get(@Param("id") int id, @Param("user_id") int userId);

}
