create database shirts_db;

use shirts_db;

create table shirts (
shirt_id int not null auto_increment primary key,
article varchar(30),
color varchar(30),
shirt_size varchar(1),
last_wom int
);

desc shirts;

insert into shirts (article, color, shirt_size,last_worn)
values ('t-shirt', 'white', 'S', 10),
		('t-shirt', 'green', 'S', 200),
        ('polo shirt', 'black' , 'M', 0),
        ('tank top', 'blue', 'S', 50),
        ('t-shirt', 'pink', 'S', 0),
        ('polo shirt', 'red', 'M', 5),
        ('tank top', 'white', 'S', 200),
        ('tank top', 'blue', 'M', 15);
        
select * from shirts;

select article, color from shirts;

select article, color, shirt_size, last_worn
from shirts
where shirt_size = "M";

update shirts
set shirt_size = "L"
where article = "polo shirt";

select * from shirts where article = "polo shirt";

update shirts
set last_worn = 10
where last_worn = 15;

update shirts
set color = "off white", shirt_size = "XS"
where color = "white";

delete from shirts
where last_worn = 200;

select * from shirts;

drop table shirts;


delete from shirts
where article = "tank top";

show tables;
