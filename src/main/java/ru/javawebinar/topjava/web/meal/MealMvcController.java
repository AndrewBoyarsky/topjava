package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping(value = "/meals")
public class MealMvcController extends MealRestController {
    @Autowired
    public MealMvcController(MealService service) {
        super(service);
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable("id") int id) {
        delete(id);
        return "redirect:/meals";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String retrieveAll(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createNew(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 500));
        return "meal";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable(name = "id") int id, Model model) {
        model.addAttribute("meal", get(id));
        return "meal";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(@RequestParam(value = "id", defaultValue = "0") int id,
                       @RequestParam("dateTime") String dateTime,
                       @RequestParam("description") String description,
                       @RequestParam("calories") int calories
                       ) {
        if (id == 0) {
            create(new Meal(LocalDateTime.parse(dateTime), description, calories));
        } else update(new Meal(id, LocalDateTime.parse(dateTime), description, calories), id);
        return "redirect:/meals";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String filterMeals(@RequestParam(name = "startDate") LocalDate startDate,
                              @RequestParam(name = "endDate") LocalDate endDate,
                              @RequestParam(name = "startTime")LocalTime startTime,
                              @RequestParam(name = "endTime") LocalTime endTime,
                              Model model) {
        model.addAttribute("meals",getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
