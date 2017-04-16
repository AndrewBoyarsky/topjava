package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDaoMemoryImpl implements MealDao {
    private static final Logger LOG = getLogger(MealDaoMemoryImpl.class);

    static {
        SingletonStorage.counter.getAndSet(6);
        getMemoryStorage().add(new Meal(1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        getMemoryStorage().add(new Meal(2, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        getMemoryStorage().add(new Meal(3, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        getMemoryStorage().add(new Meal(4, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        getMemoryStorage().add(new Meal(5, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        getMemoryStorage().add(new Meal(6, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        LOG.debug("Initial collection for test: " + getMemoryStorage());
    }

    private MealDaoMemoryImpl() {}

    private static List<Meal> getMemoryStorage() {
        return SingletonStorage.MEALS;
    }

    public static MealDaoMemoryImpl getInstance() {
        return SingletonStorage.INSTANCE;
    }

    @Override
    public synchronized void add(Meal meal) {
        Meal mealForPersisting = (meal.getId() == 0L) ? new Meal(SingletonStorage.counter
                .incrementAndGet(), meal) : meal;
        getMemoryStorage().add(mealForPersisting);
        LOG.info("Successfully added " + mealForPersisting);
    }

    @Override
    public synchronized void update(Meal meal) {
        remove(meal.getId());
        add(meal);
        LOG.info("Updating item: " + meal);
    }

    @Override
    public synchronized void remove(long id) {
        Meal mealForRemove = getById(id);
        SingletonStorage.MEALS.remove(mealForRemove);
        LOG.info("Removing item: " + mealForRemove);
    }

    @Override
    public synchronized Meal getById(long id) {
        Meal meal = getMemoryStorage().stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .get();
        LOG.info("Was found by id " + id + "  " + meal);
        return meal;
    }

    @Override
    public List<Meal> list() {
        LOG.info("Meal list: " + getMemoryStorage());
        return getMemoryStorage();
    }

    private static class SingletonStorage {
        private static final List<Meal> MEALS = new ArrayList<>();
        private static final MealDaoMemoryImpl INSTANCE = new MealDaoMemoryImpl();
        private static AtomicInteger counter = new AtomicInteger();
    }
}
