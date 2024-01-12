SET search_path TO public

drop table if exists shedlock;
create table shedlock(
  name VARCHAR(64) NOT NULL,
  lock_until TIMESTAMP(3) NOT NULL,
  locked_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  locked_by VARCHAR(255) NOT NULL,
  primary key (name)
)

alter table executions drop constraint FK_executions_region
alter table executions drop constraint FK_executions_results
alter table schedules drop constraint FK_schedules_region

drop table if exists countries cascade
drop table if exists executions cascade
drop table if exists regions cascade
drop table if exists results cascade
drop table if exists schedules cascade
drop table if exists tests cascade
drop table if exists users cascade

create table countries (id  bigserial not null, display_name varchar(255), iso2 varchar(255), primary key (id))
create table regions (id  bigserial not null, aws_cloud_region varchar(255), city varchar(255), country_id int8, default_region boolean, primary key (id))

create table users (id  bigserial not null, first_name varchar(255), is_account_non_expired boolean, is_account_non_locked boolean, is_credentials_non_expired boolean, enabled boolean, last_name varchar(255), password varchar(100), role varchar(255), username varchar(255), uuid varchar(255), primary key (id))
create table tests (id  bigserial not null, json text not null, name varchar(255) not null, primary key (id))
create table executions (id  bigserial not null, created timestamp, last_modified timestamp, name varchar(255), schedule_id int8, status int4, test_id int8, type int4, region int8, result int8, primary key (id))
create table results (id  bigserial not null, logs text not null, status int4 not null, primary key (id))
create table schedules (id  bigserial not null, emails varchar(255), name varchar(255), test_id int8, timer varchar(255), uuid varchar(255), region int8, primary key (id))

alter table executions add constraint FK_executions_region foreign key (region) references regions
alter table executions add constraint FK_executions_results foreign key (result) references results
alter table schedules add constraint FK_schedules_region foreign key (region) references regions