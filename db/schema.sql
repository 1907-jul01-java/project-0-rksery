-- create transaction function to add users
DROP VIEW IF EXISTS full_set;
DROP VIEW IF EXISTS full_nopw;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS customerstatus;
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
create table customerstatus(
  statusid serial primary key,
  statusactive text not null unique
);
create table customers(
  custid integer primary key references users(userid),
  accountnumber int not null default nextval('account_id_seq' :: regclass),
  balance money not null,
  custactive integer not null default 1 references customerStatus(statusid)
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
  customerstatus (statusactive)
VALUES
  ('New');
insert into
  customerstatus (statusactive)
VALUES
  ('Approved');
insert into
  customerstatus (statusactive)
VALUES
  ('Denied');
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
  users(username, pw, permissions)
VALUES
  ('jjdoe', 'password', 2);
insert into
  names (nameid, firstname, middlename, lastname)
VALUES
  (get_user_id('jjdoe'), 'John', 'Jay', 'Doe');
insert into
  users (username, pw, permissions)
VALUES
  ('testcust', 'password', 1);
insert into
  names (nameid, firstname, middlename, lastname)
VALUES
  (
    get_user_id('testcust'),
    'My',
    'Test',
    'Customer'
  );
insert into
  customers (custid, balance, custactive)
VALUES
  (get_user_id('testcust'), 123456789.23, 1);
insert into
  users (username, pw, permissions)
VALUES
  ('testcust2', 'password', 1);
insert into
  names (nameid, firstname, middlename, lastname)
VALUES
  (
    get_user_id('testcust2'),
    'My2',
    'Test2',
    'Customer2'
  );
insert into
  customers (custid, balance, custactive)
VALUES
  (get_user_id('testcust2'), 23456789.23, 1);
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
  statusactive,
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
  customerstatus,
  customers
where
  users.permissions = permissions.permid
  and names.nameid = users.userid
  and customers.custid = users.userid
  and customerstatus.statusid = customers.custactive;
create
  or replace view full_nopw as
select
  accountnumber,
  statusactive,
  username,
  firstname,
  middlename,
  lastname,
  balance
from
  permissions,
  users,
  names,
  customerstatus,
  customers
where
  users.permissions = permissions.permid
  and names.nameid = users.userid
  and customers.custid = users.userid
  and customerstatus.statusid = customers.custactive;