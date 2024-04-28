package kr.co.moviespring.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.moviespring.web.entity.Actor;
import kr.co.moviespring.web.entity.Director;
import kr.co.moviespring.web.entity.Movie;
import kr.co.moviespring.web.entity.Movie3;
import kr.co.moviespring.web.entity.MovieActor;
import kr.co.moviespring.web.entity.MovieDirector;
import kr.co.moviespring.web.entity.MovieStillcut;
import kr.co.moviespring.web.movieapi.KobisMovieAPI;
import kr.co.moviespring.web.movieapi.TMDBMovieAPI;
import kr.co.moviespring.web.movieapi.dto.kobis.KobisMovieInfo;
import kr.co.moviespring.web.movieapi.dto.tmdb.TMDBMovieDetail;
import kr.co.moviespring.web.movieapi.dto.tmdb.TMDBPersonDetails;
import kr.co.moviespring.web.movieapi.dto.tmdb.sub.Cast;
import kr.co.moviespring.web.movieapi.dto.tmdb.sub.Crew;
import kr.co.moviespring.web.service.ActorService;
import kr.co.moviespring.web.service.DirectorService;
import kr.co.moviespring.web.service.MovieActorService;
import kr.co.moviespring.web.service.MovieDirectorService;
import kr.co.moviespring.web.service.MovieInsertService;
import kr.co.moviespring.web.service.MovieService;
import kr.co.moviespring.web.service.MovieStillcutService;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    MovieService movieService;

    @Autowired
    ActorService actorService;

    @Autowired
    DirectorService directorService;

    @Autowired
    MovieActorService movieActorService;

    @Autowired
    MovieDirectorService movieDirectorService;

    @Autowired
    MovieInsertService serviceTest;

    @Autowired
    MovieStillcutService stillcutService;


    @GetMapping("index")
    public String index(Model model) {
        List<Movie> list = movieService.getList();
        model.addAttribute("list", list);

        return "index";
    }

    @GetMapping("test")
    public String test() throws IOException, ParseException{

        // 일단 위에거 안쓰고 데이터 저장용 테스트
        TMDBMovieAPI api = new TMDBMovieAPI();
        KobisMovieAPI kobisApi = new KobisMovieAPI();
        String movieName = "THE ROUNDUP : PUNISHMENT";
        String year = "2024";

        // Movie3 목록 불러오기(테스트용, 일단 10개만, 20240425 이전만)
        List<Movie3> list = serviceTest.getMovieList();

        // 제목이랑 개봉일자(년도만 추출) 저장해서 영화정보 불러오기
        movieName = list.get(0).getMovieNmEn();
        year = list.get(0).getOpenDt().substring(0, 4); 
        
        TMDBMovieDetail md = api.movieDetail(movieName, year); //영화 정보를 불러옴

        //db에 넣을 entity
        Movie movie = new Movie();
        

        // Movie 저장. 심의등급, 장르, 한글명, 영어명, 개봉년도, kobis코드는 먼저 저장
        movie.setGenre(list.get(0).getRepGenreNm());
        movie.setEngName(list.get(0).getMovieNmEn());
        movie.setKorName(list.get(0).getMovieNm());
        
        // 날짜 파싱
        String dateString = list.get(0).getOpenDt();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date releaseDate = dateFormat.parse(dateString);
        movie.setReleaseDate(releaseDate);
        movie.setReleaseNation(list.get(0).getNationAlt());
        movie.setKobisId(list.get(0).getMovieCd());

        // kobis 영화디테일에서 채움
        KobisMovieInfo mi = kobisApi.searchMovieInfo(movie.getKobisId());
        movie.setWatchGrade(mi.getAudits().get(0).getWatchGradeNm());
        movie.setSponsor(mi.getCompanys().get(0).getCompanyNm());

        // tmdb에서 채움
        movie.setTmdbId(md.getId());
        movie.setMovieIntro(md.getOverview());
        movie.setPosterUrl("https://image.tmdb.org/t/p/original" + md.getPosterPath());
        movie.setRunningTime(md.getRuntime());
        movie.setStillcutUrl("https://image.tmdb.org/t/p/original" + md.getBackdropPath());
        movie.setTrailerUrl("https://www.youtube.com/embed/" + md.getResults().get(0).getKey());

        Long movieID = movieService.saveMovie(movie);


        // 스틸컷 저장
        MovieStillcut stillcut = new MovieStillcut();
        stillcut.setMovieId(movieID);   
        for (String strUrl : md.getStillCuts()) {
            stillcut.setUrl(strUrl);
            stillcutService.add(stillcut);   
        }

        // actor 테이블 저장
        List<Cast> casts = md.getCasts();
        for (Cast cast : casts) {
            Actor actor = new Actor();
            actor.setEngName(cast.getOriginalName());
            actor.setImgUrl("https://image.tmdb.org/t/p/original"+cast.getProfilePath());
            actor.setTmdbId(cast.getId());
            actor.setPopularity(Double.parseDouble(cast.getPopularity()));
            TMDBPersonDetails pd = api.personDetails(cast.getId());
            actor.setKorName(pd.getKorName());

            // 리스트에 저장할 필요가 있음?
            // actors.add(actor);

            // db에 저장
            Long actorId = actorService.add(actor);
            
            // movieActor 테이블 저장
            {
                MovieActor movieActor = new MovieActor();
                movieActor.setActorId(actorId);
                movieActor.setMovieId(movieID);
                movieActor.setCastEngName(cast.getCharacter());
                // movieActor.setCastKorName(year); 한국배역명은 일단 보류
                movieActor.setCastOrder(Long.parseLong(cast.getCastOrder()));

                // 리스트에 저장할 필요가 있나?
                // movieActors.add(movieActor);

                //db에 저장
                movieActorService.add(movieActor);
            }
        }

        // director 테이블 저장
        List<Crew> crews = md.getCrews();
        for (Crew crew : crews) {
            Director director = new Director();
            director.setEngName(crew.getOriginalName());
            director.setImgUrl("https://image.tmdb.org/t/p/original"+crew.getProfilePath());
            director.setTmdbId(crew.getId());
            director.setPopularity(Double.parseDouble(crew.getPopularity()));
            TMDBPersonDetails pd = api.personDetails(crew.getId());
            director.setKorName(pd.getKorName());

            // 리스트에 저장할 필요가 없는것같음?
            // directors.add(director);

            // db에 저장
            Long directorId = directorService.add(director);

            // movieDirector 테이블 저장
            {
                MovieDirector movieDirector = new MovieDirector();
                movieDirector.setDirectorId(directorId);
                movieDirector.setMovieId(movieID);

                // movieDirectors.add(movieDirector);
                // db에 저장
                movieDirectorService.add(movieDirector);
            }
        }
        return "index";
    }

}
