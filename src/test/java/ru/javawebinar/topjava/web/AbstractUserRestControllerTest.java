package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.service.UserService;
public class AbstractUserRestControllerTest extends AbstractControllerTest {
    @Override
    public void setUp() {
        super.setUp();
        userService.evictCache();

    }
    @Autowired
    protected UserService userService;

}
