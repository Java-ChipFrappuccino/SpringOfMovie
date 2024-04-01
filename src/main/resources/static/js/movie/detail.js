function toggleContent() {
    var content = document.getElementById("content");
    if (content.style.display === "none") {
        content.style.display = "block";
    } else {
        content.style.display = "none";
    }
}

//공사중
// window.addEventListener("load", function () {

//     var submitButton =this.document.querySelector("submit-button");
//     var queryInput = queryForm.getElementsByClassName("query-input")[0];


// 	function request(url, callback, method) {

// 	        method = method || "GET";

//         	var xhr = new XMLHttpRequest();
//         	xhr.withCredentials = true;

//         	xhr.onload = function () {
//         	    var list = JSON.parse(this.responseText);
//         	    callback(list);
//         	};

//         	var q = queryInput.value;
//         	xhr.open(method, url);
//         	xhr.send();
//     	}



//    	 submitButton.onclick = function (e) {

//         	e.preventDefault();

// 	        var url = `http://localhost/api/movie?movieid=${movieid}`;


//         	request(url, function (list) {
//            	bind(list);
//             	console.log("검색어 목록 재로드.");
//         	});


//     	};

// 	function bind(list) {

// 		menuContent.innerHTML = "";

// 		list.forEach(reviews => {


// 			var sectionHTML = `<div class="fw:3 fs:2 padding-x:2 padding-y:3 bg-color:base-2 bd-radius:3 margin-bottom:3">
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