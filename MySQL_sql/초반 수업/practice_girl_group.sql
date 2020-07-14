select girl_group.*, song.*
from girl_group
join song
on girl_group.hit_song_id = song._id;

select girl_group.*, song.*
from girl_group, song
where girl_group.hit_song_id = song._id;

select g.name, s.title
from songs.girl_group as g
Join songs.song as s
on g.hit_song_id = s._id;

SELECT * FROM songs.song;
SELECT * FROM songs.girl_group;

select name, debut
from girl_group
where debut = "2008-01-17";

select g.name, g.debut, s.title
from songs.girl_group as g
Join songs.song as s
on g.hit_song_id = s._id
where debut >= 20090101;

select g.name, g.debut, s.title
from girl_group as g
join song as s
on g.hit_song_id = s._id
where g.name like "%스%";

select g.name, g.debut, s.title
from girl_group as g
join song as s
on g.hit_song_id = s._id
where debut >= 20080501
order by g.debut desc;

select g.*, s.*
from girl_group as g
join song as s
on g.hit_song_id = s._id;

select g.*, s.*
from girl_group as g
left join song as s
on g.hit_song_id = s._id
where s.title is null;

select s.title, g.name
from song as s
left join girl_group as g
on g.hit_song_id = s._id
where s.title = "기대해";

