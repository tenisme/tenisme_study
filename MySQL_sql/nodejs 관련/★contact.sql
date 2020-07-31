# contact

# 유저 테이블 설계
## 필요한 것
### 유저 정보 (유저 id, 로그인 아이디, 이메일, 비밀번호)
### 연락처 정보 (연락처 id, 이름, 연락처, 메모, 이 연락처를 저장한 유저의 id)
### 연락처 공유 정보 (공유 id, 연락처 id, 공유하는 사람 id)

create table contact_users (
	user_id int not null auto_increment primary key,
    login_id varchar(30),
    email varchar(200),
    passwd varchar(100),
    created_at timestamp default now(),
    reset_passwd_token varchar(100) default ''
);
create table contact_tokens (
	id int not null auto_increment primary key,
    user_id int,
    token varchar(200),
    foreign key (user_id) references contact_users(user_id) on delete cascade
);
create table contacts (
	contact_id int not null auto_increment primary key,
    user_id int,
    name varchar(100),
    phone varchar(50),
    comment varchar(100),
    foreign key (user_id) references contact_users(user_id) on delete cascade
);
create table contact_share (
	share_id int not null auto_increment primary key,
    contact_id int,
    shared_user_id int,
    foreign key (contact_id) references contacts(contact_id),
    foreign key (shared_user_id) references contact_users(user_id)
);

# insert_contact_data 파일로 insert - 하지말기~

drop table contacts;
drop table contact_users;
drop table contact_tokens;
drop table contact_share;

truncate table contacts;
truncate table contact_users;
truncate table contact_tokens;
truncate table contact_share;

use my_test;
select * from contact_users;
select * from contact_tokens;
select * from contacts;
select * from contact_share;

insert into contacts (user_id, name, phone) values (1, 'Gav Menghi', '838-334-8484');