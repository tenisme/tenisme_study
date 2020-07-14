-- 200713 월요일 실습
create table students (
	id int not null auto_increment primary key,
    first_name varchar(100)
);

create table papers (
	title varchar(100),
    grade int,
    student_id int,
    foreign key (student_id) references students(id)
);

insert into students (first_name)
	values ('Caleb'), ('Samantha'), ('Raj'), ('Carlos'), ('Lisa');

insert into papers (student_id, title, grade) values
	(1, 'My First Book Report', 60),
    (1, 'My Second Book Report', 75),
    (2, 'Russian Lit Through The Ages', 94),
    (2, 'De Montaigne and The Art of The Essay', 98),
    (4, 'Borges and Megical Realism', 89);

select * from students;
select * from papers;
select * from students as s left join papers as p on s.id = p.student_id;

-- 실습문제 1
select s.first_name as first_name, p.title as title, p.grade as grade
from papers as p join students as s on s.id = p.student_id order by grade desc;
-- 실습문제 2
select s.first_name as first_name, ifnull(p.title, "NULL") as title, ifnull(p.grade, "NULL") as grade
from students as s left join papers as p on s.id = p.student_id order by s.id;
-- 실습문제 3 ★ 중요 문법 : ifnull() 실무에서 많이 씀★
select s.first_name as first_name, ifnull(p.title, "MISSING") as title, ifnull(p.grade, "0") as grade
from students as s left join papers as p on s.id = p.student_id order by s.id;
-- 실습문제 4
select s.first_name as first_name, ifnull(avg(p.grade), 0) as average 
from students as s left join papers as p on s.id = p.student_id group by s.id order by average desc;
-- 실습문제 5
select s.first_name as first_name, ifnull(avg(p.grade), 0) as average,
	case
		when ifnull(avg(p.grade), 0) >= 70 then "PASSING"
        else "FAILING"
	end as passing_status
from students as s left join papers as p on s.id = p.student_id group by s.id order by average desc;