DELETE FROM verification_token;
INSERT INTO verification_token (token,user_id) VALUES
('ADMIN_TOKEN', 100001),
('USER_TOKEN', 100000);