package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * GKislin
 * 11.01.2015.
 */
@NamedQueries({@NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m where m.id =:id AND m.user.id = :user_id"),
                @NamedQuery(name = Meal.GET, query = "SELECT m FROM Meal m where m.id =:id AND m.user.id = :user_id"),
                @NamedQuery(name = Meal.GET_ALL, query = "SELECT m FROM Meal m where m.user.id = :user_id  ORDER BY m.dateTime DESC"),
                @NamedQuery(name = Meal.BETWEEN_DATE_TIME, query = "SELECT m FROM Meal m where m.user.id =:user_id AND m.dateTime BETWEEN " +
                        ":start_date AND " +
                        ":end_date ORDER BY m.dateTime DESC"),

})
@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"date_time", "user_id"}, name = "unique_date_time_user_idx")})
public class Meal extends BaseEntity {
    public static final String DELETE = "Meal.delete";
    public static final String GET = "Meal.get";
    public static final String GET_ALL = "Meal.getAll";
    public static final String BETWEEN_DATE_TIME = "Meal.getBetweenDateTime";
    @NotEmpty
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @NotBlank
    @Size(min = 5, max = 25)
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Range(min = 1, max = 10000)
    @Column(name = "calories", nullable = false)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
