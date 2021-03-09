package com.cy.project.Interceptor;

import com.cy.project.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("preHandle請求路徑:{}",requestURI);

        HttpSession session = request.getSession();

        Users users = (Users) session.getAttribute("users");

        if(users != null){
            return true;
        }

        if ("/cart".equals(requestURI.substring(requestURI.lastIndexOf('/')))){
            Cookie cookie = new Cookie("preURI", "/cart");
            cookie.setMaxAge(600);
            response.addCookie(cookie);
        }

        request.setAttribute("msg","請登錄");
        request.getRequestDispatcher("/login").forward(request,response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        Cookie cookie = new Cookie("preURI", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
