SET search_path TO public

INSERT INTO countries (iso2, display_name)
VALUES
  ('US', 'US');

INSERT INTO regions (city, aws_cloud_region, default_region, country_id)
VALUES
  ('New York', 'us-east-1', false, (SELECT id FROM countries WHERE iso2 = 'US'));