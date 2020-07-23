-- 200722/23
select * from user;

select * from user where email = 'dfd@naver.com';

insert into user (email, passwd) values ('abc@naver.com', 'adfasdf');

create table user (
	id int not null auto_increment primary key,
    email varchar(200) unique,
    passwd varchar(200),
    created_at timestamp default current_timestamp
);

create table token (
	id int not null auto_increment primary key,
    user_id int,
    token varchar(500),
    foreign key (user_id) references user(id) on delete cascade
);

drop table user;
drop table token;

desc user;
desc token;

select * from user;
select * from token;

use my_test;

-- 내가 쓴 메모는 나만 볼 수 있도록 만들 것임.
-- crud를 해당 유저 아이디로 처리.
-- 유저 아이디/비번을 저장할 memo_user 테이블도 생성.
-- 유저 인증 정보를 저장하는 memo_token 테이블도 생성.
-- memos 테이블에 user_id 컬럼 추가.

create table memo_user (
	user_id int not null auto_increment primary key,
    login_id varchar(30) unique,
    created_at timestamp default current_timestamp,
    password varchar(200)
);

create table memo_token (
	id int not null auto_increment primary key,
    user_id int,
    login_device varchar(100),
    token varchar(500),
    foreign key (user_id) references memo_user(user_id) on delete cascade
);

create table memos (
	id int not null auto_increment primary key,
    user_id int,
    title varchar(300),
    content varchar(2000),
    created_at timestamp default current_timestamp,
    updated_at timestamp default now() on update current_timestamp,
    foreign key (user_id) references memo_user(user_id) on delete cascade
);
drop table memos;
drop table memo_token;
drop table memo_user;

desc memo_user;
desc memo_token;
desc memos;

select * from memo_user;
select * from memo_token;
select * from memos;