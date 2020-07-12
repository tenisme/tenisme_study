use mydb;
create table books (
book_id int not null auto_increment,
title varchar(100),
author_fname varchar(100),
author_lname varchar(100),
released_year int,
stock_quantity int,
pages int,
primary key (book_id)
);

INSERT INTO books (title, author_fname, author_lname, released_year, stock_quantity, pages)
VALUES
('The Namesake', 'Jhumpa', 'Lahiri', 2003, 32, 291),
('Norse Mythology', 'Neil', 'Gaiman',2016, 43, 304),
('American Gods', 'Neil', 'Gaiman', 2001, 12, 465),
('Interpreter of Maladies', 'Jhumpa', 'Lahiri', 1996, 97, 198),
('A Hologram for the King: A Novel', 'Dave', 'Eggers', 2012, 154, 352),
('The Circle', 'Dave', 'Eggers', 2013, 26, 504),
('The Amazing Adventures of Kavalier & Clay', 'Michael', 'Chabon', 2000, 68, 634),
('Just Kids', 'Patti', 'Smith', 2010, 55, 304),
('A Heartbreaking Work of Staggering Genius', 'Dave', 'Eggers', 2001, 104, 437),
('Coraline', 'Neil', 'Gaiman', 2003, 100, 208),
('What We Talk About When We Talk About Love: Stories', 'Raymond', 'Carver', 1981, 23, 176),
("Where I'm Calling From: Selected Stories", 'Raymond', 'Carver', 1989, 12, 526),
('White Noise', 'Don', 'DeLillo', 1985, 49, 320),
('Cannery Row', 'John', 'Steinbeck', 1945, 95, 181),
('Oblivion: Stories', 'David', 'Foster Wallace', 2004, 172, 329),
('Consider the Lobster', 'David', 'Foster Wallace', 2005, 92, 343);

select author_fname, author_lname from books;
select concat(author_fname," ",author_lname) as "author" from books;

select concat(title, " - ", author_fname, " - ", author_lname) as title_author from books;
select concat_ws(" - ", title, author_fname, author_lname) as title_author from books;

select substring("Hello World", 3);
select substring("Hello World", 1, 4);
select substring("Hello World", -3);

select title from books;

select substring(title,1,10) from books;

select concat(substring(title,1,10), "...") as title from books;

select replace("Hello World", "Hell", "Heaven");

select replace("cheese bread coffee milk", " ", " and ");

select replace(title, "e", "3") from books;

select reverse("Hello World");

select author_fname, reverse(author_lname) from books;

select char_length("Hello World");

select author_lname, char_length(author_lname) as length from books;

select concat(author_lname, " is ", char_length(author_lname)) as author_len from books;

use mydb;

select * from books;

select upper(title) from books;

select concat("My favorite book is ", lower(title)) as favorite_book
from books;

-- 1번
select replace(title, " ", "->") as "title"
from books;
-- 2번
select author_lname as forwards, reverse(author_lname) as backwards
from books;
-- 3번
select upper(concat(author_fname, " ", author_lname)) as "full name in caps"
from books;
-- 4번
select concat(title, " was released in ", released_year) as blurb
from books;
-- 5번
select title, char_length(title) as "character count"
from books;
-- 6번
select concat(substring(title, 1, 10), "...") as "short title",
concat(author_lname, ",",author_fname) as author,
concat(stock_quantity, " in stock") as quantity
from books;