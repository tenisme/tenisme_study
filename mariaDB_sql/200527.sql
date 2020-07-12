select count(*) from books;

select count(distinct author_fname) from books;

select count(*)released_year
from books
where title like "%the%";

select count(distinct author_lname, author_fname)
from books;

select author_lname, count(*)
from books
group by author_lname;

select released_year, count(*)
from books
group by released_year;

select released_year, count(*)
from books
group by released_year;

select max(pages)
from books;

select title, pages from books
where pages = (select max(pages) from books);

select title, pages from books
order by pages desc
limit 1;

select title, pages
from books
where pages = (select min(pages) from books);

select title, pages
from books
order by pages asc
limit 1;

select concat(author_fname, " ", author_lname) as "name", released_year
from books
group by author_fname, author_lname
order by released_year asc;

select concat(author_fname, " ", author_lname) as "name", min(released_year)
from books
group by author_fname, author_lname;

select pages, concat(author_fname, " ", author_lname) as "name"
from books
group by author_fname, author_lname
order by pages desc;

select concat(author_fname, " ", author_lname) as "name", max(pages)
from books
group by author_fname, author_lname;

select concat(author_fname, " ", author_lname) as "name", sum(pages) as total
from books
group by author_fname, author_lname
order by total;

select avg(released_year) from books;

select released_year, avg(stock_quantity)
from books
group by released_year
order by released_year desc;

select released_year, count(*)
from books
group by released_year
order by released_year;

select sum(stock_quantity)
from books;

select released_year, count(*), avg(pages)
from books
group by released_year
order by released_year;


create table people2
(
name varchar(100),
birthdate date,
birthtime time,
birthdt datetime
);

insert into people2 (name, birthdate, birthtime, birthdt)
values ("Padma", "1989-11-11", "20:07:35", "1989-11-11 20:07:35");

insert into people2 (name, birthdate, birthtime, birthdt)
values ("Larry", "1992-10-21", "13:17:30", "1992-10-21 13:17:30");

select * from people2;

select curdate();

insert into people2 (name, birthdate, birthtime, birthdt)
values ("Mike", curdate(), curtime(), now());

select name, birthdate from people2;

select name, day(birthdate) from people2;

select name, dayname(birthdate) from people2;

select name, dayofweek(birthdate) from people2;

select name, month(birthdt) from people2;

select name
from people2
where month(birthdt) = 10;

select name, monthname(birthdt) from people2;

select name, year(birthdt) from people2;

select date_format(birthdt, "Was born on a %W") from people2;

select date_format(birthdt, "%Y/%m/%d at %h:%i") from people2;

--  두 날짜의 차이를 "일수"로 구해달라.
select datediff(now(), birthdate) from people2;

-- interval 1 month >> 한달 후
select birthdt, date_add(birthdt, interval 1 month) from people2;
-- interval 10 second
select birthdt, date_add(birthdt, interval 10 second) from people2;
-- interval -15 day
select birthdt, date_add(birthdt, interval -15 day) from people2;
-- interval -3 year
select birthdt, date_add(birthdt, interval -3 year) from people2;

select birthdt, birthdt + interval 1 month from people2;
select birthdt, birthdt - interval 5 month from people2;

select birthdt, birthdt + interval 15 month + interval 10 hour from people2;







insert into comments (content)
values ("Hello");

insert into comments (content)
values ("Goodbye");

insert into comments (content)
values ("I found");

select * from comments;

-- 컬럼 추가하기
-- 테이블 > 알터테이블 > 

create table comments (
content varchar(100),
created_at timestamp default now(),
-- 메세지 작성시 시간은 아무것도 입력 안 할테니까 자동으로 현재시간 저장하라. 는 컬럼을 만들 땐 실무에서는 보통 위와 같이 작성한다.
changed_at timestamp default now() on update current_timestamp
);

insert into comments (content)
values ("Hello");

insert into comments (content)
values ("Goodbye");

insert into comments (content)
values ("I found");

select * from comments;

update comments set content = "hi"
where content = "hello";

select * from comments
order by changed_at desc;

select title from books
where released_year = 2013;

select title from books
where released_year != 2013;

select title
from books
where title not like "%W%";

select title
from books
where author_fname != "Neil"
order by book_id desc;

insert into books (title, author_fname, author_lname, released_year,
stock_quantity, pages)
values ("Hell", "Neil Mike", "Gaiman", 2020, 100, 742);

select * from books
order by book_id desc;

select title, released_year
from books
where released_year >= 2000
order by released_year desc;
