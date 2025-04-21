INSERT INTO users (username, password, roles)
VALUES ('admin', '$2a$10$8Igxv5ulv3HVb95X3UzzBuNRVmH4gM9eIXJAfqkiIlC8TqqGiUnhK', 'ADMIN')
ON CONFLICT DO NOTHING;