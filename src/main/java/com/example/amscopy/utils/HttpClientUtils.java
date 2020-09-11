package com.example.amscopy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Map;

/**
 * httpClient使用
 * https绕过ssl验证
 */
@Slf4j
@Component
public class HttpClientUtils {

    private static PoolingHttpClientConnectionManager connMgr = null;
    private static RequestConfig requestConfig = null;
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final int MAX_POOL_NUM = 500;
    private static final int MAX_TIMEOUT = 100 * 60 * 1000; //10min

    static {
        connMgr = new PoolingHttpClientConnectionManager(); //设置连接池
        connMgr.setMaxTotal(MAX_POOL_NUM);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(MAX_TIMEOUT);
        builder.setSocketTimeout(MAX_TIMEOUT);
        builder.setConnectionRequestTimeout(MAX_TIMEOUT);
        builder.setRedirectsEnabled(true);
        requestConfig = builder.build();
    }

    public static String doGet(String url, Map<String, Object> params, Boolean isHttps) throws Exception {
        String apiUrl = url;
        ArrayList<NameValuePair> nameValuePairArrayList = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            BasicNameValuePair basicNameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
            nameValuePairArrayList.add(basicNameValuePair);
        }
        StringBuffer sb = new StringBuffer(apiUrl);
        if (!CollectionUtils.isEmpty(nameValuePairArrayList)) {
            sb.append("?").append(EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairArrayList, Consts.UTF_8)));
        }
        apiUrl = sb.toString();
        log.info("apiUrl: " + apiUrl);
        HttpGet httpGet = new HttpGet(apiUrl);
        String result = responseHandler(httpGet, isHttps);
        return result;
    }


    public static String doPost(String apiUrl, Map<String, Object> params) throws Exception {
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setConfig(requestConfig);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            BasicNameValuePair basicNameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
            nameValuePairs.add(basicNameValuePair);
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));
        String result = responseHandler(httpPost, false);
        return result;
    }


    private static String responseHandler(HttpRequestBase method, Boolean isHttps) throws Exception {
        CloseableHttpClient client = null;
        if (isHttps) {
            try {
                client = createSSLHttpClient();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            client = HttpClients.custom().setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        }
        HttpClientContext httpClientContext = HttpClientContext.create();
        CloseableHttpResponse response = null;
        String content = "";
        try {
            response = client.execute(method, httpClientContext);
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 200:
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        content = EntityUtils.toString(entity, Consts.UTF_8);
                        EntityUtils.consume(entity);
                    }
                    break;
                case 400:
                    log.info("400,请求出现语法错误");
                    break;
                case 403:
                    log.info("403,资源不可用");
                    break;
                case 404:
                    log.info("404,无法找到指定资源地址");
                    break;
                case 503:
                    log.info("服务不可用");
                    break;
                case 504:
                    log.info("504,网关超时");
                    break;
                default:
                    log.info("未知错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (method != null) method.releaseConnection();
        }
        return content;
    }

    private static CloseableHttpClient createSSLHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSl();
        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(MAX_POOL_NUM);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        return client;
    }

    private static SSLContext createIgnoreVerifySSl() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

}
