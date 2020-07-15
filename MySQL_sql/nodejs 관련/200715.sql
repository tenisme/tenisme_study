-- 인덱스 설명용 예시
-- 인덱스 : 바로 찾아갈 수 있게 만들어 놓은 것
select * from shirts
where color = 'yellow' and last_worn = 178;
-- 위 두개를 동시에 인덱스로 만들어 놓으려고 한다.
select count(distinct color) from shirts;
select count(distinct last_worn) from shirts;
-- 위 select 결과 분포도는 last_worn가 훨씬 좋으므로 last_worn를 인덱스의 첫번째 순서로 놓는다.

-- 실습 : 유튜브에서 내가 좋아하는 정보를 검색해 디비에 저장하기
create table youtube (
	id int not null auto_increment primary key,
    channelTitle varchar(300),
    title varchar(300),
    publishedAt datetime,
    videoId varchar(100)
);

select * from youtube;