-- 200722 수요일
select * from user;

select * from user where email = 'dfd@naver.com';

insert into user (email, passwd) values ('abc@naver.com', 'adfasdf');



create table token (
	id int not null auto_increment primary key,
    token varchar(500),
	user_id int,
    foreign key (user_id) references user(id) on delete cascade
);

drop table token;

desc user;
desc token;

select * from user;
select * from token;

