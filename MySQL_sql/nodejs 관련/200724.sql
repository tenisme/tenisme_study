-- 200724
use my_test;

-- create table movie (
-- 	id int not null auto_increment primary key,
--     title varchar(200),
--     genre varchar(100),
--     attendance int,
--     year date
-- );

-- drop table movie;

select * from movie;

select * from movie where title like '%%' limit 0, 25;

select * from movie where title like '%%' order by year asc limit 0, 25;

desc movie;

# 200727 월요일

select * from token;

select * from user;



select * from user as u join token as t on u.id = t.user_id;
delete from user where id = (select u.id from user as u join token as t on u.id = t.user_id where u.id = 1);