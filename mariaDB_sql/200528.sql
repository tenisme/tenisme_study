use mydb;



-- ★★★ and : 여러 조건이 '그리고'라는 의미로 해석 될 때 사용.

-- (1) 작가의 lname이 Eggers이고 발간 년도가 2010년 이상인 책들의 책 제목과 작가 lname을 조회하라.
select title, author_lname
from books
where author_lname = "Eggers" and released_year >= 2010;
-- 비교 조건 좀 잘 확인하자. ~이상 ~이하 등등

-- (2) 작가 이름이 Eggers이고 발간 년도가 2010년 이상이고 책제목에 novel이 포함된 책들의 모든 컬럼을 전부 조회하시오
select *
from books
where author_lname = "Eggers" and released_year >= 2010 and title like "%novel%";
-- good
-- 색인시 대소문자 가리지 않음 주의.



-- ★★★ or : 또는, ~거나 이렇게 해석될 때 사용
-- (3) 작가 이름이 Eggers 또는 발간 년도가 2010년 이상인 책들만 책 제목과 작가 이름, 발간 년도로 조회하시오.
select title, author_lname, released_year
from books
where author_lname = "Eggers" or released_year >= 2010;

-- (4) 작가 이름이 Eggers이거나 발간 년도가 2010년 이상이거나 stock_quantity가 100 이상인 책들만, 모든
-- 컬럼을 조회하시오.
select *
from books
where author_lname = "Eggers" or released_year >= 2010 or stock_quantity >= 100;



-- ★★★ between : a와 b 사이의 값으로 조회해달라. a부터 b까지
-- between은 and 연산자와 서로 대체해도 됨.
-- (5) 발간 년도가 2004년 이상이고 2015년 이하인 책 제목만 조회하시오.
select title
from books
where released_year >= 2004 and released_year <= 2015;
-- (6) 발간 년도가 2004년부터 2015년 사이(between)에 나온 책 제목만 조회하시오. (5)와 같은 말
select title
from books
where released_year between 2004 and 2015;
-- (5)와 (6)의 결과는 같음.

-- (7) 책 발간 년도가 2004년과 2015년 사이에 발간된 책이 아닌 것들만 책 제목을 조회하시오.
select title
from books
where released_year not between 2004 and 2015;
-- 이걸 between 없이 쓰면
select title
from books
where released_year < 2004 or released_year > 2015;

-- (8) people2 테이블에서 birthdt가 1990년부터 2010년 사이에 태어난 사람의 이름과 birthdt를 조회하시오.
-- 특정 날짜 기간을 조회하는 방법
-- 방법1
select name, birthdt
from people2
where birthdt between "1990-01-01" and "2010-12-31";
-- 방법2 : year()로 년도만 뽑아와서 비교하기
select name, birthdt
from people2
where year(birthdt) between "1990" and "2010";
-- 방법3 : 캐스팅(데이터 타입 바꾸기) 문자열을 데이트타임 데이터타입으로 바꿈.
select name, birthdt
from people2
where birthdt between cast("1990-01-01" as datetime)
			  and cast("2010-12-31" as datetime);
-- 그냥 따옴표와 cast한 데이터의 차이
select "1990-01-01";
select cast("1990-01-01" as datetime);



-- 예제 6개 (깃헙 DB 실습문제 3)
-- (9) 1. 작가가 Eggers 또는 Chabon이 쓴 책의 모든 컬럼을 조회하시오.
select *
from books
where author_lname = "Eggers" or author_lname = "Chabon";

-- (10) 2. 1980년 이전에 쓴 책 중 pages 수가 500페이지 이하인 책들의 책 제목과 페이지 수를 조회하세요.
select title, pages
from books
where released_year <= 1980 and pages <= 500;
-- released_year < 1980 (1980년도가 표함되면 안 됨)
-- a 중 b => and. a에 있는 조건에서 b를 찾음. a와 b에 둘 다 해당되는 조건 찾기는 and이므로 and를 써야함.

