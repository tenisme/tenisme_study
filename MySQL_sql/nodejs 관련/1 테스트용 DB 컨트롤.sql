-- 200714 화요일 테스트 : nodejs로 파싱한 json을 nodejs로 aws DB table에 insert하기
use insta;

create table employee (
	id int not null auto_increment primary key,
    employee_name varchar(100),
    employee_age int,
    employee_salary int
);

drop table employee;

select * from employee;

