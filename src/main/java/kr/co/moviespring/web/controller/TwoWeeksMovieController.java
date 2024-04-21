package kr.co.moviespring.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("2weeks")
public class TwoWeeksMovieController {

    // 현재 url을 얻어서 모델에 넣어줌
    @ModelAttribute("url")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }
    
    @GetMapping("movie")
    public String movie (){
        return "2weeks/movie";
    }

    @GetMapping("list")
    public String list (){
        return "2weeks/list";
    }
}
