use emp_test;

select e.ename, e.job, e.deptno, d.dname
from emp as e
Join dept as d
on e.deptno = d.deptno
where d.loc = "DALLAS";

select e.ename, d.dname
from emp as e
Join dept as d
on e.deptno = d.deptno
where ename like "%A%";

select e.ename, d.dname, e.sal
from emp as e
Join dept as d
on e.deptno = d.deptno
where e.sal >= 3000;

select e.job, e.ename, d.dname
from emp as e
Join dept as d
on e.deptno = d.deptno
where e.job = "SALESMAN";

select e.empno as "사원번호", e.ename as "사원이름", e.sal as "연봉",
e.sal+e.comm as "실급여", s.grade as "급여등급"
from emp as e
Join salgrade as s
on e.sal between s.losal and s.hisal
where e.comm is not NULL
and e.comm != 0;

select e.empno as "사원번호", e.ename as "사원이름", e.sal as "연봉",
e.sal + ifnull(e.comm, 0) as "실급여", s.grade as "급여등급"
from emp as e
Join salgrade as s
on e.sal between s.losal and s.hisal;

select e.deptno, d.dname, e.ename, e.sal, s.grade
from emp as e
Join dept as d
on e.deptno = d.deptno
Join salgrade as s 
on e.sal between s.losal and hisal
where e.deptno = 10;

select e.deptno, d.dname, e.ename, e.sal, s.grade
from emp as e
Join dept as d
on e.deptno = d.deptno
Join salgrade as s
on e.sal between s.losal and s.hisal
where e.deptno in (10, 20, 30)
order by e.deptno asc, e.sal desc;