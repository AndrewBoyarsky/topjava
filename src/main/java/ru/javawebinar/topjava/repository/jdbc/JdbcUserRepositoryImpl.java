package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ResultSetExtractor<List<User>> extractor = (ResultSetExtractor<List<User>>) rs -> {
        Map<Integer, User> map = new HashMap<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setEmail(rs.getString("email"));
            user.setRegistered(rs.getDate("registered"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            if (map.containsKey(user.getId())) {
                User user2 = map.get(user.getId());
                user2.getRoles().add(Role.ROLE_ADMIN.toString().equals(rs.getString("role")) ? Role.ROLE_ADMIN : Role.ROLE_USER);
            } else {
                map.put(user.getId(), user);
                user.setRoles(EnumSet.of(Role.ROLE_ADMIN.toString().equals(rs.getString("role")) ? Role.ROLE_ADMIN : Role.ROLE_USER));
            }
        }
        return new ArrayList<>(map.values());
    };

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Role> roles = new ArrayList<Role>(user.getRoles());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (" + user.getId() + ", ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, roles.get(i).toString());
                }

                @Override
                public int getBatchSize() {
                    return roles.size();
                }
            });
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
//        TransactionStatus txSt = transactionManager.getTransaction(new DefaultTransactionAttribute());
//        try {
        boolean isOk = jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
//            transactionManager.commit(txSt);
        return isOk;
//        }
//        catch (DataAccessException e) {
//            transactionManager.rollback(txSt);
//            throw e;
//        }
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles ur ON ur.user_id = ? WHERE id=?",
                extractor, id, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles ur ON users.id = ur.user_id WHERE email=?",
                extractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles ON users.id = user_roles.user_id " +
                        "ORDER BY name, email",
                extractor);
        users.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        return users;
    }
}
