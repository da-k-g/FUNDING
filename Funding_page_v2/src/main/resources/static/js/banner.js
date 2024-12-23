/**
 * 
 */

document.addEventListener("DOMContentLoaded", function () {
	
    const wrapper = document.querySelector(".banner-wrapper");
    const items = document.querySelectorAll(".banner-item");
    const prevButton = document.querySelector(".banner-container .prev");
    const nextButton = document.querySelector(".banner-container .next");

    let currentIndex = 0;
    const totalItems = items.length;
    let autoSlide;

    // 배너 이동 함수 =-
    function moveToIndex(index) {
        currentIndex = index;

        if (currentIndex >= totalItems) {
            currentIndex = 0; // 처음으로 돌아가기
        } else if (currentIndex < 0) {
            currentIndex = totalItems - 1; // 마지막으로 돌아가기
        }

        const offset = -100 * currentIndex;
        wrapper.style.transform = `translateX(${offset}%)`;
    }

    // 다음 배너로 이동
    function moveToNext() {
        moveToIndex(currentIndex + 1);
    }

    // 이전 배너로 이동
    function moveToPrev() {
        moveToIndex(currentIndex - 1);
    }

    // 자동 슬라이드 시작
    function startAutoSlide() {
        autoSlide = setInterval(moveToNext, 5000); // 5초마다 이동
    }

    // 자동 슬라이드 멈추기
    function stopAutoSlide() {
        clearInterval(autoSlide);
    }

    // 이벤트 리스너
    nextButton.addEventListener("click", () => {
        moveToNext();
        stopAutoSlide();
        startAutoSlide();
    });

    prevButton.addEventListener("click", () => {
        moveToPrev();
        stopAutoSlide();
        startAutoSlide();
    });

    // 초기 자동 슬라이드 시작
    startAutoSlide();
});
