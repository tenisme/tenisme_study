use my_test;

desc memos;

create table bootcamp (
	id int not null auto_increment primary key,
    title varchar(100),
    subject varchar(30),
    teacher varchar(20),
    start_time timestamp
);

drop table bootcamp;

select * from bootcamp;

insert into bootcamp (title, `subject`, teacher, start_time)
 values ("안드로이드 개발", "JAVA", "홍길동", "2020-05-02 11:23:00");


-- 깃허브 Node 실습 8

create table contact (
	id int not null auto_increment primary key,
    name varchar(100),
    phone varchar(50),
    comment varchar(100)
);

select * from contact;

select * from contact where name like "%000%" or phone like "%000%";

insert into contact (name, phone) values
("홍길동", "010-1234-7676");

drop table contact;

select * from contact limit 0, 10;

select * from bootcamp;

-- server-ex 폴더 실습
-- 유저의 이메일, 비밀번호, 회원가입날짜/시간 3가지를 저장하는 테이블을 생성. 테이블명은 user
create table user (
	id int not null auto_increment primary key,
    email varchar(100),
    passwd varchar(100),
    created_at timestamp default current_timestamp
);

select * from user;

insert into user (email, passwd) values
("abc@naver.com", "12341234");
-- 패스워드는 암호화해야한다.