-- (11) 3. 작가가 Eggers와 Lahiri인 사람이 쓴 책의 총 페이지 수는 몇 페이지인지, 총 페이지 수를 조회하세요.
select sum(pages)
from books
where author_lname = "Eggers" or author_lname = "Lahiri";

-- (12) 4. stock_quantityrk 60에서 100 사이의 책들 중에서 발간년도가 가장 오래된 책은 몇년도인지 조회하세요.
-- ((  방법1  ))  min() & between 사용
select min(released_year)
from books
where stock_quantity between 60 and 100;
-- ((  방법2  ))  order by & limit 사용
select released_year
from books
where stock_quantity between 60 and 100
order by released_year asc
limit 1;

-- (13) 5. 책 제목이 30글자 이상인 책들에 대해서, 책의 갯수와 stock_quantity의 총합을 조회하세요.
-- ((  1  ))  내가 풀은 거
select count(title), sum(stock_quantity)
from books
where char_length(title) >= 30; -- 공백 포함인지?? ㅇㅇ 자바든 어디서든 문자열의 길이는 공백 포함.
-- ((  2  ))  강사님이 풀은 거
select count(*), sum(stock_quantity)
from books
where char_length(title) >= 30;
-- 행(데이터)의 총 개수를 구할 때는 count(*)!!!!!!!!!!!!!

-- (14) 6. 각 년도별로, 해당 년도에 몇 권이 발행됐는지의 책 갯수와 책 페이지의 총합을 조회하시오.
select released_year as "year", count(*) as cnt, sum(pages) as pages
from books
group by released_year;
-- 이전에는 count(*)를 count(title)로 썼었음.
-- ★★★ group by하고 count하면, group by한 것을 count하라는 이야기이다. sum도 마찬가지임.



-- ★★★ in / not in : 여러개 중에 포함되어 있는 것들을 가져올 때 / 가져오지 않을 때

-- (15) Carver, Lahiri, Smith의 책 제목과 이름을 조회하시오.
select title, author_lname
from books
where author_lname in ("Carver", "Lahiri", "Smith");

-- (16) 2003, 2016, 1996년도에 출간한 책의 제목과 작가 이름을 조회하시오.
select title, author_lname
from books
where released_year in (2003, 2016, 1996);

-- (17) 2003, 2016, 1996년도가 아닌 책의 제목과 작가 이름을 조회하시오.
select title, author_lname
from books
where released_year not in (2003, 2016, 1996);

-- (18) ★ 2003, 2016, 1996년도가 아닌 책 중에서 2005년 이후에 출간된 책의 제목과 작가 이름을 조회하시오.
select title, author_lname
from books
where released_year not in (2003, 2016, 1996) and released_year >= 2005;
-- good






-- 테이블 설계 : 복수의 연결된 테이블 만들기(primary key를 포함하는 테이블)
-- 데이터 관리는 각각 하되, 필요하면 원할 때 언제든지 합칠 수 있는 테이블들을 설계.
-- 여러 테이블을 설계할 때, 연결고리가 있는 경우.
use mydb;
-- ((24)) students table, papers table 만들기
create table students (
id int not null auto_increment primary key,
first_name varchar(100)
);
create table papers (
id int not null auto_increment primary key,
title varchar(100),
grade int,
student_id int,
-- 자동으로 스튜던트 테이블의 컬럼과 연결하기 foreign key() : ()안에 적힌 컬럼은 references 뒤에 적힌
-- 테이블(컬럼)과 연결되어 있다는 뜻이다.
foreign key (student_id) references students(id)
		on delete cascade
);
-- 만약에 students 테이블에서 데이터가 삭제(delete)될 경우, papers 테이블에 해당 id값이 있으면, 같이 삭제하도록
-- 할 수 있는 문법은 "on delete cascade". foreign key 맨 뒤에 붙인다.
-- 폴인 키로 연결하지 않아도 조인할 수 있지 않나..? 무슨 차이가 있을까.

