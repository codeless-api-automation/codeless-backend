INSERT INTO countries (iso2, display_name)
VALUES
  ('US', 'US'),
  ('CA', 'Canada');

INSERT INTO regions (city, default_region, country)
VALUES
  ('San Francisco', true, (SELECT id FROM countries WHERE iso2 = 'US')),
  ('New York', false, (SELECT id FROM countries WHERE iso2 = 'US')),
  ('Toronto', false, (SELECT id FROM countries WHERE iso2 = 'CA'));

INSERT INTO users (username, password, role, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, enabled)
VALUES
 ('user','$2a$10$ZoXPf4qjJf/zrZazfN62quLf3A6qXFCVuOJJvLynsYsaKVKY5pJye','ROLE_USER', 1,1,1,1),
 ('admin','$2a$10$GMhQNJ6Fo6/IZlJfnWgnluH.YA2gWB/4EfsNzOnJwyftsv5eTEw8e','ROLE_ADMIN', 1,1,1,1);
