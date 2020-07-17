-- 깃헙 node 실습 7
-- memo를 저장할 테이블을 구성 : memos
-- id, title, content, created_at 컬럼을 생성
use my_test;
create table memos (
	id int not null auto_increment primary key,
    title varchar(300),
    content varchar(2000),
    created_at datetime default now(),
    updated_at timestamp default now() on update current_timestamp
);

drop table memos;

select * from memos;

insert into memos (title, content) values ("title","content");

update memos set title = "hey", content = "yeah" where id = 1;

delete from memos where id = 1;