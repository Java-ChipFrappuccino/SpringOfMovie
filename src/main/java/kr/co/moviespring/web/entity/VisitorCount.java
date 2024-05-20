package kr.co.moviespring.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorCount {

    private Long id;
    private LocalDate date;
    private Long totalVisitors;
    private Long todayVisitors;
}
