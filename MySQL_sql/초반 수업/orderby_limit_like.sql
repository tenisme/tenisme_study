select distinct author_lname from books;

select distinct concat(author_fname, " ", author_lname) from books;

select author_lname
from books
order by author_lname;

select title
from books
order by title desc;

select title, released_year
from books
order by released_year asc;

select title, author_fname, author_lname
from books
order by author_lname asc, author_fname asc;

select title
from books
limit 3;

select title from books limit 6;
select title from books limit 0,6;

select title
from books
limit 2, 4;

select title, released_year
from books
order by released_year desc
limit 5;

select title, released_year, author_lname
from books
order by released_year asc
limit 1;

select title
from books
limit 4, 9999999999;

select count(*) from books;

select title, author_fname
from books
where author_fname like "%da%";

select title
from books
where title like "the%";

select title
from books
where title like "%the%";

select * from tables;

select title, stock_quantity
from books
where stock_quantity like "___";

select title
from books
where title like "___ ______";

use mydb;

select count(book_id+title) from books;

select * from books;

-- 1
select title
from books
where title like "%stories%";

-- 2
select title, pages
from books
order by pages desc
limit 1;

-- 3
select concat(title, " - ", released_year) as summary
from books
order by released_year desc
limit 3;

-- 4
select title, author_lname
from books
where author_lname like "% %";

-- 5
select title, released_year, stock_quantity
from books
order by stock_quantity asc, released_year desc
limit 3;

-- 6
select title, author_lname
from books
order by author_lname asc, title asc;

select title, author_lname
from books
order by 2 asc, 1 asc;

-- 7
select upper(concat("My favorite author is ", author_fname, " ", author_lname, "!"))
as yell
from books
order by author_lname;

select sum(pages), avg(pages)
from books
