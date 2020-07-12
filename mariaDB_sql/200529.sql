use mydb;

-- ((1)) 예제 1 : 리뷰 작성을 안 한 사람도 표시하되(0으로), 사람별로 각 리뷰의 갯수, 리뷰의 최소 별점, 최대 별점, 평균 별점(rating) 조회하고,
-- 만약 별점을 준 리뷰 갯수가 0보다 크면 "ACTIVE", 그렇지 않으면 "INACTIVE"로 표시(컬럼 명은 status)
-- firstname, lastname, count, min, max, avg, status

-- ifnull(), count(*), as, round(), min(), max(), case-when-then-else-end as, left join-on, group by, order by
-- ((  1  ))  내 풀이(계속 변경함)
select r.first_name, r.last_name, count(rv.rating) as COUNT, round(min(ifnull(rv.rating, 0)), 2) as MIN,
round(max(ifnull(rv.rating, 0)), 2) as MAX, round(avg(ifnull(rv.rating, 0)), 2) as "AVG",
	case
		when count(rv.rating) > 0 then "ACTIVE"
        else "INACTIVE"
        end as "STATUS"
from Reviewers as r
left join Reviews as rv
on r.id = rv.reviewer_id
group by r.first_name, r.last_name
order by COUNT, MIN, MAX, AVG;
-- 결국 입맛에 맞게 고쳤당
-- 조인하고 갯수 셀 때의 예시
-- ((  2  ))  강사님 풀이
select r.first_name, r.last_name, count(*) as COUNT, min(rv.rating) as MIN,
max(rv.rating) as MAX, round(avg(rv.rating), 2) as AVG, if(count(*) > 0, "ACTIVE", "INACTIVE") as STATUS
from Reviewers as r
join Reviews as rv
on r.id = rv.reviewer_id
group by r.id
order by COUNT, MIN, MAX, AVG;
-- ★ 조인 한 다음에 그룹바이.
-- 조건이 단 두개(true or false)면 if로 쓰는 게 편하고, 조건이 여러개(if, else if)면 case가 좋다.
-- if()도 else if를 추가해 쓸 수 있지만 복잡하기 때문에 그냥 위와 같이 활용한다.
-- ★ 강사님 거 고쳐졌음. 고친 거 올려놓으신댔음. (깃헙 19번)
-- 아래 것도 대입 가능(다수 조건)
--    case
-- 		when count(rv.rating) >= 10 then "POWER USER"
-- 		when count(rv.rating) > 0 then "ACTIVE"
--         else "INACTIVE"
--         end as "STATUS"

-- ((2)) 예제 2. 영화 제목으로 알파벳 오름차순 정렬해서, 영화 제목, 별점, 리뷰 작성자 이름(합쳐서)을 조회하시오.
-- round(), concat(), as, join-on, order by
select s.title, round(rv.rating, 1) as rating, concat(r.first_name, " ",r.last_name) as reviewer
from Reviews as rv
join Reviewers as r
on rv.reviewer_id = r.id
join Series as s
on rv.series_id = s.id
order by s.title;

