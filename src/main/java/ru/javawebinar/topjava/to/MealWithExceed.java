package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MealWithExceed {
    private final Integer id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private boolean exceed;
    @JsonCreator
    public MealWithExceed(@JsonProperty("id") Integer id,
                          @JsonProperty("dateTime") LocalDateTime dateTime,
                          @JsonProperty("description") String description,
                          @JsonProperty("calories") int calories,
                          @JsonProperty("exceed") boolean exceed) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExceed() {
        return exceed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealWithExceed)) return false;

        MealWithExceed that = (MealWithExceed) o;

        if (getCalories() != that.getCalories()) return false;
        if (isExceed() != that.isExceed()) return false;
        if (!getId().equals(that.getId())) return false;
        if (!getDateTime().equals(that.getDateTime())) return false;
        return getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getDateTime().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getCalories();
        result = 31 * result + (isExceed() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MealWithExceed{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", exceed=" + exceed +
                '}';
    }
}
