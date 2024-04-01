package kr.co.moviespring.web.filter;

import java.io.IOException;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

// @Component("/") //스프링이 지원하기 때문에 쓰지않음
public class AuthorityFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                System.out.println("필터 작동함?");
                chain.doFilter(request, response); //들어오고 나갈때도 연결을 해줘야 한다
    }

    
}