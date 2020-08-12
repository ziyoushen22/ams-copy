package com.example.amscopy.utils.esg;

import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.dto.ResponseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;

/**
 * 该类仅适用于通过平安esg平台进行服务转发业务
 */
@Component
@Data
@Slf4j
public class EsgUtil {
    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${esg.grantType}")
    private String grantType;

    @Value("${esg.clientId}")
    private String clientId;
    @Value("${esg.clientSecret}")
    private String clientSecret;
    @Value("${esg.accessTokenUrl}")
    private String accessTokenUrl;
    @Value("${esg.openApiUrl}")
    private String openApiUrl;

    private String token(String grantType, String clientId, String clientSecret) {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("client_id", clientId);
        jsonParams.put("grant_type", grantType);
        jsonParams.put("client_secret", clientSecret);
        EsgResponse esgResponse = this.esgPost(this.accessTokenUrl, jsonParams);
        if (!esgResponse.succeed()) {
            throw new IllegalStateException(esgResponse.getMsg());
        }
        JSONObject data = new JSONObject(esgResponse.getData());
        return data == null ? null : data.getString("access_token");
    }

    public ResponseEntity<JSONObject> postOpenApi(String appSrvPath, String requestId, JSONObject jsonParams) {
        return postOpenApi(appSrvPath, requestId, jsonParams, DEFAULT_CHARSET);
    }

    public ResponseEntity<JSONObject> postOpenApi(String appSrvPath, String requestId, JSONObject jsonParams, String encode) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(openApiUrl).path(appSrvPath)
                .queryParam("access_token", this.token(grantType, clientId, clientSecret))
                .queryParam("request_id", requestId)
                .build(true);
        URI uri = uriComponents.toUri();
        log.info("send request to {} with params {}", uri, jsonParams);
        EsgResponse esgResponse = this.esgPost(uri.toString(), jsonParams, encode);
        log.info("the esgResponse is {}", esgResponse);
        if (esgResponse.succeed()) {
            return new ResponseEntity<JSONObject>().success(esgResponse.getMsg(), esgResponse.getData());
        } else {
            return new ResponseEntity<JSONObject>().fail(esgResponse.getMsg());
        }
    }

    public EsgResponse esgPost(String url, JSONObject jsonParams) {
        return esgPost(url, jsonParams, DEFAULT_CHARSET);
    }

    public EsgResponse esgPost(String url, JSONObject jsonParams, String encode) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=" + encode);
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String body = jsonParams == null ? null : jsonParams.toString();
        if (!DEFAULT_CHARSET.equalsIgnoreCase(encode)) {
            body = writeToStr(body, encode);
        }
        String response = this.restTemplate.postForObject(url, new HttpEntity<>(body, headers), String.class);
        if (response == null) return null;
        JSONObject jsonObject = JSONObject.parseObject(response);
        String rtCode = jsonObject.getString("ret");
        if (rtCode == null || rtCode.length() == 0) {
            rtCode = jsonObject.getString("resultCode");
            if ("0".equalsIgnoreCase(rtCode)) {
                rtCode = "-1";
            }
            if ("200".equalsIgnoreCase(rtCode)) {
                rtCode = "0";
            }
        }
        String msg = jsonObject.getString("msg");
        if (StringUtils.isBlank(msg)) {
            msg = jsonObject.getString("message");
        }
        EsgResponse esgResponse = new EsgResponse();
        esgResponse.setRet(rtCode);
        esgResponse.setMsg(msg);
        Object data = jsonObject.get("data");
        try {
            esgResponse.setData(JSONObject.parseObject(data.toString()));
        } catch (Exception e) {
            log.error("转化错误", e);
        }
        return esgResponse;
    }

    private String writeToStr(String params, String charsetName) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        String repStr = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(params);
            repStr = bos.toString(charsetName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return repStr;
    }

    @Data
    @NoArgsConstructor
    static class EsgResponse {
        private String ret;
        private String msg;
        private JSONObject data;

        boolean succeed() {
            return "0".equals(ret);
        }
    }
}
