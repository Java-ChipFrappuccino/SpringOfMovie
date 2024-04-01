package kr.co.moviespring.web.entity;

import java.util.Date;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    
 private Long id;
 private String userId;
 private String pwd;
 private String name;
 private String nickname;
 private int age;
 private String email;
 private int point;
 private Date reg_date;
 private String profile_image_url;
 private boolean is_with_drawn;// tinyint(1)
}
