use track_me;

# 연결 안 되는 낱개 테이블들부터 순서대로 생성

# 일별 기록 저장 테이블
create table daily_record (
	daily_record_id int not null auto_increment primary key,
    day date not null,
    feeling_icon int,
    note varchar(2000)
);
-- daily_record와 record_time을 붙일 땐(하루의 기록을 가져올 땐) daily_record의 day와 record_time의 시작 시간, 종료 시간을 between..?으로 비교해서 붙인다.

# 시작~종료 시간 기록 테이블
create table record_time (
	record_id int not null auto_increment primary key,
    start_time timestamp not null default now(),
    finish_time timestamp default now() on update now()
);

# 활동(+활동아이콘) 테이블
create table activity (
	activity_id int not null auto_increment primary key,
    name varchar(100) not null,
    icon int not null,
    bg_color int not null,
    unique(name, icon, bg_color)
);
-- icon : 안드 이미지 파일 id

# 태그 테이블
create table tag (
	tag_id int not null auto_increment primary key,
	tag varchar(100) not null,
    unique(tag)
);

# 활동별 자동 태그 셋팅 테이블
create table tag_in_activity (
	t_i_a_id int not null auto_increment primary key,
	activity_id int not null,
    tag_id int not null
);
-- 활동별 셋팅한 태그가 없어도 null로 insert해주기..?

# 한 기록당 여러 활동을 연결할 수 있도록 만든 테이블
create table record_activity (
	r_a_id int not null auto_increment primary key,
    record_id int not null,
    activity_id int not null,
    note varchar(1000),
    unique(record_id, activity_id)
);
-- note : '기록+활동'별 노트는 하나씩만 생성 가능. 메모는 없을 수 있으므로 null 가능

# 한 '기록+활동'별 여러 태그를 연결할 수 있도록 만든 테이블
create table tag_in_r_a (
	r_a_tag_id int not null auto_increment primary key,
    r_a_id int not null,
    tag_id int not null
);
-- tag_id : 한 기록+활동에 같은 태그를 여러번 써도 되도록 유니크 설정은 하지 않음
-- 			+ 태그가 없을 수 있으므로 null 가능

create table button_seat (
	button_seat_id int not null auto_increment primary key,
    layout_id int not null,
    activity_id int default 0
);

insert into button_arr (button_id) values (1),(2),(3),(4),(5),(6),(7),(8),(9);

drop table daily_note;
drop table activity;
drop table tag_in_r_a;
drop table time_log;
drop table button_seat;

# 1. 시간 기록을 위해서는 일단 활동이 있어야 함.
insert into activity (name, icon, bg_color) values 
("study", 1, 10), ("sleep", 2, 11), ("write", 3, 10), ("game", 4, 12), ("youtube", 5, 13);

# 2. 태그는 시간 기록 이후에 지정해줘도 되지만 특정 활동에 기본적으로 붙는 태그가 있을 수 있음. 활동 생성시 지정해줄 수도 있기 때문에 활동 다음으로 배치했음.
insert into tag (tag) values ("#write_novel"), ("#crazy"), ("#want_to_sleep"), ("#slept_refreshingly"), ("#hungry");

# 3. 활동에 기본적으로 따라붙는 태그가 있다면 셋팅
insert into tag_in_activity (activity_id, tag_id) values (1, 1), (1, 3), (3, 3), (4, 2), (5, 2);
# 활동에 다는 기본 태그 변경시 수정 방법 고민
# 1 : 기존 태그 싹 다 밀고(delete) 다시 전부 새로 insert하는 방법
# 2 : 기존 태그와 나중 태그를 비교해 새롭게 추가된 것이 있으면 추가(insert), 없어진 것이 있으면 삭제(delete), 나중 태그에 기존 태그와 같은 것이 있으면 건들지 않는 방법.
delete from tag_in_activity where activity_id = 1 and tag_id = 1;

# 4. 타이머의 시작 시간, 종료 시간을 기록함. 시작은 insert, 종료는 update ~ set
# 타이머 시작
insert into record_time (start_time, finish_time) values (default, default);
# 타이머 종료
update record_time set finish_time = default where record_id = 5;

# 5. 시간 기록과 행동(들)을 합침. 
# 실제로 이 테이블에 시간 기록과 행동을 insert할 때 note는 아래처럼 한번에 같이 추가하지 않음. note는 update ~ set 전용임. 기본 셋팅 메모 기능은 없음.
insert into record_activity (record_id, activity_id, note) values 
(1, 1, null), (1, 3, "wrting a thesis"), (2, 5, "Watch cute cat video"), (3, 4, "play lol"), (4, 2, null);
# update ~ set note = "" where ~ ;
update record_activity set note = "" where r_a_id = 5;

# 6. '기록 시간+행동(record_activity)'별 태그 작성/수정/삭제
# 그 다음 '로그 수정' 액티비티에서 하는 작업
insert into tag_in_r_a (r_a_id, tag_id) values (1, 8), (2, 3), (2, 10), (4, 7), (5, 9);
# 이것도 삭제/수정 고민이 4번과 같음.

# 7. '로그 조회'에 사용할 데이터 조회
select ra.record_id, a.icon, a.bg_color, a.name, rt.start_time, rt.finish_time, 
timediff(rt.finish_time, rt.start_time) as record, tg.tag, t.tag, ra.note 
from record_activity as ra 
left join tag_in_r_a as ti on ra.r_a_id = ti.r_a_id
left join tag as t on ti.tag_id = t.tag_id 
left join record_time as rt on ra.record_id = rt.record_id
left join activity as a on ra.activity_id = a.activity_id
left join tag_in_activity as tia on a.activity_id = tia.activity_id
left join tag as tg on tia.tag_id = tg.tag_id
order by rt.start_time asc;

