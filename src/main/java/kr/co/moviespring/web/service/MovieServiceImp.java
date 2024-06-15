package kr.co.moviespring.web.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import kr.co.moviespring.web.config.batch.BatchSchedulerConfig;
import kr.co.moviespring.web.entity.Movie;
import kr.co.moviespring.web.entity.Movie3;
import kr.co.moviespring.web.entity.MovieSearchView;
import kr.co.moviespring.web.movieapi.KobisMovieAPI;
import kr.co.moviespring.web.movieapi.TMDBMovieAPI;
import kr.co.moviespring.web.movieapi.dto.kobis.KobisDailyBox;
import kr.co.moviespring.web.movieapi.dto.kobis.KobisMovieInfo;
import kr.co.moviespring.web.movieapi.dto.tmdb.TMDBMovieDetail;
import kr.co.moviespring.web.repository.MovieInsertRepository;
import kr.co.moviespring.web.repository.MovieRepository;

@Service
public class MovieServiceImp implements MovieService {

    // 모든 목록 가져오기//
    @Autowired
    MovieRepository repository;

    @Autowired
    MovieInsertRepository movieInsertRepository;

    // 프로젝트 로드시 한번 실행되는 어노테이션
    @PostConstruct
    public void movieInit() throws IOException, ParseException {
        List<Movie> list = new ArrayList<>();
        KobisMovieAPI api = new KobisMovieAPI();
        TMDBMovieAPI tmdbApi = new TMDBMovieAPI();
        List<KobisDailyBox> koList = api.searchDailyBoxOfficeList();
        for (KobisDailyBox kobisDailyBox : koList) {
            String movieCd = kobisDailyBox.getMovieCd();
            if(movieInsertRepository.findByCode(movieCd) == null){
                KobisMovieInfo movieInfo = api.searchMovieInfo(movieCd);
                Movie3 movie = new Movie3();
                movie.setAudiAcc(null);
                movie.setCompanyCd(movieInfo.getCompanys().get(0).getCompanyCd());
                movie.setDirectorNm(movieInfo.getDirectors().get(0).getPeopleNm());
                movie.setMovieCd(movieCd);
                movie.setMovieNm(movieInfo.getMovieNm());
                movie.setMovieNmEn(movieInfo.getMovieNmEn());
                movie.setNationAlt(movieInfo.getNationNm().get(0));
                movie.setOpenDt(movieInfo.getOpenDt());
                movie.setPrdYear(movieInfo.getPrdtYear());
                movie.setRepGenreNm(movieInfo.getGenreNm().get(0));
                movieInsertRepository.save(movie);
            }


            Movie movie = this.getByKobisId(kobisDailyBox.getMovieCd());

            // 영화진흥위원회에서 일별박스오피스를 받는데 고전영화가 들어옴, 버그인듯
            // api 요청 파라미터 개수를 최소화했더니 일단 해결됨. 예외처리에 대해 고민
            // 영화가 없으면 다시 등록이 필요함. 일단 보류
            // if(movie == null){
            //     TMDBMovieDetail tmdbMovieDetail = new TMDBMovieDetail();
            //     // 현재 년도만 추출
            //     LocalDate currentDate = LocalDate.now();
            //     int year = currentDate.getYear();
            //     String strYear= String.valueOf(year);

            //     KobisMovieInfo kmi = api.searchMovieInfo(kobisDailyBox.getMovieCd());
            //     Long tmdbCode = tmdbApi.serchMovie(kmi.getMovieNm(), strYear);
            //     // 일단 한국어 없으면 영문으로
            //     if(tmdbCode == null)
            //         tmdbCode = tmdbApi.serchMovie(kmi.getMovieNmEn(), strYear);

            //     tmdbMovieDetail = tmdbApi.movieDetail(tmdbCode);

            //     if(tmdbMovieDetail == null){
            //         System.out.println("tmdb에 해당 영화가 존재하지 않습니다.");
            //         System.out.println(kmi.getMovieNm());
            //         System.out.println(kmi.getMovieNmEn());
            //         return;
            //     }

            //     movie.setGenre(kmi.getGenreNm().get(0));
            //     movie.setEngName(kmi.getMovieNmEn());
            //     movie.setKorName(kmi.getMovieNm());

            //     // 날짜 파싱해서 넣기
            //     String dateString = kmi.getOpenDt();
            //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            //     Date releaseDate = dateFormat.parse(dateString);
            //     movie.setReleaseDate(releaseDate);
            //     movie.setReleaseNation(kmi.getNationNm().get(0));
            //     movie.setKobisId(kobisDailyBox.getMovieCd());

            //     movie.setWatchGrade(kmi.getAudits().get(0).getWatchGradeNm());
            //     movie.setSponsor(kmi.getCompanys().get(0).getCompanyNm());

            //     // tmdb에서 채움
            //     movie.setTmdbId(tmdbMovieDetail.getId());
            //     movie.setMovieIntro(tmdbMovieDetail.getOverview());
            //     movie.setPosterUrl(tmdbMovieDetail.getPosterPath());
            //     movie.setRunningTime(tmdbMovieDetail.getRuntime());
            //     movie.setLogoUrl(tmdbMovieDetail.getLogo());
            //     movie.setMainImgUrl(tmdbMovieDetail.getBackdropPath());

            //     // movie를 db에 저장하고 생성된 ID 가져옴
            //     Long movieID = repository.save(movie);
            // }

            list.add(movie);
        }
        BatchSchedulerConfig.setList(list);
    }

