use photo_sns_test;
# 아직 인덱스 안걸어놓음. 걸어놓으면 어디다 걸어놨는지 메모하기
# 테이블 작성
create table users (
	user_id int not null auto_increment primary key,
    loginId varchar(20) not null unique,
    email varchar(100) not null unique,
    passwd varchar(100) not null,
    created_at timestamp not null default now(),
    reset_passwd_token varchar(100)
);
create table device (
	device_id int not null auto_increment primary key,
    device_name varchar(50) not null unique
);
create table user_token (
	token_id int not null auto_increment primary key,
    user_id int not null,
    device_id int not null,
    token varchar(200) not null,
    created_at timestamp not null default now(),
    foreign key (user_id) references users(user_id) on delete cascade,
    foreign key (device_id) references device(device_id)
);
create table posting (
	posting_id int not null auto_increment primary key,
    user_id int not null,
    public_on int not null default 0,
	photo_url varchar(150) not null unique,
    comments varchar(300),
    created_at timestamp not null default now(),
    updated_at timestamp not null default now() on update now(),
    foreign key (user_id) references users(user_id)
);
# public_on
# 0 : 포스팅 전체 공개
# 1 : 포스팅 팔로워 공개
# 2 : 포스팅 비공개(나만 보기)
create table tag (
	tag_id int not null auto_increment primary key,
    tag_name varchar(30) unique
);
create table posting_tag (
	posting_tag_id int not null auto_increment primary key,
    posting_id int not null,
    tag_id int not null,
    unique (posting_id, tag_id),
    foreign key (posting_id) references posting (posting_id) on delete cascade,
    foreign key (tag_id) references tag (tag_id) on delete cascade
);
create table relation (
	relation_id int not null auto_increment primary key,
    request_user_id int,
    targeted_user_id int,
    permit int default 0,
    unique (request_user_id, targeted_user_id),
    foreign key (request_user_id) references users(user_id) on delete cascade,
    foreign key (targeted_user_id) references users(user_id) on delete cascade
);
# permit
# 0 : 친구 신청 후 대기상태(디폴트)
# 1 : 친구 신청하고 수락받은 상태(친구 상태)
# 2 : 친구 신청 거절 상태(디폴트와 같은 효과 but 다양하게 활용 가능)
# 3 : 차단 상태 1 - request_user_id가 targeted_user_id를 차단함. request는 targeted의 포스팅을 조회하지 못함. 반대는 가능함.
# 4 : 차단 상태 2 - request_user_id가 targeted_user_id를 차단함. 두 유저는 서로의 게시글을 볼 수 없음.
# 신청 취소, 언팔은 relationship에서 데이터 삭제

insert into device (device_name) values 
("samsung"),("apple"),("lg"),("shaomi"),("google");

truncate table users;
truncate table user_token;
truncate table posting;
truncate table tag;
truncate table posting_tag;
truncate table relation;

drop table users;
drop table user_token;
drop table posting;
drop table tag;
drop table posting_tag;
drop table relation;

select * from device order by device_id;
select * from relation;
select * from posting;
select * from tag order by tag_id;
select * from posting_tag;
select * from users;
select * from user_token;

insert into user_token (user_id, token, device_id) values (1, "asdb", 1);
insert into users (loginId, email, passwd) values ("abc", "abc@email.com", "123456789");

delete from posting_tag where posting_id = 48;

# 포스팅 조회 쿼리 변경 전
select p.posting_id, p.public_on, p.photo_url, p.comments, p.created_at, t.tag_name
from posting_tag as pt left join tag as t on pt.tag_id = t.tag_id
join posting as p on p.posting_id = pt.posting_id
where p.user_id = 3 order by p.created_at desc;
# 포스팅 조회 쿼리 변경 후
select p.posting_id, p.public_on, p.photo_url, p.comments, p.created_at, t.tag_name
from posting as p left join posting_tag as pt on p.posting_id = pt.posting_id 
left join tag as t on pt.tag_id = t.tag_id where p.user_id = 3 order by p.created_at desc;