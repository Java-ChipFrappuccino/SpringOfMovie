const inputField = document.querySelector(".reg-textarea");
let legButton = document.querySelector(".reg-button");
let emptyBox = document.querySelector(".empty-field");
if (legButton) {
    legButton.onclick = function (e) {
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
{
// 게시글 버튼 요소 가져오기
    let voteBox = document.querySelector("#vote-box");
    let likeButtonBox = voteBox.querySelector(".like-box");
    let disLikeButtonBox = voteBox.querySelector(".dislike-box");
    let boardDeleteButton = document.querySelector(".board-delete-button");
    // let currentUrl = window.location.href;

    if (boardDeleteButton) {
        //게시글 삭제 버튼 클릭시
        boardDeleteButton.onclick = function (e) {
            e.preventDefault();
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
                        title: "게시글이 삭제되었습니다",
                        closeOnClickOutside: true,
                        confirmButtonColor: "#3085d6",
                    }).then(() => {
                        boardDeleteButton.closest("form").submit();
                    });
                }
            });
        }
    }

    let likeCount = likeButtonBox.querySelector(".like-box > span:first-child");
    let disLikeCount = disLikeButtonBox.querySelector(".dislike-box > span:first-child");
// let data = {};
//     let boardId = Number(voteBox.dataset.boardid);
    let boardId = Number(voteBox.dataset.boardid);
    let url = '/api/community-board-likes/board/' + boardId;

    voteBox.onclick = function (e) {
        e.preventDefault();

        if (likeButtonBox.classList.contains('bd-color:accent-1')) {
            Swal.fire("이미 '좋아요' 하신 게시글 입니다.");
            return;
        } else if (disLikeButtonBox.classList.contains('bd-color:sub-1')) {
            Swal.fire("이미 '싫어요' 하신 게시글 입니다.");
            return;
        }
        // 좋아요 클릭
        if (likeButtonBox.contains(e.target)) {

            async function like() {
                const response = await fetch(url + '/like',
                    {
                        method: 'POST',
                        // headers: {
                        //     'Content-Type': 'text/plain'
                        // },
                        // body: boardId
                    });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const resultText = await response.text(); //결과값을 문자열로 받음
                const result = parseInt(resultText, 10); //int로 형변환, 두번째 매개변수는 변환할 진법/ 10진수,2,8,16...

                switch (result) {
                    case 100:
                        Swal.fire("로그인후 이용할수 있습니다");
                        break;
                    case 1:
                        likeButtonBox.classList.add('bd-color:accent-1');
                        // 현재 값 가져오기
                        let currentLikeCount = Number(likeCount.innerText);
                        // 값 증가
                        let addLikeCount = currentLikeCount + 1;
                        // 버튼 텍스트 업데이트
                        likeCount.innerText = addLikeCount;
                        break;
                    default:
                        Swal.fire("예기치못한 오류가 발생했습니다, 잠시후 다시 시도해주세요");
                        break;
                }
            }

            like();
        }

        // 싫어요 클릭
        if (disLikeButtonBox.contains(e.target)) {
            // e.target.closest(".dislike-box") 이렇게도 쓸수있음

            async function disLike() {
                const response = await fetch(url + '/dislike',
                    {
                        method: 'POST',
                        // headers: {
                        //     'Content-Type': 'text/plain'
                        // },
                        // body: boardId
                    });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const resultText = await response.text(); //결과값을 문자열로 받음
                const result = parseInt(resultText, 10); //int로 형변환, 두번째 매개변수는 변환할 진법/ 10진수,2,8,16...

                switch (result) {
                    case 100:
                        Swal.fire("로그인후 이용할수 있습니다");
                        break;
                    case -1:
                        disLikeButtonBox.classList.add('bd-color:sub-1');
                        // 현재 값 가져오기
                        let currentDisLikeCount = Number(disLikeCount.innerText);
                        // 값 증가
                        let addDisLikeCount = currentDisLikeCount + 1;
                        // 버튼 텍스트 업데이트
                        disLikeCount.innerText = addDisLikeCount;
                        break;
                    default:
                        Swal.fire("예기치못한 오류가 발생했습니다, 잠시후 다시 시도해주세요");
                        break;
                }
            }

            disLike();

        }
    };
}

