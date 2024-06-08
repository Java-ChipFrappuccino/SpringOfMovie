window.addEventListener('load', function () {
    //==========================메인 페이지 헤더 스크립트===================================
    {
        const header = document.querySelector('.header');
        let scrollPosition = window.scrollY;

        if (scrollPosition > 100) {
            header.classList.remove('main-change-header');
            header.classList.add('hide-header');
            header.classList.add('scrolled');
        }
        window.onscroll = function () {
            scrollPosition = window.scrollY;
            if (scrollPosition > 100) {
                header.classList.remove('main-change-header');
                header.classList.add('hide-header');
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
                header.classList.remove('hide-header');
                header.classList.add('main-change-header');
            }
        };
    }

    //열하나하나
    const rows = document.querySelectorAll('.betting-modals');

    //모달창 변수들
    const closeButton = document.getElementById('close-btn');
    const modal = document.getElementById('modal');
    const modalBackdrop = document.getElementById('modal-backdrop');

    //행 하나마다
    rows.forEach(row => {
        row.addEventListener('click', () => {

            const pgbId = row.getAttribute('data-id');

            // data-id 값을 콘솔에 출력합니다.
            console.log('Clicked row ID:', pgbId);



            //AJAX로 POST 요청해서 모달 가져오기
            fetch('/api/playground/findboardbyid',{
                // POST 요청
                method: 'POST',
                // JSON 형식으로 데이터 보내기
                headers: {
                    'Content-Type': 'application/json'
                },
                // 데이터를 JSON 문자열로 변화하여 보냄.
                body: JSON.stringify(pgbId)

            }).then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();

            }).then(data => {
                console.log("Response from server:", data);

                // 데이터로 모달 업데이트
                updateModalContent(data);

                // 모달 표시
                modal.classList.remove('d:none');
                modalBackdrop.classList.remove('d:none');
                modal.classList.add('modal-fade-in');

            }).catch(error => {
                    // 오류 처리
                    console.error("Error:", error);
                });

        });
    });


    closeButton.addEventListener('click', function () {
        modal.classList.replace('modal-fade-in', 'modal-fade-out');

        setTimeout(() => {
            modal.classList.add('d:none');
            modalBackdrop.classList.add('d:none');
            modal.classList.remove('modal-fade-out');
        }, 130);
    });

    //모달에 데이터 넣어주기
    function updateModalContent(data) {
        // data 객체에서 필요한 데이터 추출
        const title = data.title || '제목 없음';
        const totalPoints = (data.leftBettingPoint + data.rightBettingPoint -100000) || '0';
        const posterUrl = data.posterUrl || '/image/no_image_poster.png';

        // 모달 요소 업데이트
        modal.querySelector('.title').textContent = title;
        modal.querySelector('.totalPoints').textContent = `누적 포인트: ${totalPoints} 만점`;
        modal.querySelector('.posterUrl').src = posterUrl;
    }

    //베팅페이지로 이동 버튼
    const bettingButton = document.getElementById('betting-btn');

    bettingButton.addEventListener('click', function () {

        location.href = '/playground/main';
    });

    //==========================메인 페이지 캐러셀 슬라이드 스크립트===================================
    {
        const slideSize = document.querySelector('.carousel_main').offsetWidth;
        const swiper = document.querySelector('.carousel_wrapper');
        const prevButtons = document.querySelectorAll('.carousel_prev');
        const nextButtons = document.querySelectorAll('.carousel_next');
        const bullets = document.querySelectorAll('.carousel_circle');
        const slideTotalCount = Number(document.querySelectorAll('.carousel_slide').length);
        let currentSlide = 0;
        let slideInterval;

        function showSlide(slideIndex) {
            if (slideTotalCount === slideIndex) {
                swiper.style.transform = `translateX(-${slideIndex * slideSize}px)`;
                currentSlide = slideIndex;
                return;
            }
            swiper.style.transform = `translateX(-${slideIndex * slideSize}px)`;
            currentSlide = slideIndex;

            bullets.forEach((bullet, index) => {
                if (index === currentSlide) {
                    bullet.classList.add('active');
                } else {
                    bullet.classList.remove('active');
                }
            });
        }
        // 5초마다 자동으로 슬라이드를 넘겨줌
        function startSlideShow() {
            slideInterval = setInterval(() => {
                if (currentSlide === slideTotalCount - 1) {
                    currentSlide = -1;
                }
                showSlide(currentSlide + 1);
            }, 5000);
        }
        // 사용자가 슬라이드를 수동으로 조작하면 5초 타이머를 초기화 시킴
        function resetSlideShow() {
            clearInterval(slideInterval);
            startSlideShow();
        }
        prevButtons.forEach((prevButton) => {
            prevButton.onclick = function (){
                if (currentSlide > 0) {
                    showSlide(currentSlide - 1);
                } else if (currentSlide === 0) {
                    showSlide(slideTotalCount - 1);
                }
                resetSlideShow();
            };
        });

        nextButtons.forEach((nextButton) => {
            nextButton.onclick = function (){
                if (currentSlide < slideTotalCount - 1) {
                    showSlide(currentSlide + 1);
                } else if (currentSlide === slideTotalCount - 1) {
                    showSlide(0);
                }
                resetSlideShow();
            };
        });

        bullets.forEach((bullet, index) => {
            bullet.onclick = function (){
                showSlide(index);
                resetSlideShow();
            };
        });
        //첫 페이지 로딩시 0번째 슬라이드로 초기화
        showSlide(0);
        //자동 슬라이드 함수 호출
        startSlideShow();
    }



});