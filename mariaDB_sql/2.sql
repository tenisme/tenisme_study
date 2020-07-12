select * from dept;
select * from emp;
select * from salgrade;
select * from emp order by ENAME;
select * from emp order by SAL desc;
select ENAME, SAL, DEPTNO from emp order by SAL desc;
select ENAME, SAL, DEPTNO from emp order by DEPTNO, SAL desc;
select ENAME, HIREDATE, DEPTNO from emp order by DEPTNO asc, HIREDATE asc;
select distinct JOB from emp;
select * from emp group by JOB;
select ENAME as 이름, JOB as 직급, SAL as 급여 from emp order by 직급 asc, 급여 desc;
select * from emp where SAL > 1000 and SAL < 4000 order by SAL desc;
select * from emp where SAL <= 1000 or SAL >= 4000;
select * from emp where JOB = "ANALYST" or JOB = "MANAGER" order by ENAME;
select * from emp where DEPTNO = 30;
select distinct DEPTNO from emp;
select * from emp where DEPTNO = 10 or DEPTNO = 20 or DEPTNO = 30;
select * from emp where SAL >= 3000 and SAL <= 5000;
select EMPNO, ENAME, COMM from emp where comm != 300 && comm != 500 && comm != 1400;
select EMPNO, ENAME, COMM from emp where comm not in (300,500,1400);
select count(*) from emp;
select distinct job from emp;
select * from emp where ENAME = "King";
select empno, ename from emp where ename like "S%";
select EMPNO, ENAME from emp where ENAME like "%T%";
select ENAME, EMPNO, JOB, DEPTNO from emp where JOB = "MANAGER" && DEPTNO = 30;
select EMPNO, ENAME, DEPTNO from emp where DEPTNO != 30;
select empno, ename, deptno from emp where not deptno = 30;
select empno, ename from emp where ENAME not like "%S%";
select ENAME, JOB from emp where MGR is NULL;
select avg(SAL), DEPTNO from emp group by DEPTNO;

select avg(SAL), DEPTNO from emp where SAL >= 2000 group by DEPTNO;
select deptno, count(*), count(comm) from emp group by deptno;
select deptno as "부서", count(*) as "전체 사원수", count(comm) as "커미션 사원수 " from emp group by deptno;
select DEPTNO, max(SAL), min(SAL), sum(sal) from emp group by DEPTNO;
select empno, ename, sal from emp order by sal desc, ename asc;

select emp.ename, dept.dname from emp, dept;
select emp.*, dept.* from emp, dept;

select * from salgrade;
select * from emp;
select * from dept;

