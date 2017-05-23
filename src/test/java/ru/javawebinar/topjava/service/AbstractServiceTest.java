package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.Profiles;

import java.util.concurrent.TimeUnit;

@Sql(scripts = {
        "classpath:db/initDB_" + Profiles.ACTIVE_DB + ".sql",
        "classpath:db/populateDB.sql"
}, config = @SqlConfig(encoding = "UTF-8"))
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-app.xml"
})
@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.REPOSITORY_IMPLEMENTATION})
public abstract class AbstractServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceTest.class);
    private static StringBuilder results = new StringBuilder();

    static {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            LOG.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        LOG.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------\n" +
                results +
                "---------------------------------\n");
    }

    @Test
    public abstract void testGet() throws Exception;

    @Test
    public abstract void testSave() throws Exception;

    @Test
    public abstract void testUpdate() throws Exception;

    @Test
    public abstract void testDelete() throws Exception;

    @Test
    public abstract void testGetAll() throws Exception;
}
