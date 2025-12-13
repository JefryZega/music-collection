package com.example.tubesPBW.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AdminAuthorizationAspect {

    @Around("@annotation(com.example.tubesPBW.annotation.RequiresAdmin) || @within(com.example.tubesPBW.annotation.RequiresAdmin)")
    public Object checkAdminAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false); 

        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/login?error=session_expired";
        }
        
        String userRole = (String) session.getAttribute("userRole");
        
        if (userRole == null || !"admin".equals(userRole)) {
            return "redirect:/member/home?error=unauthorized"; 
        }

        return joinPoint.proceed();
    }
}