// =========================================================================================================

{
// 댓글 버튼 요소 가져오기
    //반복문으로 생성된 여러개를 전체 선택 / 배열에 담김
    let commentVoteBoxes = document.querySelectorAll(".comment-vote-box");
    //그 배열을 하나씩 순회하면서 값을 사용함
    commentVoteBoxes.forEach(function(commentVoteBox) {
        let likeButtonBox = commentVoteBox.querySelector(".icon\\:thumbs_up");
        let disLikeButtonBox = commentVoteBox.querySelector(".icon\\:thumbs_down");
        let likeCount = likeButtonBox;
        let disLikeCount = disLikeButtonBox;
        let commentId = Number(commentVoteBox.dataset.commentid);
        let url = '/api/community-board-likes/comment/' + commentId;

        commentVoteBox.onclick = function (e) {
            e.preventDefault();

            if (likeButtonBox.classList.contains('icon-color:accent-1')) {
                Swal.fire("이미 '좋아요' 하신 댓글 입니다.");

                return;
            } else if (disLikeButtonBox.classList.contains('icon-color:sub-1')) {
                Swal.fire("이미 '싫어요' 하신 댓글 입니다.");
                return;
            }
            // 좋아요 클릭
            if (likeButtonBox.contains(e.target)) {

                async function like() {
                    const response = await fetch(url + '/like',
                        {
                            method: 'POST',
                            // headers: {
                            //     'Content-Type': 'text/plain'
                            // },
                            // body: boardId
                        });

                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }

                    const resultText = await response.text(); //결과값을 문자열로 받음
                    const result = parseInt(resultText, 10); //int로 형변환, 두번째 매개변수는 변환할 진법/ 10진수,2,8,16...

                    switch (result) {
                        case 100:
                            Swal.fire("로그인후 이용할수 있습니다");
                            break;
                        case 1:
                            likeButtonBox.classList.add('icon-color:accent-1');
                            // 현재 값 가져오기
                            let currentLikeCount = Number(likeCount.innerText);
                            // 값 증가
                            let addLikeCount = currentLikeCount + 1;
                            // 버튼 텍스트 업데이트
                            likeCount.innerText = addLikeCount;
                            break;
                        default:
                            Swal.fire("예기치못한 오류가 발생했습니다, 잠시후 다시 시도해주세요");
                            break;
                    }
                }

                like();
            }

            // 싫어요 클릭
            if (disLikeButtonBox.contains(e.target)) {
                // e.target.closest(".dislike-box") 이렇게도 쓸수있음

                async function disLike() {
                    const response = await fetch(url + '/dislike',
                        {
                            method: 'POST',
                            // headers: {
                            //     'Content-Type': 'text/plain'
                            // },
                            // body: boardId
                        });

                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }

                    const resultText = await response.text(); //결과값을 문자열로 받음
                    const result = parseInt(resultText, 10); //int로 형변환, 두번째 매개변수는 변환할 진법/ 10진수,2,8,16...

                    switch (result) {
                        case 100:
                            Swal.fire("로그인후 이용할수 있습니다");
                            break;
                        case -1:
                            disLikeButtonBox.classList.add('icon-color:sub-1');
                            // 현재 값 가져오기
                            let currentDisLikeCount = Number(disLikeCount.innerText);
                            // 값 증가
                            let addDisLikeCount = currentDisLikeCount + 1;
                            // 버튼 텍스트 업데이트
                            disLikeCount.innerText = addDisLikeCount;
                            break;
                        default:
                            Swal.fire("예기치못한 오류가 발생했습니다, 잠시후 다시 시도해주세요");
                            break;
                    }
                }

                disLike();

            }
        }
    });
}

// =========================================================================================================

