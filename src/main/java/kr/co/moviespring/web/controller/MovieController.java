package kr.co.moviespring.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.moviespring.web.entity.Movie;
import kr.co.moviespring.web.entity.OnelineReview;
import kr.co.moviespring.web.movieapi.DailyBoxEntity;
import kr.co.moviespring.web.movieapi.MovieAPI;
import kr.co.moviespring.web.movieapi.MovieInfoEntity;
import kr.co.moviespring.web.entity.Movie;
import kr.co.moviespring.web.service.CommentService;
import kr.co.moviespring.web.service.MovieService;

@Controller
@RequestMapping("movie")
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    CommentService commentService;

    // 영화 목록//
    @GetMapping("list")
    public String list(Model model) {

        List<Movie> list = movieService.getList();

        //영화 받아오기 테스트
        // List <Movie> mlist = new ArrayList<>();
        // MovieAPI api = new MovieAPI();
        // List<MovieInfoEntity> miList = new ArrayList<>();
        // List<DailyBoxEntity> dbeList = new ArrayList<>();
        // dbeList = api.requestBoxDailly();
        // for (DailyBoxEntity dailyBoxEntity : dbeList) {
        //     MovieInfoEntity movieInfo;
        //     movieInfo = api.requestMovieInfo(dailyBoxEntity.getMovieCd());
        //     miList.add(movieInfo);

        //     Movie m = new Movie();
        //     m.setId(Long.parseLong(dailyBoxEntity.getMovieCd()));
        //     m.setTotalBoxoffice(dailyBoxEntity.getRank());
        //     m.setDailyAudience(dailyBoxEntity.getAudiCnt());
        //     m.setTotalAudience(dailyBoxEntity.getAudiAcc());
        //     m.setTotalSales(Long.parseLong(dailyBoxEntity.getSalesAcc()));
        //     m.setKorName(movieInfo.getMovieNm());
        //     m.setEngName(movieInfo.getMovieNmEn());
        //     m.setReleaseDate(movieInfo.getOpenDt());
        //     m.setReleaseNationId(movieInfo.getNationNm());
        //     m.setGenreId(movieInfo.getGenreNm());

        //     mlist.add(m);
        // }

        


        model.addAttribute("list", list);
        return "movie/list";
    }

    // 영화 상세//
    @GetMapping("detail")
    public String detail(@RequestParam("movieid") Long movieId, Model model) {
        // 상세정보//
        Movie movie = movieService.getById(movieId);
        // 리뷰목록//
        List<OnelineReview> onelineReviews = commentService.getOnelineReviews(movieId);

        model.addAttribute("movie", movie);
        model.addAttribute("reviews", onelineReviews);

        return "movie/detail";
    }

    // @PostMapping("Comment")
    // public String comment(){

    // List<Comment> comments = commentService.SaveComment();

    // return "redirect:/movie/list";
    // }
    // 한줄평 등록//
    @PostMapping("comment")
    public String comment(String comments, int rate, @RequestParam("movie-id") Long movieId) {
        System.out.println("post 요청");

        commentService.saveComment(comments, rate, movieId);

        System.out.println("댓글작성");
        return "redirect:/movie/detail?movieid=" + movieId;
    }
}
