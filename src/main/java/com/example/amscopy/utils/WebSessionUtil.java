package com.example.amscopy.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.dto.UserLoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WebSessionUtil {

    public static final String LOGIN_USER_ID = "amsUserId";
    public static final String VERIFY_CODE = "verifyCode";
    public static final String LOGIN_USER_JSON = "amsUserLoginJson";
    public static final String GET_PASS_CAPTCH = "getPassCaptch";

    public static final void saveLoginUser(HttpServletRequest request, UserLoginDto userLoginDto) {
        setSession(request, LOGIN_USER_JSON, JSONObject.toJSONString(userLoginDto));
        saveSessionIdByLoginId(request, userLoginDto.getLoginId(), false);
    }

    public static final void removeLoginUser(HttpServletRequest request) {
        String loginId = getLoginId(request);
        Object sessionId = getSessionId(request);
        String sessionByLoginId = getSessionByLoginId(request, loginId);
        if (sessionId != null && sessionId.equals(sessionByLoginId)) {
            //最近一次登录退出，清除消息
            saveSessionIdByLoginId(request, loginId, true);
        }
        setSession(request, LOGIN_USER_JSON, null);
        invalidateSession(request);
    }

    public static final UserLoginDto getLoginUser(HttpServletRequest request) {
        String userLoginJson = getSession(request, LOGIN_USER_JSON);
        try {
            return JSONObject.parseObject(userLoginJson, UserLoginDto.class);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    public static final String getLoginUserId(HttpServletRequest request) {
        UserLoginDto loginUser = getLoginUser(request);
        return loginUser != null ? "" + loginUser.getUserId() : null;
    }

    public static final String getLoginUserName(HttpServletRequest request) {
        UserLoginDto loginUser = getLoginUser(request);
        return loginUser != null ? "" + loginUser.getUserName() : null;
    }

    public static final String getLoginId(HttpServletRequest request) {
        UserLoginDto loginUser = getLoginUser(request);
        return loginUser != null ? "" + loginUser.getLoginId() : null;
    }

    public static final Integer getOrgId(HttpServletRequest request) {
        UserLoginDto loginUser = getLoginUser(request);
        return loginUser != null ? loginUser.getOrgId() : null;
    }

    public static final void saveCaptcha(HttpServletRequest request, int captcha) {
        setSession(request, VERIFY_CODE, captcha);
    }

    public static final Integer getCaptcha(HttpServletRequest request) {
        return getSession(request, VERIFY_CODE);
    }

    public static final void removeCaptcha(HttpServletRequest request) {
        setSession(request, VERIFY_CODE, null);
    }

    public static final Integer getPassCaptcha(HttpServletRequest request) {
        return getSession(request, GET_PASS_CAPTCH);
    }

    public static final void removePassCaptcha(HttpServletRequest request) {
        setSession(request, GET_PASS_CAPTCH, null);
    }

    public static boolean isValidCaptcha(HttpServletRequest request, int captcha) {
        Integer sessionCaptcha = getCaptcha(request);
        log.info("session captcha is {},and sessionId is {}", sessionCaptcha, request.getSession().getId());
        removeCaptcha(request);
        return sessionCaptcha != null && sessionCaptcha == captcha;
    }

    public static boolean isValidGetPassCaptcha(HttpServletRequest request, int captcha) {
        Integer sessionGetPassCaptcha = getPassCaptcha(request);
        log.info("session captcha is {},and sessionId is {}", sessionGetPassCaptcha, request.getSession().getId());
        removePassCaptcha(request);
        return sessionGetPassCaptcha != null && sessionGetPassCaptcha == captcha;
    }

    public static final void setSession(HttpServletRequest request, String key, Object value) {
        HttpSession session = request.getSession(true);
        if (null != session) session.setAttribute(key, value);
    }

    public static final void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        if (null != session) {
            session.invalidate();
        }
    }

    public static final void saveSessionIdByLoginId(HttpServletRequest request, String loginId, boolean cleanSessionId) {
        StringRedisTemplate redisTemplate = ApplicationContextUtil.getBean(StringRedisTemplate.class);
        HttpSession session = request.getSession();
        if (null != redisTemplate && null != session) {
            if (cleanSessionId) {
                redisTemplate.delete(LOGIN_USER_ID + loginId);
            } else {
                redisTemplate.opsForValue().set(LOGIN_USER_ID + loginId, session.getId(), 10 * 60, TimeUnit.SECONDS);
            }
        }
    }

    public static final <T> T getSession(HttpServletRequest request, String key) {
        return (T) request.getSession(true).getAttribute(key);
    }

    public static final <T> T getSessionId(HttpServletRequest request) {
        return (T) request.getSession(true).getId();
    }

    public static final String getSessionByLoginId(HttpServletRequest request, String loginId) {
        StringRedisTemplate redisTemplate = ApplicationContextUtil.getBean(StringRedisTemplate.class);
        return redisTemplate != null ? redisTemplate.opsForValue().get(LOGIN_USER_ID + loginId) : null;
    }

}
