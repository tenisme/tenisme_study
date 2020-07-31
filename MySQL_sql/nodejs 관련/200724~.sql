-- 200724
use my_test;

create table movie (
	id int not null auto_increment primary key,
    title varchar(200),
    genre varchar(100),
    attendance int,
    year date
);

-- drop table movie;

select * from movie;

select * from movie where title like '%%' limit 0, 25;

select * from movie where title like '%%' order by year asc limit 0, 25;

desc movie;

# 200727 월요일

select * from token;

select * from user;

select * from user as u join token as t on u.id = t.user_id;
delete from user where id = (select u.id from user as u join token as t on u.id = t.user_id where u.id = 1);

# 200728 화요일

use my_test;

# user table에 reset_passwd_token 컬럼 만들기

select * from user;
select * from token;

# movie-server 테이블 실습

select * from movie;

create table movie_user (
	user_id int not null auto_increment primary key,
    login_id varchar(20) unique,
    email varchar(200),
    passwd varchar(100),
    created_at timestamp default current_timestamp,
    reset_passwd_token varchar(100) default ''
);

create table movie_token (
	id int not null auto_increment primary key,
    user_id int,
    token varchar(200),
    foreign key (user_id) references movie_user(user_id) on delete cascade
);

create table favorite_movie (
	id int not null auto_increment primary key,
    user_id int,
    movie_id int,
    foreign key (user_id) references movie_user(user_id) on delete cascade,
    foreign key (movie_id) references movie(id) on delete cascade
);

drop table favorite_movie;
truncate table movie_token;

select * from movie;
select * from movie_user;
select * from movie_token;
select * from favorite_movie;

# user_id, movie_id
select * from favorite_movie where user_id = 10 and movie_id = 1;
insert into favorite_movie (user_id, movie_id) values ();

select m.* from favorite_movie as f join movie as m on f.movie_id = m.id where f.user_id = 2;

delete from favorite_movie where user_id = 2 and movie_id = 1;

# 200729 실습 - 댓글 api 개발

create table movie_reply (
	reply_id int not null auto_increment primary key,
    user_id int,
    movie_id int,
    comments varchar(100),
    rating int,
    created_at timestamp default now(),
    updated_at timestamp default now() on update now(),
    foreign key (user_id) references movie_user(user_id),
    foreign key (movie_id) references movie(id)
);
drop table movie_reply;
truncate table movie_reply;

# 댓글 달기 api 쿼리
insert into movie_reply (user_id, movie_id, rating, comments) values (2, 2, "그닥", 4);

# 댓글 수정 api 쿼리
-- 아래의 rows.length가 0이면 리턴
select * from movie_reply where user_id = 2 and movie_id = 2;
-- 댓글 수정
update movie_reply set comments = "안녕" where user_id = 2 and movie_id = 2;

# 댓글 삭제 api 쿼리
-- 아래의 rows.length가 0이면 리턴
select * from movie_reply where user_id = 2 and reply_id = 15;
-- 댓글 삭제
delete from movie_reply where user_id = 2 and reply_id = 15;

# 댓글 조회 api 쿼리
select r.movie_id, r.reply_id, m.title, u.login_id, r.rating, r.comments, r.created_at, r.updated_at 
from movie_reply as r
join movie as m on r.movie_id = m.id
join movie_user as u on r.user_id = u.user_id
where r.movie_id = 1
limit 0, 25;

# 영화 조회 api 수정 - 조회 목록에 댓글 수, 별점 평균 추가
# 전체 조회 api 수정
select m.*, count(r.comments) as cnt_comments, 
ifnull(round(avg(r.rating), 2), "unrated") as avg_rating 
from movie as m 
left join movie_reply as r 
on m.id = r.movie_id 
group by m.id
order by m.id
limit 0, 25;

# 영화명으로 조회 api 수정
select m.*, count(r.comments) as cnt_comments, 
ifnull(round(avg(r.rating), 2), "unrated") as avg_rating 
from movie as m 
left join movie_reply as r 
on m.id = r.movie_id 
where m.title like "%the%"
group by m.id
order by m.id
limit 0, 25;

# 연도 정렬 조회 api 수정
# 가져오고 수정하기 귀찮다.

# 관객수 정렬 조회 api 수정
# 가져오고 수정하기 귀찮다.

# 실습 - 영화 예약 api 기능 만들기
# reserve_movie 테이블 만들기

create table start_at(
	start_at_id int not null auto_increment primary key,
    movie_id int,
    screening_no int,
    start_at timestamp,
    foreign key (movie_id) references movies(id)
);
create table theaters(
	thearter_id int not null auto_increment primary key,
    thearter_no int,
    seat_no varchar(2)
);
create table reservations(
	reserve_id int not null auto_increment primary key,
    thearter_id int,
    start_at_id int,
    user_id int,
    foreign key (thearter_id) references theaters(thearter_id),
    foreign key (start_at_id) references start_at(start_at_id)
);
drop table abc;
# 영화 예약에 필요한 것
# 영화 정보(영화 id, 영화 제목, ...)
# 상영 시간 정보(상영 시간 id, 영화 id, 상영 순서, 상영 날짜/시간)
# 상영관 정보(상영관 id, 상영관 넘버, 좌석 넘버)
# 예약 정보(예약 id, 상영관 id, 상영 시간 id, 유저 id)

select seats_id, start_at, seat_no from movie_seats where movie_id = 1 and start_at = "2020-07-31 12:30:00" order by seats_id;

alter table my_test.favorite_movie rename movie_test.favorites;
alter table my_test.movie rename movie_test.movies;
alter table my_test.movie_reply rename movie_test.replies;
alter table my_test.movie_token rename movie_test.user_tokens;
alter table my_test.movie_user rename movie_test.users;

use movie_test;
select * from movies;
select * from users;
select * from user_tokens;
select * from favorites;
select * from replies;
select * from start_at;
select * from theaters;
select * from reservations;

