use mydb;
create database mydb;

-- Schema
-- students table, papers table
create table students (
	id int not null auto_increment primary key,
    first_name varchar(100)
);
create table papers (
	id int not null auto_increment primary key,
    title varchar(100),
    grade int,
    student_id int,
    foreign key (student_id) references students(id) 
);
-- 만약에 students 테이블에서 데이터가 삭제(delete)될 경우에,
-- papers 테이블에 해당 id 값이 있으면, 같이 삭제하도록 할 수 있다.
-- 그 문법은 ?  on delete cascade 
create table papers (
	id int not null auto_increment primary key,
    title varchar(100),
    grade int,
    student_id int,
    foreign key (student_id) references students(id) 
		on delete cascade
);

select * from students;
select * from papers;

INSERT INTO students (first_name) VALUES 
('Caleb'), ('Samantha'), ('Raj'), ('Carlos'), ('Lisa');

INSERT INTO papers (student_id, title, grade ) VALUES
(1, 'My First Book Report', 60),
(1, 'My Second Book Report', 75),
(2, 'Russian Lit Through The Ages', 94),
(2, 'De Montaigne and The Art of The Essay', 98),
(4, 'Borges and Magical Realism', 89);


-- ==================================================


-- 1. 이름, 제목, 등급을 조회하되, 등급 내림차순으로.
select s.first_name, p.title, p.grade
from students as s
join papers as p
on s.id = p.student_id
order by p.grade desc;



-- 2. left join 조인
select s.first_name, p.title, p.grade
from students as s
left join papers as p
on s.id = p.student_id;



-- 3. Null 로 되어있는 데이터를 처리.
select s.first_name, ifnull(p.title, "MISSING") , 
		ifnull(	p.grade , 0 )
from students as s
left join papers as p
on s.id = p.student_id;



-- 4. 사람별 그레이드 평균을 구하고, 평균 내림차순으로 정렬.
select s.first_name, avg(  ifnull(p.grade, 0)  )
from students as s
left join papers as p
on s.id = p.student_id
group by s.first_name;

-- case statements 
select title, released_year,
	case 
		when released_year >= 2000 then "Modern"
        else "20th Century"
	end as genre
from books;

select title, stock_quantity,
	case
		when stock_quantity between 0 and 50 then "*"
        when stock_quantity between 51 and 100 then "**"
        else "***"
	end as stock
from books;

select title, stock_quantity,
	case 
		when stock_quantity <= 50 then "*"
        when stock_quantity <= 100 then "**"
        else "***"
	end as stock
from books;



-- 5. average 가 70점 이상이면 PASSING, 아니면 FAILING
select s.first_name, avg(ifnull(p.grade, 0)) as average,
	case
		when avg(ifnull(p.grade, 0)) >= 70 then "PASSING"
        else "FAILING"
    end as passiing_status
from students as s
left join papers as p
on s.id = p.student_id
group by s.first_name
order by average desc;

-- students , papers
desc students;
desc papers;

select * from students;
select * from papers;
delete from students where id = 1;