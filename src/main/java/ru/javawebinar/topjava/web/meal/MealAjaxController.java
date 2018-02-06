package ru.javawebinar.topjava.web.meal;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(path = MealAjaxController.MEAL_AJAX_PATH)
public class MealAjaxController extends AbstractMealController {
    static final String MEAL_AJAX_PATH = "/ajax/meals";

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }


    @PostMapping
    public void createOrUpdate(
            @RequestParam("id") Integer id,
            @RequestParam("description") String description,
            @RequestParam("calories") Integer calories,
            @RequestParam("dateTime") String dateTime
    ) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        if (id == null) {
            create(meal);
        } else {
            update(meal, id);
        }
    }

    @Override
    @PostMapping("/filter")
    public List<MealWithExceed> getBetween(
            @RequestParam(value = "startDate",required = false) LocalDate startDate,                    @RequestParam(value = "startTime", required = false) LocalTime startTime,                   @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "endTime", required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
