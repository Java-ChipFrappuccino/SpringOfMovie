package kr.co.moviespring.web.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.co.moviespring.web.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.co.moviespring.web.config.batch.BatchSchedulerConfig;
import kr.co.moviespring.web.config.security.CustomUserDetails;
import kr.co.moviespring.web.movieapi.dto.kobis.KobisDailyBox;
import kr.co.moviespring.web.service.MovieActorService;
import kr.co.moviespring.web.service.MovieDirectorService;
import kr.co.moviespring.web.service.MovieService;
import kr.co.moviespring.web.service.MovieStillcutService;
import kr.co.moviespring.web.service.MovieTrailerService;
import kr.co.moviespring.web.service.OnelineReviewService;
import kr.co.moviespring.web.service.TwoWeeksMovieService;

@Controller
@RequestMapping("movie")
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    OnelineReviewService onelineReviewService;

    @Autowired
    MovieActorService movieActorService;

    @Autowired
    MovieDirectorService movieDirectorService;

    @Autowired
    MovieStillcutService movieStillcutService;

    @Autowired
    MovieTrailerService movieTrailerService;
    @Autowired
    TwoWeeksMovieService TWMovieService;

    // 영화 목록//
    @GetMapping("main")
    public String main(Model model) {

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
        //     MovieUrlEntity movieUrl = api.requestUrl(movieInfo.getMovieNm(), movieInfo.getOpenDt());

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
        //     m.setTrailerUrl(movieUrl.getTrailerUrl());
        //     m.setPosterUrl(movieUrl.getPosterUrl());
        //     m.setStillcutUrl(movieUrl.getStillcutUrl());
        //     m.setMovieIntro(movieUrl.getOverView());
        //     mlist.add(m);
        // }
        // List<String> sList = BatchSchedulerConfig.getList();
        // List<Movie> dailyList = new ArrayList<>();
        // for (String mCode : sList) {
        //     Movie movie = movieService.getByKobisId(mCode);
        //     dailyList.add(movie);
        // }
        // 슬슬 없는 영화들 나와서 movie3을 채우던지, 없는 영화에 대해 새로 받는 로직을 추가할건지
        // 일단 일별박스오피스는 올해의 영화로 대체
        List<Movie> dailyList = BatchSchedulerConfig.getList();
        List<Movie> list = movieService.getListByYear();
        List<Movie> listAfter = movieService.getListAfter();

        // 서버로 돌면 풀어주기, 일단 그냥 리스트로
        if(dailyList == null)
            dailyList = list;

        model.addAttribute("dlist", dailyList);
        model.addAttribute("list", list);
        model.addAttribute("listAfter", listAfter);




        return "movie/main";
        
        
    }

    // 영화 상세//
    @GetMapping("detail")
    public String detail(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @RequestParam("movieid") Long movieId, Model model) {
        Long memberId = null;

        // 상세정보//
        Movie movie = movieService.getById(movieId);
        List<MovieActorView> actors = movieActorService.getById(movieId);
        List<Director> directors = movieDirectorService.getById(movieId); //디렉터 타입인데 무비디렉터 서비스 쓰는게 맞나?
        List<MovieStillcut> stillcuts = movieStillcutService.getById(movieId);
        List<totalVoteView> TWMovie = TWMovieService.findByMovieCd();
        List<MovieTrailer> trailers = movieTrailerService.getById(movieId);
        // 로그인한 회원이 쓴 리뷰가 있을경우
        if (userDetails != null) {
        memberId = userDetails.getId();
            OnelineReview review = onelineReviewService.getById(movieId, memberId);
            if (review != null)
            model.addAttribute("myReview", review);
        }

        // 전체 리뷰목록//
//        List<OnelineReviewView> onelineReviews = onelineReviewService.getList(movieId); // 필요 없을수도? 일단 보관
        // 기본값으로 작성일 최신순(newest)으로 불러옴, 멤버아이디를 넣는 이유는 해당멤버가 리뷰들에 좋아요를 했는지 여부를 쿼리에서 참조 하기 때문, 비회원일시 null값이 들어가고 쿼리결과값은 0으로 나옴(좋아요 하지않은 상태)
        List<OnelineReviewView> onelineReviews = onelineReviewService.getListByCategory(movieId,memberId,"newest");

        // 영화 평점 불러오기
        {
            model.addAttribute("avgRate", 15000); //리뷰가 없을경우 기본값 전송
            if (onelineReviews.size() != 0) {
                int total = 0;
                int avg = 0;
                for (int i = 0; i < onelineReviews.size(); i++) {
                    total += onelineReviews.get(i).getMemberRate();
                }
                avg = total / (onelineReviews.size());
                int intAvg = avg / 100 * 100; // 소수점 앞 2자리 잘라서 100단위까지만 나오게
                model.addAttribute("avgRate", intAvg); //유저 평점을 기준으로 평균가격 측정
            }
        }
        // for(totalVoteView TWMovies : TWMovie){
        //     if(TWMovies.getMovieCd() == movie.getId())

        // }
        
        model.addAttribute("movie2", TWMovie);
        model.addAttribute("movie", movie);
        model.addAttribute("actors", actors);
        model.addAttribute("directors", directors);
        model.addAttribute("reviews", onelineReviews);
        model.addAttribute("stillcuts", stillcuts);
        model.addAttribute("trailers", trailers);
        model.addAttribute("user", userDetails); //유저 정보 객체 넣어줌 테스트중
        

        return "movie/detail";
    }

    // @PostMapping("Comment")
    // public String comment(){

    // List<Comment> comments = commentService.SaveComment();

    // return "redirect:/movie/list";
    // }
    // 한줄평 등록//
    @PostMapping("comment")
    public String comment(String comments, @RequestParam(value = "rate", defaultValue = "15000") int rate, @RequestParam("movieid") Long movieId,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getId();
        onelineReviewService.saveComment(memberId, comments, rate, movieId);
        return "redirect:/movie/detail?movieid=" + movieId;
    }

    /*한줄평 삭제 */
    @PostMapping("comment/delete/{movieid}")
    public String delete(@PathVariable("movieid") Long movieId,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getId();
        onelineReviewService.deleteComment(memberId, movieId);
        return "redirect:/movie/detail?movieid=" + movieId;
    
    }
    // 한줄평 수정// //put으로 바꿔야함
    @PostMapping("comment/edit/{movieid}")
    public String edit(String comments , int rate, @PathVariable("movieid") Long movieId,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getId();
        onelineReviewService.editComment(memberId, comments, rate, movieId);

        return "redirect:/movie/detail?movieid=" + movieId;
    }

//    @GetMapping("actor")
//    public String actor() {
//
//        return "movie/people";
//    }

}