    @Override
    public List<Movie> getList(Integer page) {
        int size = 10;
        int offset = (page-1);
        List<Movie> list = repository.findAll(offset, size);
        return list;
    }

    @Override
    public List<Movie> getListByYear() {
        List<Movie> list = repository.findAllByYear();
        return list;
    }

    @Override
    public List<Movie> getListAfter() {
        List<Movie> list = repository.findAllAfter();
        return list;
    }
    // 검색값으로 영화목록 가져오기//
    @Override
    public List<Movie> getListByName(String query) {

        List<Movie> list;
        if (query == null || query.trim().isEmpty()) {
            list = null;
        } else {
            list = repository.findAllByName(query);
        }
        return list;
    }

    //인물별 필모리스트
    @Override
    public List<Movie> getListByPeopleId(Long id, String type) {
        List<Movie> list = repository.findAllByPeopleId(id, type);
        return list;
    }

    @Override
    public List<MovieSearchView> getListView(Integer page) {
        return getListView(page, null);
    }

    @Override
    public List<MovieSearchView> getListView(Integer page, String query) {
        int size = 10;
        int offset = (page-1) * size;
        return repository.findAllByQuery(query, offset, size);
    }


    // 요청 id값으로 상세정보가져오기//
    @Override
    public Movie getById(Long id) {
        Movie movie = repository.findById(id);
        return movie;
    }

    @Override
    public Movie getByTMDBId(String id) {
        Movie movie = repository.findByTMDBId(id);
        return movie;
    }
    @Override
    public Movie getByKobisId(String id) {
        Movie movie = repository.findByKobisId(id);
        return movie;
    }

    // 저장하고 생성된 영화 ID를 받아와서 리턴 //
    @Override
    public Long saveMovie(Movie movie) {
        repository.save(movie);
        Long movieId = movie.getId();
        return movieId;
    }

    //카테고리별 영화검색후 등록(2주의영화 관리자)
    public void findByGenre(String childSelectValue) {
        repository.getByGenre(childSelectValue);
    }
    @Override
    public void findByReleseDate(String childSelectValue) {
        repository.getByReleseDate(childSelectValue);

    }
    @Override
    public void findByDistributor(String childSelectValue) {
        repository.getByDistributor(childSelectValue);
    }
    @Override
    public  List<Movie>  findAllEditedList() {
        List<Movie>  movieWeeks = repository.getEditedList();
        return movieWeeks;
    }

    @Override
    public int getCount() {
        return repository.getCount(null);
    }

    @Override
    public int getCount(String query){
        return repository.getCount(query);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(id);
    }

}