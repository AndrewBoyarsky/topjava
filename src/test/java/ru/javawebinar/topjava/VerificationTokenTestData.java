package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.model.VerificationToken;

import java.time.LocalDateTime;

public class VerificationTokenTestData {
    public static final String ADMIN_TOKEN_STRING = "ADMIN_TOKEN";
    public static final String USER_TOKEN_STRING = "USER_TOKEN";
    public static final LocalDateTime ADMIN_TOKEN_EXPIRY_DATE = LocalDateTime.now().plusDays(1);
    public static final Integer ADMIN_TOKEN_ID = BaseEntity.START_SEQ + 10;
    public static final Integer USER_TOKEN_ID = BaseEntity.START_SEQ + 11;
    public static final VerificationToken ADMIN_VERIFICATION_TOKEN = new VerificationToken(ADMIN_TOKEN_ID, ADMIN_TOKEN_STRING, ADMIN_TOKEN_EXPIRY_DATE);
    public static final VerificationToken USER_VERIFICATION_TOKEN = new VerificationToken(USER_TOKEN_ID, USER_TOKEN_STRING, ADMIN_TOKEN_EXPIRY_DATE);
    public static final ModelMatcher VERIFICATION_TOKEN_MATHER = ModelMatcher.of(VerificationToken.class);

    public static VerificationToken createVerificationToken(VerificationToken token) {
        return new VerificationToken(token.getId(), token.getToken(), token.getExpiryDate());
    }

    public static VerificationToken createVerificationToken(VerificationToken token, LocalDateTime expiryDate) {
        return new VerificationToken(token.getId(), token.getToken(), expiryDate);
    }

}
