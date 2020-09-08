INSERT INTO countries (iso2, display_name)
VALUES
  ('US', 'US'),
  ('CA', 'Canada');

INSERT INTO regions (city, default_region, country)
VALUES
  ('San Francisco', true, (SELECT id FROM countries WHERE iso2 = 'US')),
  ('New York', false, (SELECT id FROM countries WHERE iso2 = 'US')),
  ('Toronto', false, (SELECT id FROM countries WHERE iso2 = 'CA'));