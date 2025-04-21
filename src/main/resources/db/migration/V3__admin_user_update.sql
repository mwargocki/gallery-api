DELETE FROM users
where username like 'admin';

INSERT INTO users (username, password, roles)
VALUES ('admin', '$2a$10$bVVhLQrHGKd0n7uqSt30WeXR05Ejl7wfxPb1vXzuPLhrUERLD7zIe', 'ADMIN')