use my_test;
create table book_user (
	user_id int not null auto_increment primary key,
    email varchar(50) not null unique,
    passwd varchar(100) not null,
	age int not null
);
create table book_user_token (
	token_id int not null auto_increment primary key,
    user_id int not null,
    token varchar(200) not null,
    foreign key (user_id) references book_user (user_id)
);
create table book (
	id int not null auto_increment primary key,
    title varchar(100) not null,
    author varchar(100) not null,
    limit_age int
);
create table book_rental (
	rental_id int not null auto_increment primary key,
    user_id int not null,
    book_id int not null,
    limit_date timestamp,
    foreign key (user_id) references book_user (user_id) on delete cascade,
    foreign key (book_id) references book (id)
);
drop table book_user_token;
drop table book_user;

select * from book_user;
select * from book_user_token;
select * from book limit 0, 25;
select * from book_rental;

select u.age, b.limit_age from book_user as u, book as b where u.user_id = 3 and b.id = 100;

select r.rental_id, r.book_id, b.title, b.author, b.limit_age, r.limit_date 
from book_rental as r join book as b on r.book_id = b.id where user_id = 3;