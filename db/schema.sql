-- create transaction function to add users
DROP VIEW full_set;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS names;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS permissions;
DROP SEQUENCE IF EXISTS account_id_seq;
create sequence account_id_seq increment 3 minvalue 100000 maxvalue 999999 start 100000;
-- CREATE
-- OR REPLACE FUNCTION random_between(low INT, high INT) RETURNS INT AS $$ BEGIN RETURN floor(random() * (high - low + 1) + low);
-- END;
--$$ language 'plpgsql' STRICT;
create table permissions (
  permid serial primary key,
  title text not null unique
);
create table users (
  userid serial not null primary key,
  accountnumber int not null default nextval('account_id_seq' :: regclass),
  username text not null unique,
  pw text not null,
  permissions integer not null
);
create table names (
  nameid integer primary key references users(userid),
  firstname text not null,
  middlename text,
  lastname text not null
);
create table customers(
  custid integer primary key references users(userid),
  -- accountnumber int not null references users(accountnumber),
  balance money not null
);
create
or replace function get_user_id (user_name text) returns integer as $$
SELECT
  userid
from
  users
where
  username = user_name;
$$ language sql;
create
  or replace function get_user_perm (user_name text) returns integer as $$
SELECT
  permissions
from
  users
where
  username = user_name;
$$ language sql;
insert into
  permissions (title)
VALUES('Customer');
insert into
  permissions (title)
VALUES('Employee');
insert into
  permissions (title)
VALUES('Administrator');
insert into
  users (username, pw, permissions)
VALUES
  ('rksery', 'P4ssw0rd!', 3);
insert into
  names (nameid, firstname, middlename, lastname)
VALUES
  (
    get_user_id('rksery'),
    'Robert',
    'Keefer',
    'Sery'
  );
insert into
  customers (custid, balance)
VALUES(get_user_id('rksery'), 12345.23);
-- create
  --   or replace function get_time () returns time with time zone as $$
  -- select
  --   current_time;
  -- $$ language sql;
  -- http://www.postgresqltutorial.com/postgresql-random-range/
  create
  or replace view full_set as
select
  title,
  accountnumber,
  username,
  pw,
  firstname,
  middlename,
  lastname,
  balance
from
  permissions,
  users,
  names,
  customers
where
  users.permissions = permissions.permid
  and names.nameid = users.userid
  and customers.custid = users.userid;