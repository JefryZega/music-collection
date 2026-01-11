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
    public Object checkMemberAuthorization(
            ProceedingJoinPoint joinPoint,
            RequiresMember requiresMember
    ) throws Throwable {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            attributes.getResponse().sendRedirect("/login?error=session_expired");
            return null; // ⬅ STOP chain
        }

        String userRole = (String) session.getAttribute("userRole");

        if (userRole == null) {
            session.invalidate();
            attributes.getResponse().sendRedirect("/login?error=invalid_session");
            return null;
        }

        if (!"member".equalsIgnoreCase(userRole)) {
            attributes.getResponse().sendRedirect("/access-denied");
            return null;
        }

        return joinPoint.proceed();
    }
}
