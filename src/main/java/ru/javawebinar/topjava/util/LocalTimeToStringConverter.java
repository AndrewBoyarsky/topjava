package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

/**
 * Created by zandr on 31.05.2017.
 */
public class LocalTimeToStringConverter implements Converter<LocalTime, String> {
    @Override
    public String convert(LocalTime source) {
        return null;
    }
}
