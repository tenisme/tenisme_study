# db 만들기
create database photo_sns_test
default character set utf8
default collate utf8_general_ci;

# database에 접속할 수 있는 유저 생성
# my_test 데이터베이스에만 접속할 수 있는 "유저 생성"
create user 'node_user'@'%' identified by '0000000000';
	# 어디서든 원격 접속이 가능한 node_user라는 user를 만들고 identified by로 비밀번호를 부여한다
		# '유저아이디' 이름의 엔드 유저를 만들고 '접속가능경로'를 통해 접속이 가능하도록 설정한다 : '유저아이디'@'접속가능경로'
			# '유저아이디'@'%' => 이 유저 아이디는 '원격' 접속이 가능

drop user 'node_user'@'%';

# 유저 권한 설정
grant all on photo_sns_test.* to 'node_user'@'%';
	# node_user라는 엔드 유저는 원격 접속('%')을 통해 my_test db에 접속해 모든 테이블(.*)에서 모든 작업(all)을 할 수 있음. "db를 새로 만들거나 할 수는 없음."
	# db/테이블에서 실행 가능한 작업 권한 부여하기 : grant 가능한작업
		# grant all => 모든 작업(CRUD) 가능
    # 접속 가능한 db 설정하기 : on 데이터베이스.테이블 '데이터베이스'의 '테이블'에 접속 가능.
		# *은 모든 DB, .*은 모든 테이블을 의미함.
    # 권한을 줄 유저 설정하기 : to '유저아이디'@'접속가능경로'
    
alter table my_test.favorite_movie rename movie_test.favorites;