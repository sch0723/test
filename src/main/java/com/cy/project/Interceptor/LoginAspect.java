package com.cy.project.Interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class LoginAspect {

    @Pointcut("execution(* com.cy.project.controller.CartController.cart(..))||" +
            "execution(* com.cy.project.controller.OrdersController.checkout(..))||" +
            "execution(* com.cy.project.controller.OrdersController.*Orders*(..))")
    private void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        Object users = session.getAttribute("users");

        if(users == null){
            //儲存攔截前請求供登入完成使用
            session.setAttribute("preURI",request.getRequestURI());

            request.setAttribute("msg","請登錄");
            //未登入轉向登入頁
            return "login";
        }
        Object proceed = joinPoint.proceed();

        session.removeAttribute("preURI");
        return proceed;
    }


}
