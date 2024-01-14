DROP TABLE IF EXISTS shedlock;

CREATE TABLE shedlock(
  name VARCHAR(64) NOT NULL,
  lock_until TIMESTAMP(3) NOT NULL,
  locked_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  locked_by VARCHAR(255) NOT NULL,
  PRIMARY KEY (name)
);


INSERT INTO countries (iso2, display_name)
VALUES
  ('US', 'US'),
  ('CA', 'Canada'),
  ('EU', 'Europe');

INSERT INTO regions (city, aws_cloud_region, default_region, country_id)
VALUES
  ('San Francisco', 'us-west-1', true, (SELECT id FROM countries WHERE iso2 = 'US')),
  ('New York', 'us-east-1', false, (SELECT id FROM countries WHERE iso2 = 'US')),
  ('Toronto', 'ca-central-1', false, (SELECT id FROM countries WHERE iso2 = 'CA')),
  ('Frankfurt', 'eu-central-1', false, (SELECT id FROM countries WHERE iso2 = 'EU'));

INSERT INTO users (uuid, username, first_name, last_name, password, role, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, enabled)
VALUES
 ('429edb84-fef7-4b25-80a4-158e47839cdf', 'user@user.com', 'ikola', 'mavr', '$2a$10$ZoXPf4qjJf/zrZazfN62quLf3A6qXFCVuOJJvLynsYsaKVKY5pJye','ROLE_USER', true,true,true,true),
 ('fc88dc17-cadc-4b76-b894-cda88488529b', 'admin@admin.com', 'reo', 'savr', '$2a$10$GMhQNJ6Fo6/IZlJfnWgnluH.YA2gWB/4EfsNzOnJwyftsv5eTEw8e','ROLE_ADMIN', true,true,true,true);
