$(function() {
  // 페이지가 로드되면 실행
  $(window).scroll(function() {
    // 스크롤 이벤트가 발생할 때 실행
    if ($(this).scrollTop() > 250) {
      // 현재 스크롤 위치가 250px 이상일 경우
      $('#topBtn').fadeIn(); // "위로 가기" 버튼을 서서히 나타냄
    } else {
      // 현재 스크롤 위치가 250px 미만일 경우
      $('#topBtn').fadeOut(); // "위로 가기" 버튼을 서서히 사라지게 함
    }
  });

  // "위로 가기" 버튼 클릭 이벤트
  $("#topBtn").click(function() {
    // HTML과 body 요소를 애니메이션으로 스크롤 위치를 맨 위로 이동
    $('html, body').animate({
      scrollTop: 0 // 스크롤 위치를 맨 위(0px)로 설정
    }, 400); // 애니메이션 지속 시간: 400ms
    return false; // 링크 기본 동작 방지 (페이지 이동 방지)
  });
});

// top 위로 가기 버튼