select * from activity order by activity_id asc;
select * from tag order by tag_id asc;
select * from tag_in_activity;
select * from record_time;
select * from record_activity;
select * from tag_in_r_a;
select * from button_arr;

select * from record_activity as ra join record_time as rt on ra.record_id = rt.record_id join activity as a on ra.activity_id = a.activity_id;



# 200829 로그 테이블 재설계

# activity
create table activity (
	activity_id int not null auto_increment primary key,
    name varchar(100) not null,
    icon int not null,
    bg_color int not null,
    unique(name, icon, bg_color)
);
-- icon : 안드 이미지 파일 id

# button_seat
create table button_seat (
	button_seat_id int not null auto_increment primary key,
    layout_id int not null,
    activity_id int,
    time_record_id int check(activity_id is not null),
    unique (layout_id),
    unique (activity_id)
);

# time_record
create table time_record (
	time_record_id int not null auto_increment primary key,
    activity_id int not null,
    start_time timestamp not null default now(),
    finish_time timestamp not null default now(),
    record_memo varchar(1000)
);
-- button_seat의 time_record_id는 조회용이 아니므로 
-- activity_id와 time_record_id를 이어서 로그 조회를 하기 위해서는 time_record 테이블에 activity_id 데이터가 필요하다.

# tag
create table tag (
	tag_id int not null auto_increment primary key,
	tag varchar(100) not null,
    unique(tag)
);

# activity_tag
create table activity_tag (
	activity_tag_id int not null auto_increment primary key,
	activity_id int not null,
    tag_id int not null
);

# activity_record_tag
create table time_record_tag (
	time_record_tag_id int not null auto_increment primary key,
    time_record_id int not null,
    tag_id int not null
);

# daily_record
create table daily_record (
	daily_record_id int not null auto_increment primary key,
    day date not null,
    feeling_icon int,
    daily_note varchar(2000)
);

drop table activity_record;
drop table button_seat;

# 활동 저장
insert into activity (name, icon, bg_color) values 
("activity_1", 1001, 2001), ("activity_2", 1002, 2002), ("activity_3", 1003, 2003), ("activity_4", 1004, 2004), ("activity_5", 1005, 2005),
("activity_6", 1006, 2006), ("activity_7", 1007, 2007), ("activity_8", 1008, 2008), ("activity_9", 1009, 2009);

# 버튼 자리별 activity_id(활동) 배치
insert into button_seat (layout_id, activity_id) values
(3001, 1), (3002, 2), (3003, 3), (3004, 4), (3005, 5), (3006, 6), (3007, 7), (3008, 8), (3009, 9);

# 시간 기록 시작
insert into time_record (activity_id) values (1);
insert into time_record (activity_id) values (3);
insert into time_record (activity_id) values (5);
insert into time_record (activity_id) values (7);
insert into time_record (activity_id) values (9);

# 버튼을 누른 자리에 시작한 시간 기록을 update 
update button_seat set time_record_id = 1 where button_seat_id = 1;
update button_seat set time_record_id = 2 where button_seat_id = 3;
update button_seat set time_record_id = 3 where button_seat_id = 5;
update button_seat set time_record_id = 4 where button_seat_id = 7;
update button_seat set time_record_id = 5 where button_seat_id = 9;

# 시간 기록 종료
update time_record set finish_time = now() where time_record_id = 5; -- time_record_id 1부터 5까지 5번 update

# 기록 종료된 자리의 time_record_id를 0으로 교체
update button_seat set time_record_id = 0 where button_seat_id = 1;
update button_seat set time_record_id = 0 where button_seat_id = 3;
update button_seat set time_record_id = 0 where button_seat_id = 5;
update button_seat set time_record_id = 0 where button_seat_id = 7;
update button_seat set time_record_id = 0 where button_seat_id = 9;

# 태그 저장
insert into tag (tag) values 
("#tag_1"), ("#tag_2"), ("#tag_3"), ("#tag_4"), ("#tag_5"), ("#tag_6"), ("#tag_7"), ("#tag_8"), ("#tag_9"), ("#tag_10"), 
("#tag_11"), ("#tag_12"), ("#tag_13"), ("#tag_14"), ("#tag_15"), ("#tag_16"), ("#tag_17"), ("#tag_18"), ("#tag_19"), ("#tag_20");

# 활동별 기본 태그 추가
insert into activity_tag (activity_id, tag_id) values (3, 1), (3, 2), (7, 3), (7, 4);

# 시간 기록별로 태그 추가
insert into time_record_tag (time_record_id, tag_id) values (1, 5), (1, 6), (1, 7), (3, 8), (3, 9), (5, 10), (5, 11), (7, 12), (7, 13), (8, 14), (8, 15);

# 기록별로 노트 추가
update time_record set record_memo = "note_1" where time_record_id = 2;
update time_record set record_memo = "note_2" where time_record_id = 4;

# 로그 조회용 select
select tr.time_record_id, a.name, a.icon, a.bg_color, tr.start_time, tr.finish_time, 
timediff(tr.finish_time, tr.start_time) as record, t.tag, ta.tag, tr.record_note 
from time_record as tr 
join activity as a on tr.activity_id = a.activity_id 
left join activity_tag as ac on a.activity_id = ac.activity_id 
left join tag as t on ac.tag_id = t.tag_id 
left join time_record_tag as trt on tr.time_record_id = trt.time_record_id
left join tag as ta on trt.tag_id = ta.tag_id
order by tr.start_time asc;

# 버튼 상태 조회
select * from button_seat;