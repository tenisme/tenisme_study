create table employees
(
id int not null auto_increment,
last_name varchar(30) not null,
first_name varchar(30) not null,
middle_name varchar(30),
age int not null,
current_status varchar(20) default "employed",
primary key (id)
);

desc employees;
select * from employees;

drop table cats;

create table cats (
cat_id int not null auto_increment primary key,
name varchar(100),
breed varchar(100),
age int
);

desc cats;
select * from cats;

insert into cats (name, breed, age)
values ("Ringo", "Tabby", 4),
("Cindy", "Maine Coon", 11),
("Dumbleddore", "Maine Coon", 11),
("Egg", "Persian", 4),
("Misty", "Tabby", 13),
("George Michael", "Ragdoll", 9),
("Jackson", "Sphynx", 7);

update cats
set breed = "Shorthair"
where breed = "Tabby" and cat_id > 0;

update cats
set age = 14
where name = "Misty" and cat_id > 0;

use mydb;

update cats
set name = "Jack"
where name = "Jackson";

select count(*)
from cats
where name = "Jackson";

select count(*)
from cats
where name = "Jack";

update cats
set breed = "British Shorthair"
where name = "Ringo";

select * from cats;

select *
from cats
where breed like "%hair%";

update cats
set age = 12
where breed = "Maine Coon";

select *
from cats
where age > 8;

update cats
set name = "Kitty", breed = "Ragdoll"
where age > 8;

delete from cats where age = 4;

update cats
set age = 3
where age = 5;

select * from cats;

delete
from cats
where cat_id = age;