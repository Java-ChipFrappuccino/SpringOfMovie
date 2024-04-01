package kr.co.moviespring.web.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.moviespring.web.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.moviespring.web.memberService.MemberService;

@Controller
@RequestMapping("user")
public class MemberController {
    // 로그인 공사중//
    @Autowired
    MemberService memberService;

    @GetMapping("signin")
    public String signin() {

        return "user/signin";
    }

    @PostMapping("signin")
    public String signin(String userId, String pwd, HttpSession session, HttpServletResponse response) {
        boolean valid = memberService.validate(userId, pwd);
        System.out.println("포스트 요청");
        if (!valid) {
            return "redirect:signin?error";
        }

        // Cookie uidCookie = new Cookie("uid","1");
        // uidCookie.setPath("/");
        // // uidCookie.setMaxAge(0);
        // // uidCookie.setSecure(false);
        // uidCookie.setHttpOnly(true);
        //
        // Cookie userIdCookie = new Cookie("userId",userId);
        // userIdCookie.setPath("/");
        //
        // response.addCookie(uidCookie);
        // response.addCookie(userIdCookie);

        return "redirect:/index";
    }

    // 회원가입 공사중//
    @GetMapping("regist")
    public String regist() {

        return "user/regist";
    }

    @PostMapping("regist")
    public String regist(String userId, String pwd, String name, String nickname, int age, String email) {

        memberService.regist(userId, pwd, name, nickname, age, email);

        return "redirect:/user/welcome";
    }

    @GetMapping("welcome")
    public String welcome() {

        return "user/welcome";
    }

}
