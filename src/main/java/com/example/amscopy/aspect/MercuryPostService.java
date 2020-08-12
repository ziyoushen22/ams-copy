package com.example.amscopy.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;

@Component
@Slf4j
public class MercuryPostService {

    @Autowired
    private RestTemplate restTemplate;

    public void updateMercuryUser(Object o) {
        JSONObject jsonObject = new JSONObject();
        doMercuryPost("", jsonObject, null);
    }

    public JSONObject doMercuryPost(String mercuryPath, JSONObject params, String userId) {
        UriComponents build = UriComponentsBuilder.fromHttpUrl("")
                .path(mercuryPath)
                .build(true);
        URI uri = build.toUri();
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(uri, params, JSONObject.class);
        JSONObject body = responseEntity.getBody();
        return body;
    }

    public JSONObject doProductFactoryPost(String productFactorySevPath, JSONObject params) {
        UriComponents build = UriComponentsBuilder.fromHttpUrl("")
                .path(productFactorySevPath)
                .build(true);
        URI uri = build.toUri();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ArrayList<String> cookieList = new ArrayList<>();
        if (request != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if ("SESSION".equalsIgnoreCase(cookie.getName())) {
                        cookieList.add("SESSION=" + cookie.getValue());
                    }
                }
            }
        }
        RequestEntity<JSONObject> requestEntity = RequestEntity.post(uri)
                .header(HttpHeaders.COOKIE, cookieList.get(0))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(params);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject body = responseEntity.getBody();
        // TODO
        return null;

    }


}
