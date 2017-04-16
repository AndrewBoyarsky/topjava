package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionParameter = req.getParameter("action");
        req.setCharacterEncoding("UTF-8");
        assignAttributes(req);
        req.setAttribute("meal", new Meal());
        if (actionParameter != null) {
            String action = req.getParameter("action");
            if ("delete".equalsIgnoreCase(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                MealDaoMemoryImpl.getInstance().remove(id);
                resp.sendRedirect(req.getContextPath() + "/meals");
            } else if ("edit".equalsIgnoreCase(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("meal", MealDaoMemoryImpl.getInstance().getById(id));
                req.getRequestDispatcher("/meal.jsp").forward(req, resp);
            }
        } else {
            req.getRequestDispatcher("/meal.jsp").forward(req, resp);

        }
    }

    private void assignAttributes(ServletRequest req) throws ServletException, IOException {
        List<MealWithExceed> mealWithExceeds = MealsUtil.getFilteredWithExceeded
                (MealDaoMemoryImpl.getInstance().list(),
                        LocalTime.MIN, LocalTime.MAX, 2000);
        LOG.debug("List of meal with exceed: " + mealWithExceeds);
        req.setAttribute("mealList", mealWithExceeds);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idParameter = req.getParameter("id");
        if (idParameter != null) {
            Meal meal = new Meal(Long.parseLong(idParameter), LocalDateTime.parse(req
                    .getParameter("dateTime").replace('T', ' '), dateTimeFormatter),
                    req.getParameter("description"),
                    Integer.parseInt(req.getParameter("calories")));
            MealDaoMemoryImpl.getInstance().update(meal);
        } else {
            Meal meal = new Meal(LocalDateTime.parse(req.getParameter("dateTime").replace('T', ' '), dateTimeFormatter),
                    req.getParameter("description"),
                    Integer.parseInt(req.getParameter("calories")));
            MealDaoMemoryImpl.getInstance().add(meal);
        }
        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
