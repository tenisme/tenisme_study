# todo-server (test)

use my_test;

create table todo (
	id int not null auto_increment primary key,
    title varchar(300),
    date timestamp,
    completed boolean
);

drop table todo;
truncate table todo;

select * from todo limit 0, 25;

select id, completed from todo where id = 1;
update todo set completed = 0 where id = 1;

select * from todo;