package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {
    private final Logger LOG = getLogger(JdbcMealRepositoryImpl.class);

    private SimpleJdbcInsert jdbcInsert;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private RowMapper ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Meal meal = new Meal();
        meal.setId(rs.getInt("id"));
        meal.setDescription(rs.getString("description"));
        meal.setCalories(rs.getInt("calories"));
        meal.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        return meal;
    };

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Objects.requireNonNull(meal);
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", meal.getDateTime())
                .addValue("user_id", userId);
        if (meal.isNew()) {
            Number id = jdbcInsert.executeAndReturnKey(parameters);
            meal.setId(id.intValue());
            LOG.info("Successfully saved new meal with id = {} for user with id = {}", meal.getId(), userId);
        } else {
            Meal checkedMeal = get(meal.getId(), userId);
            if (checkedMeal == null) {
                LOG.warn("Unauthorized access from user {} to meal {} was rejected.", userId, meal.getId());
                return null;
            }
            namedParameterJdbcTemplate.update("UPDATE meals SET description=:description, calories=:calories, date_time=:date_time WHERE id=:id " +
                            "AND user_id=:user_id",
                    parameters);
            LOG.info("Updating meal with id = {} for user with id = {} ", meal.getId(), userId);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id = ? AND user_id = ?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id = ? AND user_id = ?", ROW_MAPPER, id, userId);
        return singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("startDate", Timestamp.valueOf(startDate))
                .addValue("endDate", Timestamp.valueOf(endDate))
                .addValue("userId", userId);
        return namedParameterJdbcTemplate.query("SELECT * FROM meals WHERE (date_time BETWEEN :startDate AND :endDate) AND user_id = :userId ORDER BY date_time " +
                        "DESC",
                parameters,
                ROW_MAPPER
                );
    }
}
