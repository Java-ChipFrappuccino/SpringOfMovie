package kr.co.moviespring.web.controller.api;

import kr.co.moviespring.web.config.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/playground")
public class PlayGroundService {

    //로그인 상태 체크
    @GetMapping("/checkLogin")
    public boolean checkLogin(@AuthenticationPrincipal CustomUserDetails userDetails){

        if(userDetails == null)
            return false;
        else
            return true;

    }

    @PostMapping("/betting-possible")
    public void bettingPossible(@RequestBody BettingRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){
        System.out.println("PBG ID: " + request.getPbgId());
        System.out.println("Betting Amount: " + request.getBettingAmount());

        // 사용자 정보 사용 예시
        System.out.println("User ID: " + userDetails.getId());
        System.out.println("Username: " + userDetails.getUsername());
        System.out.println("UserPoint: " + userDetails.getPoint());


    }



    public static class BettingRequest {
        private Long pbgId; // pbgId 필드
        private int bettingAmount; // bettingAmount 필드

        // Getter와 Setter 메서드
        public Long getPbgId() {
            return pbgId;
        }

        public void setPbgId(Long pbgId) {
            this.pbgId = pbgId;
        }

        public int getBettingAmount() {
            return bettingAmount;
        }

        public void setBettingAmount(int bettingAmount) {
            this.bettingAmount = bettingAmount;
        }
    }

}

