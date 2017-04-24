package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        return MealsUtil.getWithExceeded(service.getAllFiltered(AuthorizedUser.id(), LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX), AuthorizedUser
                .getCaloriesPerDay());
    }

    public Meal save(Meal meal) {
        return service.save(meal, AuthorizedUser.id());
    }

    public void delete(int id) {
        service.delete(id, AuthorizedUser.id());
    }

    public Meal get(int id) {
        return service.get(id, AuthorizedUser.id());
    }

    public List<MealWithExceed> getFilteredWithExceed(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        fromDate = fromDate == null ? LocalDate.MIN : fromDate;
        toDate = toDate == null ? LocalDate.MAX : toDate;
        fromTime = fromTime == null ? LocalTime.MIN : fromTime;
        toTime = toTime == null ? LocalTime.MAX : toTime;
        return MealsUtil.getWithExceeded(service.getAllFiltered(AuthorizedUser.id(), fromDate, toDate, fromTime, toTime), AuthorizedUser
                .getCaloriesPerDay());
    }

}