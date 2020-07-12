use sakila;

-- DB 실습문제 4
-- Join

-- ((19)) 1. 각 영화별로, 등장한 배우가 몇 명인지, 영화 제목과 배우 수를 조회하시오.
-- 내가 쓴 거
select f.title, count(a.first_name)
from film_actor as fa
join film as f
on fa.film_id = f.film_id
join actor as a
on fa.actor_id = a.actor_id
group by f.title;
-- 강사님 풀이 ??? 다시 보고 이해하기
select f.title, count(fa.film_id)
from film as f
join film_actor as fa
on fa.film_id = f.film_id
group by f.title;
-- 결과는 같음

-- ((20)) 2. Hunchback Impossible 이라는 제목의 영화는, inventory 테이블에 몇개가 존재하는지,
-- 영화제목과 숫자를 조회하시오.
select f.title, count(inv.film_id)
from inventory as inv
join film as f
on inv.film_id = f.film_id
where f.title = "HUNCHBACK IMPOSSIBLE";
-- 강사님
select f.title, count(*) as cnt
from film as f
join inventory as inv
on inv.film_id = f.film_id
where f.title = "HUNCHBACK IMPOSSIBLE";
-- 결과는 같음. 그치만 count(*)가 더 나음.

-- ((21)) 3. 고객별로 영화를 보는데 얼마를 썼는지(amount), 각 고객의 이름과 해당 금액(amount) 을 조회하시오.
-- (amount가 없으면 0으로 계산)
-- 내 풀이
select c.last_name, sum(p.amount)
from payment as p
join customer as c
on p.customer_id = c.customer_id
group by c.customer_id;
-- 강사님 풀이
select c.first_name, c.last_name, sum(ifnull(p.amount,0)) as amount
from customer as c 
join payment as p
on c.customer_id = p.customer_id
group by c.first_name, c.last_name;
-- 결과는 이름 부분 하나 더 추가된 거 말고는 같음. ★★★★★ifnull()!!!!!!!!!!!!!!!!!!!!

-- ((22)) 4. 캐나다 사람 대상으로 이메일 마케팅 하려한다. 모든 캐나다 고객의 이름과 이메일을 조회하시오.
-- (이메일 없으면 없다고 표시) >> 실무와 가까운 문제.
-- 내 풀이(이메일 없으면 없다고 표시를 못했네.. 아래는 ifnull 넣어서 수정한 것임.)
select cu.last_name as "name", ifnull(cu.email, "no email") as email
from customer as cu
join address as ad
on cu.address_id = ad.address_id 
join city as ci
on ad.city_id = ci.city_id
join country as co
on ci.country_id = co.country_id
where co.country = "canada";
-- 강사님 풀이
select c.first_name, c.last_name, ifnull(c.email, "No Email") as email
from customer as c
join address as a
on c.address_id = a.address_id
join city as ct
on a.city_id = ct.city_id
join country as co
on ct.country_id = co.country_id
where co.country = "canada";

-- ((23)) 5. 가족영화만 보고싶다. 따라서 카테고리가 “Family”인 영화의 영화 제목을 조회하시오.
-- 내 풀이
select fi.title as title
from film_category as fc
join category as ca
on fc.category_id = ca.category_id
join film as fi
on fc.film_id = fi.film_id
where ca.category_id = 8;
-- 강사님 풀이
select f.title
from film as f 
join film_category as fc
on f.film_id = fc.film_id
join category as ca
on fc.category_id = ca.category_id
where ca.name = "Family";
-- 판떼기(table) 붙여놓고(join) 조건(where) 달기.