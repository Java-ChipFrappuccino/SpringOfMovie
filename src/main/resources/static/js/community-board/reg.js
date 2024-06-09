$('#summernote').summernote({

    // 에디터 크기 설정
    height: 800,
    // 에디터 한글 설정
    lang: 'ko-KR',
    // 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
    toolbar: [
        // 글자 크기 설정
        ['fontsize', ['fontsize']],
        // 글자 [굵게, 기울임, 밑줄, 취소 선, 지우기]
        ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
        // 글자색 설정
        ['color', ['color']],
        // 표 만들기
        ['table', ['table']],
        // 서식 [글머리 기호, 번호매기기, 문단정렬]
        ['para', ['ul', 'ol', 'paragraph']],
        // 줄간격 설정
        ['height', ['height']],
        // 이미지 첨부
        ['insert',['picture']]
    ],
    // 추가한 글꼴
    fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
    // 추가한 폰트사이즈
    fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72','96'],
    // focus는 작성 페이지 접속시 에디터에 커서를 위치하도록 하려면 설정해주세요.
    focus : true,
    // callbacks은 이미지 업로드 처리입니다.
    callbacks : {
        onImageUpload : function(files, editor, welEditable) {
            // 다중 이미지 처리를 위해 for문을 사용했습니다.
            let maxFileSize = 5 * 1024 * 1024; // 2MB 제한
            let totalFileSize = 0; // 전체 요청의 총 파일 크기
            for (let i = 0; i < files.length; i++) {
                if (files[i].size > maxFileSize) {
                    alert('파일 크기는 5MB를 초과할 수 없습니다.');
                    return;
                }
                totalFileSize += files[i].size;
            }

            let maxTotalFileSize = 10 * 1024 * 1024; // 전체 요청의 총 용량 제한: 10MB
            // 전체 요청의 총 파일 크기가 제한을 초과하는 경우 알림
            if (totalFileSize > maxTotalFileSize) {
                alert('전체 파일의 크기는 10MB를 초과할 수 없습니다.');
                return;
            }

            // 제한을 넘지 않은 파일은 업로드 처리
            for (let i = 0; i < files.length; i++) {
                imageUploader(files[i], this);
            }
        },
        // 복붙할때 쓸때없는 스타일 태그 들어가는거 막는 설정
        onPaste: function(e) {
            let bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('text/html');
            let div = document.createElement('div');
            div.innerHTML = bufferText;
            $(div).find('*').removeAttr('style');
            setTimeout(function () {
                document.execCommand('insertText', false, div.innerText);
            }, 10);
            e.preventDefault();
        },
        onChange: function(contents, $editable) {
            // 최대 글자수
            const maxLength = 1000;

            // HTML 엔티티와 태그를 제거한 텍스트 길이 계산
            const text = $("<div>").html(contents).text();

            // 줄바꿈 문자를 포함한 길이 계산
            const newlineCount = (contents.match(/<p><br><\/p>/g) || []).length;
            const currentLength = text.length + newlineCount;

            // 글자수 표시 업데이트
            document.querySelector('.content-count').textContent = `${currentLength} / ${maxLength}`;

            // 최대 글자수를 초과하는 경우 추가 입력 막기
            if (currentLength > maxLength) {
                // 초과된 글자 수 계산
                const exceededLength = currentLength - maxLength;

                // 초과된 글자 수만큼 내용 제거
                const trimmedContents = contents.substring(0, contents.length - exceededLength);

                // 내용 업데이트
                $editable.html(trimmedContents);

                // 포커스를 잃게 하여 추가 입력 방지
                $editable.blur();
            }
        }
    }

});
//Contador de Caracteres para summernote
$(".note-editable").on("keypress", function(){
    var limiteCaracteres = 255;
    var caracteres = $(this).text();
    var totalCaracteres = caracteres.length;

    //Update value
    $("#total-caracteres").text(totalCaracteres);

    //Check and Limit Charaters
    if(totalCaracteres >= limiteCaracteres){
        return false;
    }
});
function imageUploader(file, el) {
    let formData = new FormData();
    formData.append('file', file);

    $.ajax({
        data : formData,
        type : "POST",
        // url은 자신의 이미지 업로드 처리 컨트롤러 경로로 설정해주세요.
        url : '/post/image-upload',
        contentType : false,
        processData : false,
        enctype : 'multipart/form-data',
        success : function(data) {
            $(el).summernote('insertImage', "/image/upload/"+data, function($image) {
                $image.css('width', "50%");
            });
            // 값이 잘 넘어오는지 콘솔 확인 해보셔도됩니다.
            console.log(data);
        },
        error: function(xhr, status, error) {
            alert('이미지 업로드에 실패했습니다.');
        }
    });
}
// 게시글 필터링 설정
{
    const contentField = document.querySelector(".note-editable");
    const titleField = document.querySelector("input[name=title]");
    const titleCounter = document.querySelector(".title-count");

    //제목 공백입력 제한
    let legButton = document.querySelector(".reg-button");
    let titleEmptyBox = document.querySelector(".title-empty-field");
    let contentEmptyBox = document.querySelector(".content-empty-field");
    legButton.onclick = function (e) {
        let contentText = contentField.innerText;
        // console.log(contentText);
        let inputText = titleField.value;
        // 입력값이 공백인지 확인
        if (inputText.trim() === "") {
            titleEmptyBox.classList.add("show-and-hide");
            setTimeout(function () {
                titleEmptyBox.classList.remove("show-and-hide");
            },3000)
            return false; // 제출을 방지하기 위해 false 반환
        }
        if (contentText.trim() === "") {
            contentEmptyBox.classList.add("show-and-hide");
            setTimeout(function () {
                contentEmptyBox.classList.remove("show-and-hide");
            },3000)
            return false; // 제출을 방지하기 위해 false 반환
        }
        return true; // 유효한 입력이므로 제출 허용
    };

    //제목 글자수 제한
    {
        titleField.oninput = function () {
            const maxLength = parseInt(titleField.getAttribute("maxlength"));
            let currentLength = titleField.value.length;

            // 최대 길이를 초과하는 입력을 제거
            if (currentLength > maxLength) {
                titleField.value = titleField.value.slice(0, maxLength);
                currentLength = maxLength;
            }

            // const remainingLength1 = maxLength - currentLength;
            titleCounter.textContent = `${currentLength} / ${maxLength}`;
        };
    }
}