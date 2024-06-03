function toggleContent() {
  let content = document.querySelector("#content");
  if (content.classList.contains("toggle-off")) {
    content.classList.remove("toggle-off");

    // setTimeout(()=>{
      content.classList.add("toggle-on") //img 객체를 넣는것과 슬라이드 클래스를 넣어주는 코드 사이에 간극이 너무 짧아서 제대로 동작하지 않을수있기에 지연시간을 넣어준다 
  // },1000);

    // content.classList.add("toggle-on")
    // "style.display === "none") {
    // content.style.display = "block";
  } else {
  content.classList.remove("toggle-on");
  
  // setTimeout(()=>{
    content.classList.add("toggle-off") 
// },1000);
  // content.classList.add("toggle-off");
  }
  // if (content.classList.contains("toggle-on")) {
  //   content.classList.remove("toggle-on");
  //   content.classList.add("toggle-off");
  //   // content.style.display = "none";
  // }
}
//공사중
// window.addEventListener("load", function () {

//     let submitButton =this.document.querySelector("submit-button");
//     let queryInput = queryForm.getElementsByClassName("query-input")[0];

// 	function request(url, callback, method) {

// 	        method = method || "GET";

//         	let xhr = new XMLHttpRequest();
//         	xhr.withCredentials = true;

//         	xhr.onload = function () {
//         	    let list = JSON.parse(this.responseText);
//         	    callback(list);
//         	};

//         	let q = queryInput.value;
//         	xhr.open(method, url);
//         	xhr.send();
//     	}

//    	 submitButton.onclick = function (e) {

//         	e.preventDefault();

// 	        let url = `http://localhost/api/movie?movieid=${movieid}`;

//         	request(url, function (list) {
//            	bind(list);
//             	console.log("검색어 목록 재로드.");
//         	});

//     	};

// 	function bind(list) {

// 		menuContent.innerHTML = "";

// 		list.forEach(reviews => {

// 			let sectionHTML = `<div class="fw:3 fs:2 padding-x:2 padding-y:3 bg-color:base-2 bd-radius:3 margin-bottom:3">
//             <div class="d:flex flex-direction:column gap:2">
//                 <div class="d:flex ai:center gap:3">
//                     <span class="icon icon:user">아이콘</span>
//                     <span>${reviews.count}</span>
//                     <span  class="margin-right:auto">${reviews.memberRate+"원"}</span>
//                 </div>
//                 <div >${reviews.comments}</div>
//                 <div>
//                     <span class="icon icon:thumbs_up">아이콘</span>
//                     <span >${reviews.count}</span>
//                 </div>
//             </div>
//         </div>`;
//                 	menuContent.insertAdjacentHTML("beforeend", sectionHTML);
// 			});
// 		}
// });
//===============================스크롤시 헤더 반응형으로 생성====================================
{
  document.addEventListener('scroll', function() {
    let header = document.querySelector('.hide-header');
    let scrollPosition = window.scrollY;

    if (scrollPosition > 100) { // 스크롤 위치를 임의로 100px로 설정
      header.classList.add('scrolled');
    } else {
      header.classList.remove('scrolled');
    }
  });
}
//===============================스틸컷이미지 클릭시 모달 생성====================================

{
  let modal = document.getElementById('imageModal');
  let images = document.querySelectorAll('.thumbnail');
  let modalImg = document.getElementById('modalImage');
  let currentIndex;

  images.forEach((img,index) => {
    img.onclick = function () {
      currentIndex = index;
      modal.style.display = "flex";
      modalImg.src = this.src;
      setTimeout(function() {
        modal.classList.add('show');
      }, 10); // 작은 지연을 주어 CSS 트랜지션이 제대로 적용되도록 함
    }
  })
  // 모달창 이전,다음 버튼
  let prev = modal.querySelector('.prev');
  let next = modal.querySelector('.next');

  prev.onclick = function() {
    currentIndex = (currentIndex === 0) ? images.length - 1 : currentIndex - 1;
    changeImage();
  }

  next.onclick = function() {
    currentIndex = (currentIndex === images.length - 1) ? 0 : currentIndex + 1;
    changeImage();
  }
// 이미지 변경하는 함수
  function changeImage() {
    modalImg.src = images[currentIndex].src;
  }

// 모달창 닫기버튼
  let span = modal.querySelector('.close');

// 모달창 닫기버튼 클릭시 액션
  span.onclick = function () {
    modal.style.display = "none";
    modal.classList.remove('show');
    setTimeout(function() {
      modal.style.display = "none";
    }, 100); // CSS 트랜지션 시간과 동일하게 설정
  }

// 모달창을 누르거나 이미지 클릭시 닫기 액션
  window.onclick = function (event) {
    if (event.target === modal || event.target === modalImg) {
      modal.style.display = "none";
      modal.classList.remove('show');
      setTimeout(function() {
        modal.style.display = "none";
      }, 100); // CSS 트랜지션 시간과 동일하게 설정
    }
  }
}

{
  let form = document.querySelector(".reg-form");
  let deleteButton = document.querySelector(".delete-button");

  if (deleteButton) {
    //게시글 삭제 버튼 클릭시
    deleteButton.onclick = function (e) {
      // e.preventDefault();
      Swal.fire({
        title: "정말 삭제하시겠습니까?",
        text: "삭제한 후에는 복구가 불가능합니다 🥲",
        // icon: "warning",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: "#d33", //빨간색
        cancelButtonColor: "#3085d6", //파란색
        confirmButtonText: "삭제",
        cancelButtonText: "취소",
        reverseButtons: false // 버튼 순서 거꾸로
      }).then((result) => {
        if (result.isConfirmed) {
          Swal.fire({
            title: "한줄평이 삭제되었습니다",
            closeOnClickOutside: true,
            confirmButtonColor: "#3085d6",
          }).then(() => {
            const form = deleteButton.closest('form');
            form.submit();
          });
        }
      });
    }
  }
}
//====================================================================================
//한줄평 필터링 스크립트
{
  //한줄평 공백 필터링
  const inputField = document.querySelector(".reg-textarea");
  let submitButton = document.querySelector(".submit-button");
  let emptyBox = document.querySelector(".empty-field");
  if (submitButton) {
    submitButton.onclick = function (e) {
      // e.preventDefault();
      let inputText = inputField.value;
      // 입력값이 공백인지 확인
      if (inputText.trim() === "") {
        emptyBox.classList.add("show-and-hide");
        setTimeout(function () {
          emptyBox.classList.remove("show-and-hide");
        }, 3000)
        return false; // 제출을 방지하기 위해 false 반환
      }
      return true; // 유효한 입력이므로 제출 허용
    }
  }
  //텍스트 입력수 제한 필터링스크립트
  const counterElement = document.querySelector(".charCount");

  inputField.oninput = function() {
    const maxLength = parseInt(inputField.getAttribute("maxlength"));
    let currentLength = inputField.value.length;

    // 최대 길이를 초과하는 입력을 제거
    if (currentLength > maxLength) {
      inputField.value = inputField.value.slice(0, maxLength);
      currentLength = maxLength;
    }
    counterElement.textContent = `${currentLength} / ${maxLength}`;
  };
}
