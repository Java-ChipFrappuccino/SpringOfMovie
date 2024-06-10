package kr.co.moviespring.web.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.moviespring.web.config.security.CustomUserDetails;
import kr.co.moviespring.web.entity.EventPage;
import kr.co.moviespring.web.service.EventPageService;

@Controller("AdminMovieEventController") //이름의 중복을 피하기 위해 ioc컨테이너에 담기는 이름을 직접 작성해줄수있다
@RequestMapping("admin/movie-event")
public class MovieEventController {

    @Autowired
    EventPageService eventPageService;

    @GetMapping("list")
    public String movieEventList(
        Model model
    ) {
        List<EventPage> list = eventPageService.getList();
        model.addAttribute("list", list);
        return "/admin/movie-event/list"; 
    }

    @GetMapping("reg")
    public String movieEventReg(
        
    ) {
        return "admin/movie-event/reg"; 
    }

    @PostMapping("reg")
    public String movieEventReg(
        EventPage ep
        ,@RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sttDate
        ,@RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
        ,@AuthenticationPrincipal CustomUserDetails userDetails
    ){
        ep.setStartDate(sttDate);
        ep.setEndDate(endDate);
        eventPageService.reg(ep, userDetails.getId());

        return "redirect:/admin/movie-event/list";
    }

    @GetMapping("edit")
    public String movieEventEdit(
        @RequestParam("id") Long id
        , Model model
    ){
        EventPage ep = eventPageService.getById(id);
        model.addAttribute("menu", ep);
        return "/admin/movie-event/edit";
    }

    @PostMapping("edit")
    public String movieEventEdit(
        EventPage eventPage
        ,@RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sttDate
        ,@RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ){
        eventPage.setStartDate(sttDate);
        eventPage.setEndDate(endDate);
        eventPageService.edit(eventPage);
        return "redirect:/admin/movie-event/list";
    }

    @GetMapping("delete")
    public String movieEventDelete(
        @RequestParam("id") Long id
    ){
        eventPageService.deleteById(id);
        return "redirect:/admin/movie-event/list";
    }
}
