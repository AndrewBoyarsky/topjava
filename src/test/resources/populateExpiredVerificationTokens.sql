DELETE FROM verification_token;
INSERT INTO verification_token (token,expiry_date, user_id) VALUES
  ('ADMIN_TOKEN_EXPIRED', now(),100001),
  ('USER_TOKEN_EXPIRED', now(),100000);