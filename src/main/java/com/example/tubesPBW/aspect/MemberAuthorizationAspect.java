package com.example.tubesPBW.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.tubesPBW.annotation.RequiresMember;

@Aspect
@Component
public class MemberAuthorizationAspect {

    @Around("@annotation(requiresMember)")
    public Object checkMemberAuthorization(ProceedingJoinPoint joinPoint, RequiresMember requiresMember) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false); 
        if (session == null) {
            return "redirect:/login?error=session_expired";
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null) {
            session.invalidate();
            return "redirect:/login?error=invalid_session";
        }
        if (!"member".equals(userRole)) {
            return "redirect:/access-denied";
        }
        return joinPoint.proceed();
    }
}