-- ((25)) 테이블 데이터 추가하기 : 깃헙 - OS_DB - 16_foreign_table_insert.sql
INSERT INTO students (first_name) VALUES 
('Caleb'), ('Samantha'), ('Raj'), ('Carlos'), ('Lisa');

INSERT INTO papers (student_id, title, grade ) VALUES
(1, 'My First Book Report', 60),
(1, 'My Second Book Report', 75),
(2, 'Russian Lit Through The Ages', 94),
(2, 'De Montaigne and The Art of The Essay', 98),
(4, 'Borges and Magical Realism', 89);
-- student 전에 papers 데이터 추가하려면 오류남.

-- =====  DB 실습문제 5 with students, papers  =====

-- ((26)) 1. print this : 이름, 제목, 등급을 조회하되, 등급을 내림차순으로 조회.
select s.first_name, p.title, p.grade
from students as s
join papers as p
on s.id = p.student_id
order by p.grade desc;
-- good~

-- ((27)) 2. print this : ★left join★
select s.first_name, p.title, p.grade
from students as s
left join papers as p
on s.id = p.student_id
order by s.id asc, p.grade asc;
-- gooood

-- ((28)) 3. print this : ifnull()
select s.first_name, ifnull(p.title, "MISSING") as title, ifnull(p.grade, 0) as grade
from students as s
left join papers as p
on s.id = p.student_id
order by s.id asc, p.grade asc;

-- ((29)) 4. print this : left join, group by, order by, avg(), ifnull()
select s.first_name, ifnull(avg(p.grade), 0) as average
from students as s
left join papers as p
on s.id = p.student_id
group by s.first_name
order by average desc;
-- ifnull과 avg 바뀌어도 됨. 강사님은 바꿔서 적으셨음.
-- order by 원래는 p.grade로 적었었는데 그러면 안됨.

-- ((30)) 5. print this (가르쳐주시는 거) : ★case statements★, if()
-- 홀 if()로 해결했숴
select s.first_name, ifnull(avg(p.grade), 0) as average,
if(avg(p.grade) >= 70, "PASSING", "FAILING") as passing_status
from students as s
left join papers as p
on s.id = p.student_id
group by s.first_name
order by average desc;
-- case statements로 해결하기
select s.first_name, ifnull(avg(p.grade), 0) as average,
	case
		when ifnull(avg(p.grade), 0) >= 70 then "PASSING"
        else "FAILING"
	end as passing_status
from students as s
left join papers as p
on s.id = p.student_id
group by s.first_name
order by average desc;





-- ★★★★★ case statements ★★★★★

-- case when then else end as
-- when 옆에 조건식 then 그려면 이거해라 (when은 계속 추가 가능)
-- else 아니면 이거해라 (else는 없어도 됨)

-- ((31)) 예시1 (books table)
select title, released_year,
	case
		when released_year >= 2000 then "Modern"
        else "20th Centry"
	end as genre
from books;
-- 테이블 안에 컬럼을 새로 만드는 것이 아니라 조회용 컬럼을 만드는 것임.

-- ((32)) 예시2 (books table)
select title, stock_quantity,
	case
		when stock_quantity between 0 and 50 then "*"
        when stock_quantity between 51 and 100 then "**"
        else "***"
	end as stock
from books;

-- ((33)) 예시3 (book table)
select title, stock_quantity,
	case
		when stock_quantity <= 50 then "*"
        when stock_quantity <= 100 then "**"
        else "***"
	end as stock
from books;
-- 예시 2와 같은 결과임.





-- 테이블 설계 - 예제 
-- 테이블 이름 : Reviewers / Series / Reviews
-- Reviewers : id(int, primary key) / first_name(varchar) / last_name(varchar)
-- Series : id(int, primary key) / title(varchar) / released_year(int) / genre(varchar)
-- Reviews : id(int, primary key) / rating(int) / series_id(int, primary key) / reviewer_id(int, primary key)

