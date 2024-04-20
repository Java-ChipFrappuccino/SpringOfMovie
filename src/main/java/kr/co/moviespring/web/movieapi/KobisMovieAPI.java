package kr.co.moviespring.web.movieapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.co.moviespring.web.movieapi.KonisMovieInfoEntity.Actors;
import kr.co.moviespring.web.movieapi.KonisMovieInfoEntity.Audits;
import kr.co.moviespring.web.movieapi.KonisMovieInfoEntity.Companys;
import kr.co.moviespring.web.movieapi.KonisMovieInfoEntity.Directors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KobisMovieAPI {

    // 상수 설정
    //   - 요청(Request) 요청 변수
    private String REQUEST_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";
    private final String AUTH_KEY = "8eebf0d30cb02fd27d0ccede30262ce2";
 
    //   - 일자 포맷
    private final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyyMMdd");

    // Map -> QueryString
    public String makeQueryString(Map<String, String> paramMap) {
        final StringBuilder sb = new StringBuilder();
        paramMap.entrySet().forEach(( entry )->{
            if( sb.length() > 0 ) {
                sb.append('&');
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        });
        return sb.toString();
    }
 
    // API요청
    public void requestAPI() {
        // 변수설정
        //   - 하루전 날짜
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
 
        // 변수 설정
        //   - 요청(Request) 인터페이스 Map
        //   - 어제자 다양성 한국영화 10개 조회
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("key"          , AUTH_KEY);                      // 발급받은 인증키
        paramMap.put("targetDt"     , DATE_FMT.format(cal.getTime()));// 조회하고자 하는 날짜
        paramMap.put("itemPerPage"  , "10");                    // 결과 ROW 의 개수( 최대 10개 )
        paramMap.put("multiMovieYn" , "N");                     // Y:다양성 영화, N:상업영화, Default:전체
        //paramMap.put("repNationCd"  , "K");                             // K:한국영화, F:외국영화, Default:전체

        try {
            // Request URL 연결 객체 생성
            URL requestURL = new URL(REQUEST_URL+"?"+makeQueryString(paramMap));
            HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
 
            // GET 방식으로 요청
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
 
            // 응답(Response) 구조 작성
            //   - Stream -> JSONObject
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String readline = null;
            StringBuffer response = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                response.append(readline);
            }
 
            // JSON 객체로  변환
            JSONObject responseBody = new JSONObject(response.toString());
 
            // 데이터 추출
            JSONObject boxOfficeResult = responseBody.getJSONObject("boxOfficeResult");
 
            // 박스오피스 주제 출력
            String boxofficeType = boxOfficeResult.getString("boxofficeType");
            System.out.println(boxofficeType);
 
            // 박스오피스 목록 출력
            JSONArray dailyBoxOfficeList = boxOfficeResult.getJSONArray("dailyBoxOfficeList");
            Iterator<Object> iter = dailyBoxOfficeList.iterator();


            while(iter.hasNext()) {
                JSONObject boxOffice = (JSONObject) iter.next();
                System.out.printf("  %s - %s\n", boxOffice.get("rnum"), boxOffice.get("movieNm"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    //순위, 대표코드, 해당일의 관객수, 누적관객수, 누적매출액
    public List<KobisDailyBoxEntity> searchDailyBoxOfficeList(){
        
        // 일단 어제 날짜
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("key"          , AUTH_KEY);                        // 발급받은 인증키
        paramMap.put("targetDt"     , DATE_FMT.format(cal.getTime()));  // 조회하고자 하는 날짜
        paramMap.put("itemPerPage"  , "10");                            // 결과 ROW 의 개수( 최대 10개 )
        paramMap.put("multiMovieYn" , "N");                             // Y:다양성 영화, N:상업영화, Default:전체
        
        //반환할 리스트 객체 생성
        List<KobisDailyBoxEntity> dbeList = new ArrayList<>();
        
        try {
            // Request URL 연결 객체 생성
            URL requestURL = new URL(REQUEST_URL+"?"+makeQueryString(paramMap));
            HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
            
            // GET 방식으로 요청
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            
            // 응답(Response) 구조 작성
            //   - Stream -> JSONObject
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String readline = null;
            StringBuffer response = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                response.append(readline);
            }
 
            // JSON 객체로  변환
            JSONObject responseBody = new JSONObject(response.toString());
 
            // 데이터 추출
            JSONObject boxOfficeResult = responseBody.getJSONObject("boxOfficeResult");
            
            // 박스오피스 주제 출력
            String boxofficeType = boxOfficeResult.getString("boxofficeType");
            System.out.println(boxofficeType);
            
            // 박스오피스 목록 출력
            JSONArray dailyBoxOfficeList = boxOfficeResult.getJSONArray("dailyBoxOfficeList");
            
            
            //여기서 하나씩 받자
            Iterator<Object> iter = dailyBoxOfficeList.iterator();
            
            while(iter.hasNext()) {
                JSONObject boxOffice = (JSONObject) iter.next();
                
                KobisDailyBoxEntity dbe = new KobisDailyBoxEntity();
                dbe.setRnum(boxOffice.getString("rnum"));
                dbe.setRank(boxOffice.getString("rank"));
                dbe.setRankInten(boxOffice.getString("rankInten"));
                dbe.setRankOldAndNew(boxOffice.getString("rankOldAndNew"));
                dbe.setMovieCd(boxOffice.getString("movieCd"));
                dbe.setMovieNm(boxOffice.getString("movieNm"));
                dbe.setOpenDt(boxOffice.getString("openDt"));
                dbe.setSalesAmt(boxOffice.getString("salesAmt"));
                dbe.setSalesShare(boxOffice.getString("salesShare"));
                dbe.setSalesInten(boxOffice.getString("salesInten"));
                dbe.setSalesChange(boxOffice.getString("salesChange"));
                dbe.setSalesAcc(boxOffice.getString("salesAcc"));
                dbe.setAudiCnt(boxOffice.getString("audiCnt"));
                dbe.setAudiInten(boxOffice.getString("audiInten"));
                dbe.setAudiChange(boxOffice.getString("audiChange"));
                dbe.setAudiAcc(boxOffice.getString("audiAcc"));
                dbe.setScrnCnt(boxOffice.getString("scrnCnt"));
                dbe.setShowCnt(boxOffice.getString("showCnt"));

                
                dbeList.add(dbe);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dbeList;
    }


    //영화 상세정보, 인자값은 영화 코드
    public KobisMovieInfoEntity searchMovieInfo(String movieCd){

        //요청 url 설정
        REQUEST_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";

        //반환할 변수 생성
        KobisMovieInfoEntity kobisMovieInfo = new KobisMovieInfoEntity();

        // 변수 설정
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("key"    , AUTH_KEY); // 발급받은 인증키
        paramMap.put("movieCd", movieCd);

        try {
            // Request URL 연결 객체 생성
            URL requestURL = new URL(REQUEST_URL+"?"+makeQueryString(paramMap));
            HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();
 
            // GET 방식으로 요청
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
 
            // 응답(Response) 구조 작성
            //   - Stream -> JSONObject
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String readline = null;
            StringBuffer response = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                response.append(readline);
            }
 
            // JSON 객체로  변환
            JSONObject responseBody = new JSONObject(response.toString());
 
            // 데이터 추출
            JSONObject movieInfoResult = responseBody.getJSONObject("movieInfoResult").getJSONObject("movieInfo");

            //일단 코비스 영역 다시 데이터 추출
            {
                // "nations" 키의 값인 JsonArray를 추출
                JSONArray nationsArray = movieInfoResult.getJSONArray("nations");
                Iterator<Object> nationsIter = nationsArray.iterator();
                List<String> nationList = new ArrayList<>();;
                while(nationsIter.hasNext()){
                    JSONObject object = (JSONObject)nationsIter.next();
                    nationList.add(object.getString("nationNm"));
                }
                kobisMovieInfo.setNationNm(nationList);  

                // "genres" 키의 값인 JsonArray를 추출
                JSONArray genreArray = movieInfoResult.getJSONArray("genres");
                Iterator<Object> genreIter = genreArray.iterator();
                List<String> genreList = new ArrayList<>();
                while (genreIter.hasNext()) {
                    JSONObject object = (JSONObject)genreIter.next();
                    genreList.add(object.getString("genreNm"));
                }
                kobisMovieInfo.setGenreNm(genreList);

                // "directors" 키의 값인 JsonArray를 추출
                JSONArray directorsArray = movieInfoResult.getJSONArray("directors");
                Iterator<Object> directorsIter = directorsArray.iterator();
                List<Directors> directorsList = new ArrayList<>();
                while (directorsIter.hasNext()) {
                    JSONObject object = (JSONObject)directorsIter.next();
                    Directors director = new Directors();
                    director.setPeopleNm(object.getString("peopleNm"));
                    director.setPeopleNmEn(object.getString("peopleNmEn"));
                    directorsList.add(director);
                }
                kobisMovieInfo.setDirectors(directorsList);

                // "actors" 키의 값인 JsonArray를 추출
                JSONArray actorsArray = movieInfoResult.getJSONArray("actors");
                Iterator<Object> actorsIter = actorsArray.iterator();
                List<Actors> actorsList = new ArrayList<>();
                while (actorsIter.hasNext()) {
                    JSONObject object = (JSONObject)actorsIter.next();
                    Actors actor = new Actors();
                    actor.setPeopleNm(object.getString("peopleNm"));
                    actor.setPeopleNmEn(object.getString("peopleNmEn"));
                    actor.setCast(object.getString("cast"));
                    actor.setCastEn(object.getString("castEn"));
                    actorsList.add(actor);
                }
                kobisMovieInfo.setActors(actorsList);

                // "companys" 키의 값인 JsonArray를 추출
                JSONArray companysArray = movieInfoResult.getJSONArray("companys");
                Iterator<Object> companysIter = companysArray.iterator();
                List<Companys> companysList = new ArrayList<>();
                while (companysIter.hasNext()) {
                    JSONObject object = (JSONObject)companysIter.next();
                    Companys company = new Companys();
                    company.setCompanyCd(object.getString("companyCd"));
                    company.setCompanyNm(object.getString("companyNm"));
                    company.setCompanyNmEn(object.getString("companyNmEn"));
                    company.setCompanyPartNm(object.getString("companyPartNm"));
                    companysList.add(company);
                }
                kobisMovieInfo.setCompanys(companysList);

                // "audits" 키의 값인 JsonArray를 추출
                JSONArray auditsArray = movieInfoResult.getJSONArray("audits");
                Iterator<Object> auditsIter = auditsArray.iterator();
                List<Audits> auditsList = new ArrayList<>();
                while (auditsIter.hasNext()) {
                    JSONObject object = (JSONObject)auditsIter.next();
                    Audits audits = new Audits();
                    audits.setAuditNo(object.getString("auditNo"));
                    audits.setWatchGradeNm(object.getString("watchGradeNm"));
                    auditsList.add(audits);
                }
                kobisMovieInfo.setAudits(auditsList);

                kobisMovieInfo.setMovieNm(movieInfoResult.getString("movieNm"));
                kobisMovieInfo.setMovieNmEn(movieInfoResult.getString("movieNmEn"));
                kobisMovieInfo.setMovieNmOg(movieInfoResult.getString("movieNmOg"));
                kobisMovieInfo.setPrdtYear(movieInfoResult.getString("prdtYear"));
                kobisMovieInfo.setShowTm(movieInfoResult.getString("showTm"));
                kobisMovieInfo.setOpenDt(movieInfoResult.getString("openDt"));
                kobisMovieInfo.setPrdtStatNm(movieInfoResult.getString("prdtStatNm"));
                kobisMovieInfo.setTypeNm(movieInfoResult.getString("typeNm"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //반환
        return kobisMovieInfo;
    }

    public MovieUrlEntity requestUrl(String movieName, String movieYear) throws IOException{

        //반환할 데이터 객체 생성
        MovieUrlEntity entity = new MovieUrlEntity();

        //search-movie 먼저 여기서는 영화 코드만 추출하면 됨.
        OkHttpClient client = new OkHttpClient();
        String strMovieName = movieName;
        String strYear = movieYear;
        Long lMovieCode = 0L;

        {
            String reqUrl = String.format("https://api.themoviedb.org/3/search/movie?query=%s&include_adult=false&language=ko-KR&video&primary_release_year=%s&page=1", strMovieName, strYear);

            //요청 url, query(영화명)와 primary_release_year(년도)는 나중에 인자값으로 전달받아서 처리
            Request request = new Request.Builder()
                .url(reqUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyM2U5NWU1MTY0NWUzYjgwZDU0MzQyNGQxYTA5ODg0YSIsInN1YiI6IjY2MDEzYjRmNzcwNzAwMDE2MzBhZjg0MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.RFfawiMrE8C2YgpGPdaU5IOcl-R5t-JIquRBN6vaLzU")
                .build();

            Response response = client.newCall(request).execute();

            // 응답 데이터를 JSON 형식으로 파싱
            String responseData = response.body().string();

            // JSON 객체로  변환
            JSONObject responseBody = new JSONObject(responseData.toString());

            // String strResults = responseBody.getString("results");
            // 영화 코드 추출
            JSONArray arrMovieInfo = responseBody.getJSONArray("results");
            Iterator<Object> iter = arrMovieInfo.iterator();
            JSONObject jnMovieInfoParts = (JSONObject)iter.next();

            //키 입력하면 값 반환, 아래는 예시(overview라는 키를 입력)
            lMovieCode = jnMovieInfoParts.getLong("id");
            System.out.println("id : "+ lMovieCode);
        }

        {
            //movie-detail, 추출할 데이터: 
            //(소개글) tmdb, overview, 
            //(포스트) tmdb, poster_path,
            //스틸컷이미지,  backdrop_path
            //트레일러영상,  videos <= 배열 형식으로 받아야 함
            String reqUrl = String.format("https://api.themoviedb.org/3/movie/%d?append_to_response=videos,credits&language=ko-KR", lMovieCode);
            Request request = new Request.Builder()
            .url(reqUrl)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyM2U5NWU1MTY0NWUzYjgwZDU0MzQyNGQxYTA5ODg0YSIsInN1YiI6IjY2MDEzYjRmNzcwNzAwMDE2MzBhZjg0MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.RFfawiMrE8C2YgpGPdaU5IOcl-R5t-JIquRBN6vaLzU")
            .build();

            Response response = client.newCall(request).execute();

            // 응답 데이터를 JSON 형식으로 파싱
            String responseData = response.body().string();

            // JSON 객체로  변환
            JSONObject responseBody = new JSONObject(responseData.toString());
            String strOverView = responseBody.getString("overview");
            String strPosterPath = responseBody.getString("poster_path");
            String strStillCut = responseBody.getString("backdrop_path");
            JSONObject jnVideos = responseBody.getJSONObject("videos");
            JSONArray arrResult = jnVideos.getJSONArray("results");
            JSONObject jnResult = arrResult.getJSONObject(0);
            String strTrailerURL = jnResult.getString("key");


            entity.setId(lMovieCode);
            entity.setOverView(strOverView);
            entity.setPosterUrl(strPosterPath);
            entity.setStillcutUrl(strStillCut);
            entity.setTrailerUrl(strTrailerURL);
        }

        return entity;
    };


    public static void main(String[] args) {
        // API 객체 생성
        KobisMovieAPI api = new KobisMovieAPI();
        // api.searchDailyBoxOfficeList(); // 작동 확인
        // api.searchMovieInfo("20228797");// 범죄도시로 테스트
 
        // API 요청
        // api.requestAPI();
        // api.requestBoxDailly();
    }
}