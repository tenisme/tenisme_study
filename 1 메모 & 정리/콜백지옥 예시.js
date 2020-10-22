// 유저가 안드로이드에서 사진과 글을 서버로 전송하는 경우

// 1. 유저 디비에서 우리 서비스를 이용하는 유저가 있는지 체크
// => 데이터베이스 작업 : 시간이 많이 걸리는 작업 : Web API : 비동기 실행된다.

// 2. 체크 결과 이상없으면, 파일을 다운로드받습니다.
// => 파일 처리 : 시간이 많이 걸리는 작업 : Web API : 비동기 실행된다.

// 3. 다운로드가 완료되면, AWS S3에 저장한다.
// => 네트워크 통신 : 시간이 많이 걸리는 작업 : Web API : 비동기 실행된다.

// 4. 저장이 완벽히 끝나면, 포스팅 디비에 새로운 내용을 인서트합니다.
// => 데이터베이스 작업 : 시간이 많이 걸리는 작업 : Web API : 비동기 실행한다.

//// 비동기로 작업하는 게 하나가 아니라 여러개일 때 => 콜백 헬(콜백 지옥) 발생

function workDB(callback, timeout) {
  setTimeout(callback, timeout);
}

function workFile(callback, timeout) {
  setTimeout(callback, timeout);
}

function workAWS(callback, timeout) {
  setTimeout(callback, timeout);
}

//// 콜백지옥 예시(함수 중첩)
// 콜백 지옥의 문제 : 분석하기 어려운 코드가 됨.
// 비동기 콜백이 중첩될 때 콜백 지옥이 생성됨.
// 이걸 해결하는 게 프로미스(강사님이 pdf 파일로 nodejs책 주신 거 참고하는 게 좋음.)

// 1. 유저 정보를 DB에서 가져온다
workDB(function () {
  console.log("1. get user info");

  // 2. 파일을 다운로드 받는다.
  // 순서대로 실행하려면 2번을 밖에서 호출하면 안 되므로 함수 내부에 적음. => 콜백 지옥 생성의 시작
  workFile(function () {
    console.log("2. File Download");

    // 3. AWS S3 보낸다.
    workAWS(function () {
      console.log("3. AWS S3 upload");

      // 4. 포스팅 DB에 정보를 인서트한다.
      workDB(function () {
        console.log("4. DB insert");
      }, 2000);
    }, 3000);
  }, 1000);
}, 2000);
