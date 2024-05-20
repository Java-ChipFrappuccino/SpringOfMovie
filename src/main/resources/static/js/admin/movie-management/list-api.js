// DOMContentLoaded 이벤트가 발생하면 실행될 함수 등록
document.addEventListener('DOMContentLoaded', function() {
  // 모든 검색 버튼을 선택
  const buttons = document.querySelectorAll('.n-btn');
  const modal = document.getElementById('movieModal'); // 모달 요소 가져오기
  const span = document.querySelector('.close-btn'); // 닫기 버튼 요소 가져오기

  // 각 검색 버튼에 이벤트 리스너 등록
  buttons.forEach(button => {
      button.addEventListener('click', async function() {
          // 클릭된 버튼이 속한 섹션 요소 가져오기
          const section = this.closest('section');
          // 섹션 요소에서 영화 이름 가져오기
          const movieName = section.querySelector('h1').innerText;
          const movieNameEn = section.querySelector('h2').innerText;
          console.log(movieName);

          try {
              // 한글로 검색
              let movies = await searchMovies(movieName);
              // 검색 결과가 없을 경우 영어로 다시 검색
              if (!movies || movies.length === 0) {
                  movies = await searchMovies(movieNameEn);
              }

              if (movies && movies.length > 0) {
                  // 검색된 영화 정보를 모달에 표시
                  displayMovies(movies);
                  modal.style.display = 'block'; // 모달 열기
              } else {
                  // 검색된 영화가 없는 경우 경고 표시
                  console.error('No movie found with name:', movieName);
                  alert("영화를 찾지 못했습니다.");
              }
          } catch (error) {
              console.error('Error during movie search:', error);
          }
      });
  });

  // 닫기 버튼 클릭 이벤트 리스너 등록
  span.onclick = function() {
      modal.style.display = 'none'; // 모달 닫기
  }

  // 모달 외부 클릭 시 모달 닫기
  window.onclick = function(event) {
      if (event.target == modal) {
          modal.style.display = 'none';
      }
  }
});

// 영화 검색 함수
async function searchMovies(movieName) {
  // API 엔드포인트 및 키
  const apiUrl = `https://api.themoviedb.org/3/search/movie?query=${movieName}&include_adult=false&language=ko-KR&page=1`;
  const apiKey = 'eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyM2U5NWU1MTY0NWUzYjgwZDU0MzQyNGQxYTA5ODg0YSIsInN1YiI6IjY2MDEzYjRmNzcwNzAwMDE2MzBhZjg0MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.RFfawiMrE8C2YgpGPdaU5IOcl-R5t-JIquRBN6vaLzU';

  try {
      // API 호출
      const response = await fetch(apiUrl, {
          method: 'GET',
          headers: {
              'Accept': 'application/json',
              'Authorization': `Bearer ${apiKey}`
          }
      });

      if (!response.ok) {
          throw new Error('Network response was not ok');
      }

      // API 응답을 JSON 형식으로 변환
      const responseData = await response.json();
      const movies = [];

      // 검색된 영화 정보를 파싱하여 배열에 추가
      responseData.results.forEach(movieInfo => {
          const mi = {
              movieCode: movieInfo.id || 0,
              name: movieInfo.title || null,
              originName: movieInfo.original_title || null,
              posterUrl: movieInfo.poster_path ? `https://image.tmdb.org/t/p/original${movieInfo.poster_path}` : null,
              releaseDate: movieInfo.release_date || null
          };
          movies.push(mi);
      });

      return movies; // 검색된 영화 정보 배열 반환
  } catch (error) {
      console.error('Error during movie search:', error);
      return null;
  }
}

// 검색된 영화를 화면에 표시하는 함수
function displayMovies(movies) {
  // 영화 목록을 표시할 요소 가져오기
  const movieListDiv = document.getElementById('movie-list');
  movieListDiv.innerHTML = ''; // 이전 내용 지우기

  // 각 영화 정보를 반복하여 화면에 표시
  movies.forEach(movie => {
      const movieItem = document.createElement('div');
      movieItem.classList.add('movie-item');
      movieItem.innerHTML = `
          <h3>${movie.name}</h3>
          <p>원제목: ${movie.originName}</p>
          <p>개봉년도: ${movie.releaseDate || '알 수 없음'}</p>
          <img src="${movie.posterUrl}" alt="영화 포스터" style="max-width: 200px;">
          <button class="n-btn" type="button">선택</button>
      `;
      movieListDiv.appendChild(movieItem);
  });
}