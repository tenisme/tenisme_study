-- (1) 회원 중에서, 가입한지 가장 오래된 회원들 5명을 조회하시오.
select *
from users
order by created_at asc
limit 5;

-- (2) 회원 가입이 가장 많이 일어난 요일, 그 요일과 가입한 회원 수를 조회하시오.
-- keyword : ★dayname(), date(), group by, order by, limit
select dayname(created_at) as day, count(*) as total
from users
group by day
order by total desc
limit 1;
-- =====
-- dayname(A) "날짜에서 영문 요일명로 바꿔 가져온다"
-- date(A) "날짜를 YYYY-MM-DD로 가져온다"
-- 그룹해야하는 것과 정렬해야 하는 것이 무엇인지를 잘 생각해내자.
-- 그리고 별명(as) 활용도 잘 하자.

-- (3) 회원들 중에서, 사진이 없는(사진을 안 올림) 회원들의 이름을 조회하시오.
-- keyword : left join, is null
select u.username, p.image_url
from users as u
left join photos as p
on u.id = p.user_id
where P.image_url is null;
-- =====
-- 실무에서는 "활동을 안하는 회원"을 inactive user라고 부른다. active user는 "활동을 하는 유저"
-- 컬럼 앞에 테이블 이름 좀 잘 붙여주셈.

-- (4) 가장 좋아요(인기가 많은)를 많이 받은 사진의 사진 작성자, 사진 url, 좋아요 수를 조회하시오.
-- 셀프 작성
select u.username, p.image_url, count(*) as likes
from users as u
join photos as p
on u.id = p.user_id
join likes as l
on u.id = l.user_id
group by l.user_id
order by likes desc
limit 1;
-- 강사님 답변
select u.username, p.image_url, count(*) as likes -- count(p.id)도 가능.
from photos as p
join likes as l
on l.photo_id = p.id
join users as u
on p.user_id = u.id
group by p.id -- l.photo_id도 가능.
order by likes desc
limit 1;
-- 사진에 눌러진 좋아요 갯수를 세야하므로 "사진"이 중심이 되어야 하고
-- 그룹으로 묶는 것은 "사진"의 아이디여야 한다. 나는 연결도 유저 테이블 위주로 하고 그룹바이도 유저 아이디로 했다..
-- 원하는 데이터를 조회하기 위해서는 무슨 값을 얻어야 하고 그 값을 얻기 위해서는 무슨 테이블(들)이 필요하고
-- 뭘 join하고 뭘 group by해야하는지 어떻게 order by해야하는지 등등을 잘 생각해야한다. 이게 안 되는 거 같다.

