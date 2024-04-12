package kr.co.moviespring.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//롬복에서 주는 기능
@Data //게터 세터 ToString 생성자 해쉬코드 이퀄을 제공해준다.
@Builder //객체 생성 과정을 단순화 , 가독성을 높힘, 유연성을 확보, 불변성을 확보.
@NoArgsConstructor // 기본 생성자를 자동으로 생성해줌.
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자를 자동으로 생성해줌.
public class Movie2 {
    private String movieCd; // 영화 코드(PK)
    private String movieNm; // 영화한국이름
    private String openDt; //날짜 일단 string으로
    private int salesAmt; //해당 매출액
    private int audiCnt; //해당 관객수
    private int audiAcc; // 누적 관객수
    private  String RepGenreNm; //대표 장르



}