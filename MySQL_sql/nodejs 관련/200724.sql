-- 200724
-- use my_test;

-- create table movie (
-- 	id int not null auto_increment primary key,
--     title varchar(200),
--     genre varchar(100),
--     attendance int,
--     year date
-- );

-- drop table movie;

select * from movie;

select * from movie where title like '%to%' limit 0, 25;