-- ((34-1)) 테이블 만들기
create table Reviewers (
id int not null auto_increment primary key,
first_name varchar(100) not null,
last_name varchar(100) not null
);
create table Series (
id int not null auto_increment primary key,
title varchar(100),
released_year year(4), -- 4자리수의 년도를 넣는다는 뜻.
genre varchar(100)
);
-- reviews 먼저 만들려고 했었음.. 연결되는 건 마지막에 만들어라. 연결되어야 할 테이블이랑 컬럼이 먼저 만들어져 있어야 하니까.
create table Reviews (
id int not null auto_increment primary key,
rating float not null,
-- ★decimal(2,1) 숫자 두개, 소수점 한자리를 넣는다는 뜻. float이나 double을 써도 됨. 근데 강사님은 실무에서 float을 쓰신댔음.
series_id int,
reviewers_id int,
foreign key (series_id) references Series(id),
foreign key (reviewers_id) references Reviewers(id)
);
-- ★★★ 폴인 키 - 레퍼런스 옆에 있는 테이블.(컬럼) 쓸 때 대소문자 가림. 핵주의.★★★
-- ★★★ 폴인 키에 일부러 on delete cascade 안 넣고 만드심. 차이 비교를 위해서.
-- 테이블 만들고 나서는 알터 테이블의 폴인 키 탭으로 가서 폴인 키 설정들을 바꿀 수 있다.

-- ((35)) 폴인 키에 on delete cascade를 추가하고 삭제했을 경우
-- students, papers
desc students;
desc papers;
-- foreign key는 papers에 걸려있다.
select * from students;
select * from papers;
-- 1번 학생을 지워본다
delete from students where id = 1;
-- papers에도 1번 학생과 관련된 자료"들"이 전부 지워졌다.
-- ★★★ on delete cascade를 넣어야 할지 말아야 할지는 기획, 정책에 달려있다.
-- ex) 학생이 전학가면 관련 리포트 자료를 지운다. 회원이 탈퇴해도 리뷰는 남긴다.

-- ((34-2)) reviews 관련 테이블들 데이터 추가하기 (깃헙에 있음)
INSERT INTO Series (title, released_year, genre) VALUES
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
  
INSERT INTO Reviewers (first_name, last_name) VALUES
   ('Thomas', 'Stoneman'),
   ('Wyatt', 'Skaggs'),
   ('Kimbra', 'Masters'),
   ('Domingo', 'Cortes'),
   ('Colt', 'Steele'),
   ('Pinkie', 'Petit'),
   ('Marlon', 'Crafford');
   
INSERT INTO Reviews(series_id, reviewer_id, rating) VALUES
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
   
-- ((36)) 예제 : 티비 쇼 제목과 별점을 조회하시오.
select s.title, r.rating
from Reviews as r
join Series as s
on s.id = r.series_id;

-- ((37)) 예제 : 티비 쇼의 제목별, 별점 평균을 조회하시오.
select s.title, avg(r.rating) as average
from Reviews as r
join Series as s
on s.id = r.series_id
group by s.title;

-- ((38)) 예제 : 리뷰어의 이름(2가지 다)과 그 사람이 준 별점을 조회하세요.
select concat(rr.first_name, " ", rr.last_name) as name, rv.rating
from Reviewers as rr
join Reviews as rv
on rr.id = rv.reviewer_id;

-- ((39)) 예제 : 티비쇼 중에서 별점이 없는 것들을 제목으로 조회하시오.
select s.title
from Series as s
left join Reviews as r
on s.id = r.reviewer_id -- r.series_id??????
where r.rating is null;
-- series_id 가 맞음.. series 테이블을 가져오는데 필요없는 reviewer_id랑 연결하지 마세오..

-- ((40)) 예제 : 각 장르별로, 장르와 장르의 별점 평균을 조회하세요.
select s.genre, round(avg(r.rating), 2) as average
from Series as s
join Reviews as r
on s.id = r.reviewer_id
group by s.genre;
-- ★★★ round(a, b) b는 소수점 아래에 보여줄 자릿수(int)이다.