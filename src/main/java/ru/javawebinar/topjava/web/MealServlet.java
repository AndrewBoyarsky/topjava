package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;
    private ConfigurableApplicationContext applicationContext;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = applicationContext.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        applicationContext.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //filtering
        String userId = request.getParameter("user");
        if (userId != null) {
            AuthorizedUser.setId(Integer.parseInt(userId));
            response.sendRedirect("meals");
        } else if (request.getParameter("filterByTime") != null) {
            LocalTime fromTime = LocalTime.MIN;
            try {
                fromTime = LocalTime.parse(request.getParameter("fromTime"), DateTimeUtil.TIME_FORMATTER);
            }
            catch (Exception e) {
                LOG.error("Error during parsing fromTime ", e);
            }
            LocalTime toTime = LocalTime.MAX;
            try {
                toTime = LocalTime.parse(request.getParameter("toTime"), DateTimeUtil.TIME_FORMATTER);
            }
            catch (Exception e) {
                LOG.error("Error during parsing toTime", e);
            }
            LOG.info("Filtering by time from {} to {}", fromTime, toTime);
            List<MealWithExceed> filteredMeal = mealRestController.getFilteredWithExceed(LocalDate.MIN, LocalDate.MAX, fromTime, toTime);
            request.setAttribute("meals", filteredMeal);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (request.getParameter("filterByDate") != null) {
            LocalDate fromDate = LocalDate.MIN;
            try {
                fromDate = LocalDate.parse(request.getParameter("fromDate"), DateTimeUtil.DATE_FORMATTER);
            }
            catch (Exception e) {
                LOG.error("Error during parse fromDate ", e);
            }
            LocalDate toDate = LocalDate.MAX;
            try {
                toDate = LocalDate.parse(request.getParameter("toDate"), DateTimeUtil.DATE_FORMATTER);
            }
            catch (Exception e) {
                LOG.error("Error during parse toDate ", e);
            }
            LOG.info("Filtering by date from {} to {}", fromDate, toDate);
            List<MealWithExceed> filteredMeal = mealRestController.getFilteredWithExceed(fromDate, toDate, LocalTime.MIN, LocalTime.MAX);
            request.setAttribute("meals", filteredMeal);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            String id = request.getParameter("id");
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            mealRestController.save(meal);
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {

            case "delete":
                int id = getId(request);
                LOG.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = action.equals("create") ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "all":
            default:
                LOG.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}