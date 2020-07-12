create database tv_show;
use tv_show;
-- reviewers
create table reviewers (
	id int not null auto_increment primary key,
    first_name varchar(100),
    last_name varchar(100)
);
-- series 
create table series (
	id int not null auto_increment primary key,
    title varchar(100),
    released_year year,
    genre varchar(100)
);
-- reviews
create table reviews (
	id int not null auto_increment primary key,
    rating float,
    series_id int,
    reviewer_id int,
    foreign key(series_id) references series(id),
    foreign key(reviewer_id) references reviewers(id)
);

INSERT INTO series (title, released_year, genre) VALUES
   ('Archer', 2009, 'Animation'),
   ('Arrested Development', 2003, 'Comedy'),
   ("Bob's Burgers", 2011, 'Animation'),
   ('Bojack Horseman', 2014, 'Animation'),
   ("Breaking Bad", 2008, 'Drama'),
   ('Curb Your Enthusiasm', 2000, 'Comedy'),
   ("Fargo", 2014, 'Drama'),
   ('Freaks and Geeks', 1999, 'Comedy'),
   ('General Hospital', 1963, 'Drama'),
   ('Halt and Catch Fire', 2014, 'Drama'),
   ('Malcolm In The Middle', 2000, 'Comedy'),
   ('Pushing Daisies', 2007, 'Comedy'),
   ('Seinfeld', 1989, 'Comedy'),
   ('Stranger Things', 2016, 'Drama');
 
 
INSERT INTO reviewers (first_name, last_name) VALUES
   ('Thomas', 'Stoneman'),
   ('Wyatt', 'Skaggs'),
   ('Kimbra', 'Masters'),
   ('Domingo', 'Cortes'),
   ('Colt', 'Steele'),
   ('Pinkie', 'Petit'),
   ('Marlon', 'Crafford');
  
 
INSERT INTO reviews(series_id, reviewer_id, rating) VALUES
   (1,1,8.0),(1,2,7.5),(1,3,8.5),(1,4,7.7),(1,5,8.9),
   (2,1,8.1),(2,4,6.0),(2,3,8.0),(2,6,8.4),(2,5,9.9),
   (3,1,7.0),(3,6,7.5),(3,4,8.0),(3,3,7.1),(3,5,8.0),
   (4,1,7.5),(4,3,7.8),(4,4,8.3),(4,2,7.6),(4,5,8.5),
   (5,1,9.5),(5,3,9.0),(5,4,9.1),(5,2,9.3),(5,5,9.9),
   (6,2,6.5),(6,3,7.8),(6,4,8.8),(6,2,8.4),(6,5,9.1),
   (7,2,9.1),(7,5,9.7),
   (8,4,8.5),(8,2,7.8),(8,6,8.8),(8,5,9.3),
   (9,2,5.5),(9,3,6.8),(9,4,5.8),(9,6,4.3),(9,5,4.5),
   (10,5,9.9),
   (13,3,8.0),(13,4,7.2),
   (14,2,8.5),(14,3,8.9),(14,4,8.9);
   
-- ==========================================================



-- 티비 쇼 제목과 별점을 조회하시오.
select s.title, r.rating
from series as s
join reviews as r
on s.id = r.series_id;



-- 티비쇼의 제목별, 별점평균을 조회하시오.
select s.title, avg(r.rating) as average
from series as s
join reviews as r
on s.id = r.series_id
group by s.title ;



-- 리뷰어의 이름(2개다)과, 그 사람이 준 별점을 조회하세요.
select r.first_name, r.last_name, rv.rating
from reviewers as r
join reviews as rv
on r.id = rv.reviewer_id;



-- 티비쇼 중에서 별점이 없는것들을, 제목으로 조회하시오.
select s.title
from series as s
left join reviews as rv
on s.id = rv.series_id
where rv.rating is null;
-- 각 장르별로 , 장르와 장르의 별점평균을 조회하세요. 
-- (단, 별점평균은 소수점 2자리까지만 나오도록 하세요.)
select s.genre, round( avg(rv.rating) , 2) as average
from series as s
join reviews as rv
on s.id = rv.series_id
group by s.genre;

use mydb;


-- 리뷰 작성을 안한 사람도 표시하되(0으로), 사람별로 각 리뷰의 갯수,
-- 리뷰의 최소별점(rating), 최대별점, 평균별점을 조회하고,
-- 리뷰 갯수가 0보다 크면, "ACTIVE", 그렇지 않으면 "INACTIVE"로 
-- 표시 ( 컬럼명은 status) 
-- first_name, lst_name, count, min, max, avg, status
select r.first_name, r.last_name, count(rv.rating) as count,
	round(min( ifnull(rv.rating,0)),2) as min, 
    round(max(ifnull(rv.rating,0)),2) as max, 
    round(avg( ifnull(rv.rating,0)) , 2) as avg,
    if( count(rv.rating) > 0, "ACTIVE", "INACTIVE") as status
from reviewers as r
left join reviews as rv
on r.id = rv.reviewer_id
group by r.id ;



select first_name, last_name, 
	count(rv.rating) as count,
	round(ifnull( min(rv.rating), 0), 2) as min,
    round(ifnull( max(rv.rating), 0), 2) as max,
    round( ifnull(avg(rv.rating), 0), 2) as avg,
    case
		when count(rv.rating) >= 10 then "POWER USER"
		when count(rv.rating) > 0 then "ACTIVE"
        else "INACTIVE"
    end as status
from reviewers as r
left join reviews as rv
on r.id = rv.reviewer_id
group by r.id;
    
    
    
-- 영화제목으로 알파벳 오름차순 정렬해서, 
-- 영화제목, 별점, 리뷰작성자이름(합쳐서) 조회하시오.
select s.title, rv.rating, 
	concat( r.first_name, " ",r.last_name) as reviewer
from reviewers as r
join reviews as rv
on r.id = rv.reviewer_id
join series as s
on s.id = rv.series_id
order by s.title;