{
    {
        // 댓글 등록시 엔터키로 입력
        let regBox = document.querySelector(".reg-box");
        if (regBox.querySelector(".reg-button") != null) { // 비로그인 유저 접속시 콘솔창 오류나는거 보기싫어서 추가함
            // 인증된 사용자를 위한 <button>의 DOM 객체를 얻음
            let regButton = regBox.querySelector(".reg-button");
            // 인증된 사용자를 위한 <textarea>의 DOM 객체를 얻음
            let regTextArea = regBox.querySelector(".reg-textarea");

            regTextArea.addEventListener('keypress', function (event) {
                if (event.key === 'Enter') {
                    //쉬프트 엔터 누를시 줄넘김 처리
                    if (event.shiftKey) {
                        // Allow default behavior (new line) if Shift + Enter is pressed
                        return;
                    } else {
                        // Prevent default behavior (new line) if only Enter is pressed
                        event.preventDefault();
                        let regTextAreaContent = regTextArea.value;
                        regTextArea.value = regTextAreaContent.replace(/\n/g, '<br>');
                        regButton.click();
                    }
                }
            });
        }
    }
        //댓글 삭제, 수정
        //댓글 수 만큼 코멘트 박스 찾기
        let commentBoxes = document.querySelectorAll(".comment-box");
        // 현재 URL의 쿼리 문자열 가져오기
        let params = new URLSearchParams(window.location.search);
        // 특정 파라미터 값 가져오기 (예: 'c' 파라미터 값)
        let categoryEngName = params.get('c');
        let boardId = params.get('id');
        let url = '/api/community-board/comments/';
        commentBoxes.forEach(function (commentBox) {
            //댓글 수 만큼 반복문 돌리는데 그중에 내가 쓴 댓글이라서 수정,삭제 버튼이 존재하는 경우에만 함수 추가,
            //이렇게 안하면 내가 작성하지 않은(수정,삭제 없는) 코멘트박스에서 버튼 돔객체를 찾지 못해 null값이 저장되어서 오류발생
            if (commentBox.querySelector(".del-btn") != null) {
                let deleteButton = commentBox.querySelector(".del-btn");
                let editButton = commentBox.querySelector(".edit-btn");
                let myComment = commentBox.querySelector("p");
                let currentText = myComment.innerHTML; //줄넘김 br 태그를 포함한 html형식으로 텍스트를 가져옴
                let commentId = Number(deleteButton.dataset.commentid);
                //댓글 삭제
                deleteButton.onclick = async function (e) {
                    const choice = await Swal.fire({
                        title: "정말 삭제하시겠습니까?",
                        text: "삭제한 후에는 복구가 불가능합니다 🥲",
                        // icon: "warning",
                        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
                        confirmButtonColor: "#d33", //빨간색
                        cancelButtonColor: "#3085d6", //파란색
                        confirmButtonText: "삭제",
                        cancelButtonText: "취소",
                        reverseButtons: false // 버튼 순서 거꾸로
                    });

                    if (choice.isDismissed) {
                        return;
                    }

                    const response = await fetch(url + commentId,
                        {
                            method: 'DELETE',
                            // headers: {
                            //     'Content-Type': 'text/plain'
                            // },
                            // body: boardId
                        });

                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }

                    const resultText = await response.text(); //결과값을 문자열로 받음
                    const result = parseInt(resultText, 10); //int로 형변환, 두번째 매개변수는 변환할 진법/ 10진수,2,8,16...

                    switch (result) {
                        case 100:
                            Swal.fire("로그인후 이용할수 있습니다");
                            break;
                        case 1:
                            await Swal.fire({
                                title: "댓글이 삭제되었습니다",
                                // icon  : "success",
                                closeOnClickOutside: true,
                                confirmButtonColor: "#3085d6",
                            });
                            window.location.href = `/community/board/detail?c=${categoryEngName}&id=${boardId}`;
                            break;
                        default:
                            Swal.fire("예기치못한 오류가 발생했습니다, 잠시후 다시 시도해주세요");
                            break;
                    }
                };
                // 댓글 수정
                editButton.onclick = async function (e) {

                    // 수정버튼 누를시 텍스트 에이리어 돔 객체를 만듦
                    let textArea = document.createElement("textarea");
                    textArea.classList.add("mb:2","py:2","h:2","n-textbox");
                    // 현재 텍스트를 최신으로 다시 업데이트한다
                    currentText = myComment.innerHTML;
                    textArea.value = currentText.replace(/<br>/g, '\n')
                    // textArea.value = currentText;

                    // 내 댓글창을 텍스트에이리어 창으로 바꾼다
                    myComment.replaceWith(textArea);
                    // 수정버튼 클릭시 취소버튼 생성
                    let cancelButton = document.createElement("button");
                    cancelButton.textContent = "취소";

                    // 취소버튼에 클래스 추가
                    cancelButton.classList.add("mr:3", "p:1", "bd", "bd-radius:3", "bg-color:base-1", "box-shadow:2");

                    // 삭제버튼을 취소버튼으로 변경
                    deleteButton.replaceWith(cancelButton);
                    // 취소버튼 클릭시 원상태로 변경
                    cancelButton.onclick = function (e) {
                        // myComment.innerHTML = currentText.replace(/<br>/g, '\n')
                        textArea.replaceWith(myComment);
                        cancelButton.replaceWith(deleteButton);
                        submitButton.replaceWith(editButton);
                    }
                    // 수정완료 버튼 추가
                    let submitButton = document.createElement("button");
                    submitButton.classList.add("mr:3", "p:1", "bd", "bd-radius:3", "bg-color:base-1", "box-shadow:2");
                    submitButton.textContent = "수정";
                    editButton.replaceWith(submitButton);

                    //엔터키 누를시 입력되게 리스너 등록
                    textArea.addEventListener('keydown', function (event) {
                        if (event.key === 'Enter') {
                            //쉬프트 엔터 누를시 줄넘김 처리
                            if (event.shiftKey) {
                                // Allow default behavior (new line) if Shift + Enter is pressed
                                return;
                            } else {
                                // Prevent default behavior (new line) if only Enter is pressed
                                event.preventDefault();
                                submitButton.click();
                            }
                        }
                    });

                    // 수정완료 버튼 클릭시 수정 요청 보냄
                    submitButton.onclick = async function (e) {

                        let comment = textArea.value;
                        comment = comment.replace(/\n/g, '<br>');
                        const response = await fetch(url + commentId,
                            {
                                method: 'PUT',
                                headers: {
                                    'Content-Type': 'text/plain'
                                },
                                body: comment
                            });

                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }

                        const resultText = await response.text(); //결과값을 문자열로 받음
                        const result = parseInt(resultText, 10); //int로 형변환, 두번째 매개변수는 변환할 진법/ 10진수,2,8,16...

                        switch (result) {
                            case 100:
                                Swal.fire("로그인후 이용할수 있습니다");

                                break;
                            case 1:
                                // 수정완료
                                myComment.innerHTML = comment;
                                // window.location.href = 'http://localhost/community/board/detail?c=review&id=81';
                                break;
                            default:
                                Swal.fire("예기치못한 오류가 발생했습니다, 잠시후 다시 시도해주세요");
                                break;
                        }

                        // 요청이 끝나고 버튼과 돔객체를 원위치
                        textArea.replaceWith(myComment);
                        submitButton.replaceWith(editButton);
                        cancelButton.replaceWith(deleteButton);
                        return;
                    };

                };
            }
        });
    }
// =========================================================================================================

{
    //텍스트 입력수 제한 필터링스크립트
    const inputField = document.querySelector(".reg-textarea");
    const counterElement = document.querySelector(".charCount");

    inputField.oninput = function() {
        const maxLength = parseInt(inputField.getAttribute("maxlength"));
        let currentLength = inputField.value.length;

        // 최대 길이를 초과하는 입력을 제거
        if (currentLength > maxLength) {
            inputField.value = inputField.value.slice(0, maxLength);
            currentLength = maxLength;
        }

        // const remainingLength = maxLength - currentLength;
        counterElement.textContent = `${currentLength} / ${maxLength}`;
    };
}