-- # node_user로 접속함
-- # 
-- create database node_db
-- default character set utf8
-- default collate utf8_general_ci;
-- # Error Code: 1044. Access denied for user 'node_user'@'%' to database 'node_db'
-- # 에러코드 뜨는 게 정상임. 루트가 db 생성 권한을 안 줌.

-- CREATE TABLE `my_test`.`memo` (
--   `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
--   `title` VARCHAR(50) NULL,
--   `comment` VARCHAR(1000) NULL,
--   PRIMARY KEY (`id`));

-- use my_test;

use my_test;
select * from memo;

-- insert into memo (title, comment) values ("1","2");

-- delete from memo where id = 3;

-- select * from memo where id >=2;

-- # 실습 깃헙 Node Mysql 실습 1 - shirts 테이블

create table shirts
	(
    shirt_id int not null auto_increment,
    article varchar(100),
    color varchar(100),
    shirt_size varchar(100),
    last_worn int,
    primary key(shirt_id)
    );

select * from shirts where color = "off white";
select count(*) from shirts;

-- 실습문제 1
select article, color from shirts limit 20;
-- 실습문제 2
insert into shirts values (default,"polo shirt","Purple","M",50);
-- 실습문제 3
select * from shirts where shirt_size = "M";
-- 실습문제 4
update shirts set shirt_size = "L" where article = "polo shirt";
-- 실습문제 5
update shirts set last_worn = 0 where last_worn = 15;
-- 실습문제 6
update shirts set shirt_size = "XS", color = "off white" where color = "Orange";
-- 실습문제 7
delete from shirts where last_worn = 200;
-- 실습문제 8
delete from shirts where article = "tank top";

-- # 실습 깃헙 Node Mysql 실습 1 - users 테이블

create table users (
	email varchar(255) primary key,
    created_at timestamp default now()
);

select * from users;
select count(*) from users;
-- 실습문제 1
select * from users limit 30;
-- 실습문제 2
select * from users order by created_at asc limit 1;
-- 실습문제 3
select monthname(created_at) as month, count(*) as count from users group by month order by count desc;
-- 실습문제 4
select * from users where email like "%yahoo%" order by created_at desc limit 20;
-- 실습문제 5
select count(*) from users where email like "%yahoo%";

-- # 실습 깃헙 Node Mysql 실습 3 - users 테이블

create table books
	(
		book_id int not null auto_increment,
		title varchar(100),
        author_fname varchar(100),
        author_lname varchar(100),
        released_year int,
        stock_quantity int,
        pages int,
        primary key(book_id)
    );

select * from books;
-- 실습문제 1
select concat(author_fname, " ", author_lname) as full_name from books;
-- 실습문제 2
select concat(substring(title, 1, 10), "...") as "short title" from books;
-- 실습문제 3
select concat(title, " was released in ", released_year) as "blurb" from books;
-- 실습문제 4 ★이거 중요★
select title, char_length(title) as "character count" from books;
-- 실습문제 5
insert into books
values (default, "10% Happier", "Dan", "Harris", 2014, 29, 256),
		(default, "fake_book", "Freida", "Harris", 2001, 287, 428),
        (default, "Lincoln In The Bardo", "George", "Saunders", 2017, 1000, 367);
-- 실습문제 6
select title, released_year from books order by released_year desc limit 5;
-- 실습문제 7
select title, pages from books order by pages desc limit 1;
-- 실습문제 8
select count(*) as cnt from books where title like "%the%";
-- 실습문제 9
select released_year, count(*) as cnt from books group by released_year order by released_year;
-- 실습문제 10
select released_year as year, count(*) as "# books", avg(pages) as "avg pages"
from books group by year order by year;

