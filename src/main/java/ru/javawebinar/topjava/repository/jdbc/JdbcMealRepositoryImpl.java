package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final RowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    private final DataSourceTransactionManager transactionManager;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  DataSourceTransactionManager transactionManager) {
        this.insertMeal = new SimpleJdbcInsert(dataSource)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Meal save(Meal meal, int userId) {

//        TransactionDefinition txDef = new DefaultTransactionAttribute(TransactionAttribute.ISOLATION_REPEATABLE_READ);
//        TransactionStatus txSt = transactionManager.getTransaction(txDef);
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", meal.getDateTime())
                .addValue("user_id", userId);

//        try {
            if (meal.isNew()) {
                Number newId = insertMeal.executeAndReturnKey(map);
                meal.setId(newId.intValue());
            } else {
                if (namedParameterJdbcTemplate.update("" +
                                "UPDATE meals " +
                                "   SET description=:description, calories=:calories, date_time=:date_time " +
                                " WHERE id=:id AND user_id=:user_id"
                        , map) == 0) {
//                    transactionManager.rollback(txSt);
                    return null;
                }
            }
//            transactionManager.commit(txSt);
//        }
//        catch (DataAccessException e) {
//            transactionManager.rollback(txSt);
//            throw e;
//        }
        return meal;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public boolean delete(int id, int userId) {

//        TransactionDefinition txDef = new DefaultTransactionAttribute(TransactionAttribute.ISOLATION_REPEATABLE_READ);
//        TransactionStatus txSt = transactionManager.getTransaction(txDef);
//
//        try {
            boolean isOk = jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
//            transactionManager.commit(txSt);
            return isOk;
//        }
//        catch (DataAccessException e) {
//            transactionManager.rollback(txSt);
//            throw e;
//        }
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query(
                "SELECT * FROM meals WHERE id = ? AND user_id = ?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=?  AND date_time BETWEEN  ? AND ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDate, endDate);
    }
}
