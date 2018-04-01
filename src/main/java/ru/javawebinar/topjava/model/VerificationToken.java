package ru.javawebinar.topjava.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_token", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id", name = "verification_token_unique_user_idx")})
public class VerificationToken extends BaseEntity {
    private static final int EXPIRATION = 24 * 60;
    @Column(name = "token", nullable = false)
    @NotNull
    private String token;

    @Column(name = "expiry_date", nullable = false)
    @NotNull
    private LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION);
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    public VerificationToken() {
    }

    public VerificationToken(String token) {
        super(null);
        this.token = token;
    }

    public VerificationToken(Integer id, String token) {
        super(id);
        this.token = token;
    }

    public VerificationToken(Integer id, String token, LocalDateTime expiryDate) {
        super(id);
        this.token = token;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + getId() +
                "token='" + token + '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
