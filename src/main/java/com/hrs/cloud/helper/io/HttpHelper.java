package com.hrs.cloud.helper.io;


import com.hrs.cloud.helper.json.JsonHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class Name : HttpHelper<br>
 * <p>
 * Description : HTTP 工具类<br>
 *
 * @author bingcong huang
 * @see
 */
@Configuration
@Slf4j
public class HttpHelper {

    private static int connectionTimeOut = 60000;

    private static int socketTimeOut = 60000;

    private static int maxConnectionPerHost = 800;

    private static int maxTotalConnections = 20000;

    private static CloseableHttpClient httpClient;

    //重试3次
    private static int retryTime = 3;

    static {
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            RequestConfig.Builder requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectionTimeOut)
                    .setConnectionRequestTimeout(connectionTimeOut)
                    .setSocketTimeout(socketTimeOut);
            httpClientBuilder.setDefaultRequestConfig(requestConfig.build());


            ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(HttpResponse response,
                                                 HttpContext context) {
                    return 65 * 1000;
                }
            };
            httpClientBuilder.setKeepAliveStrategy(myStrategy);
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(maxTotalConnections);
            cm.setDefaultMaxPerRoute(maxConnectionPerHost);

            // 重试机制
            HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
                @Override
                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount >= retryTime) {
                        return false;
                    }
                    // 服务端断掉客户端的连接异常
                    if (exception instanceof NoHttpResponseException) {
                        if(log.isErrorEnabled()){
                            log.error("http请求异常:"+exception.getMessage(),exception);
                        }
                        return true;
                    }
                    // time out 超时重试
                    if (exception instanceof InterruptedIOException) {
                        if(log.isErrorEnabled()){
                            log.error("http请求异常:"+exception.getMessage(),exception);
                        }
                        return true;
                    }
                    // Unknown host
                    if (exception instanceof UnknownHostException) {
                        if(log.isErrorEnabled()){
                            log.error("http请求异常:"+exception.getMessage(),exception);
                        }
                        return false;
                    }
                    // Connection refused
                    if (exception instanceof ConnectTimeoutException) {
                        if(log.isErrorEnabled()){
                            log.error("http请求异常:"+exception.getMessage(),exception);
                        }
                        return false;
                    }
                    // SSL handshake exception
                    if (exception instanceof SSLException) {
                        if(log.isErrorEnabled()){
                            log.error("http请求异常:"+exception.getMessage(),exception);
                        }
                        return false;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    if (!(request instanceof HttpEntityEnclosingRequest)) {
                        return true;
                    }
                    return false;
                }
            };
            httpClientBuilder.setRetryHandler(httpRequestRetryHandler);
            httpClient = httpClientBuilder.setConnectionManager(cm).build();
        } catch (Exception e) {
            log.error("URLConnectionHelper is error ",
                    getStackTrace(e));
        }
    }


    /**
     * get请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String get(String url, Map<String, String> paramMap) {
        HttpRequestBase httpGet = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        int statusCode = 0;
        String responseBodyAsString = "";
        try {
            if (MapUtils.isNotEmpty(paramMap)) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Entry<String, String> entry : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry
                            .getValue()));
                }
                url = new StringBuilder(url).append("?")
                        .append(URLEncodedUtils.format(nvps, Consts.UTF_8))
                        .toString();
            }
            if (log.isDebugEnabled()) {
                log.debug("\n\n\n%%%%%%%%%%%%%%%http send: " + url
                        + "\n%%%%%%%%%%%%%%%paramMap:" + paramMap);
            }
            httpGet = new HttpGet(url);
            httpGet.addHeader(HTTP.CONTENT_TYPE,
                    ContentType.APPLICATION_FORM_URLENCODED.withCharset(
                            Consts.UTF_8).toString());
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                responseBodyAsString = EntityUtils.toString(entity,
                        Consts.UTF_8);
            }else{
                if(log.isErrorEnabled()){
                    log.error("返回状态码:"+statusCode);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("\n%%%%%%%%%%%%%%%http state: " + statusCode
                        + "\n%%%%%%%%%%%%%%%response:" + responseBodyAsString);
            }
        } catch (Exception e) {
            log.error("soa exception   %%%%% http url:" + url);
            log.error(getStackTrace(e));
        } finally {
            close(entity, httpGet, response);
        }
        return responseBodyAsString;
    }

    /**
     * post请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String postFormUrlencoded(String url, String param) {
        HttpEntityEnclosingRequestBase httpPost = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        int statusCode = 0;
        String responseBodyAsString = "";
        try {
            httpPost = new HttpPost(url);
            httpPost.addHeader(
                    HTTP.CONTENT_TYPE,
                    ContentType.APPLICATION_FORM_URLENCODED.withCharset(
                            Consts.UTF_8).toString());
            // 将表单的值放入postMethod中


            StringEntity se =doStringEntity(param.toString());
            httpPost.setEntity(se);
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                responseBodyAsString = EntityUtils.toString(entity,
                        Consts.UTF_8);
            }else{
                if(log.isErrorEnabled()){
                    log.error("返回状态码:"+statusCode);
                }
            }
        } catch (Exception e) {
            log.error("soa exception   %%%%% http url:" + url);
            log.error(getStackTrace(e));
        } finally {
            close(entity, httpPost, response);
        }
        return responseBodyAsString;
    }

//
//
//    public static GlobalResult upload(String url, File file, String fileName, String appId, String header) {
//        HttpEntityEnclosingRequestBase httpPost = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        int statusCode = 0;
//        String responseBodyAsString = "";
//        try {
//            httpPost = new HttpPost(url);
//            FileBody fb = new FileBody(file);
//            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//            HttpEntity build = multipartEntityBuilder.addPart("curfile", fb)
//                    .addPart("cfileName", new StringBody(fileName, ContentType.TEXT_PLAIN))
//                    .addPart("appId", new StringBody(appId, ContentType.TEXT_PLAIN)).build();
//
//            httpPost.setEntity(build);
//            httpPost.addHeader("Authorization", header);
//            response = httpClient.execute(httpPost);
//            entity = response.getEntity();
//            statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                responseBodyAsString = EntityUtils.toString(entity,
//                        Consts.UTF_8);
//                return JsonHelper.fromJson(responseBodyAsString, GlobalResult.class);
//            }
//        } catch (Exception e) {
//            log.error("soa exception   %%%%% http url:" + url);
//            log.error(getStackTrace(e));
//        } finally {
//            close(entity, httpPost, response);
//        }
//        return GlobalResult.fail(responseBodyAsString);
//    }
//
//    public static GlobalResult uploadFile(String url, File file, String mediaType, String json, String appId, String header) {
//        HttpEntityEnclosingRequestBase httpPost = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        int statusCode = 0;
//        String responseBodyAsString = "";
//        try {
//            httpPost = new HttpPost(url);
//            FileBody fb = new FileBody(file);
//            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//            multipartEntityBuilder.addPart("curfile", fb);
//            StringBody name = new StringBody(json, ContentType.TEXT_PLAIN);
//            multipartEntityBuilder.addPart("json", name);
//            StringBody mediaTypep = new StringBody(mediaType, ContentType.TEXT_PLAIN);
//            multipartEntityBuilder.addPart("mediaType", mediaTypep);
//            StringBody appIds = new StringBody(appId, ContentType.TEXT_PLAIN);
//            multipartEntityBuilder.addPart("appId", appIds);
//            httpPost.setEntity(multipartEntityBuilder.build());
//            httpPost.addHeader("Authorization", header);
//            response = httpClient.execute(httpPost);
//            entity = response.getEntity();
//            statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                responseBodyAsString = EntityUtils.toString(entity,
//                        Consts.UTF_8);
//                return JsonHelper.fromJson(responseBodyAsString, GlobalResult.class);
//            }
//        } catch (Exception e) {
//            log.error("soa exception   %%%%% http url:" + url);
//            log.error(getStackTrace(e));
//        } finally {
//            close(entity, httpPost, response);
//        }
//        return GlobalResult.fail(responseBodyAsString);
//    }
//
//    public static GlobalResult uploadFile(String url, File file, String mediaType, String cfileName, String corpId, String header, String suffix) {
//        HttpEntityEnclosingRequestBase httpPost = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        int statusCode = 0;
//        String responseBodyAsString = "";
//        try {
//            httpPost = new HttpPost(url);
//            FileBody fb = new FileBody(file);
//            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//            multipartEntityBuilder.addPart("curfile", fb);
//            StringBody name = new StringBody(cfileName, ContentType.APPLICATION_JSON);
//            multipartEntityBuilder.addPart("cfileName", name);
//            StringBody mediaTypep = new StringBody(mediaType, ContentType.TEXT_PLAIN);
//            multipartEntityBuilder.addPart("type", mediaTypep);
//            httpPost.setEntity(multipartEntityBuilder.build());
//            httpPost.addHeader("Authorization", header);
//            httpPost.addHeader("corpId",corpId);
//            response = httpClient.execute(httpPost);
//            entity = response.getEntity();
//            statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                responseBodyAsString = EntityUtils.toString(entity,
//                        Consts.UTF_8);
//                return JsonHelper.fromJson(responseBodyAsString, GlobalResult.class);
//            }
//        } catch (Exception e) {
//            log.error("soa exception   %%%%% http url:" + url);
//            log.error(getStackTrace(e));
//        } finally {
//            close(entity, httpPost, response);
//        }
//        return GlobalResult.fail(responseBodyAsString);
//    }
//
//    public static GlobalResult postJson(String url, String json) {
//        HttpEntityEnclosingRequestBase httpPost = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        int statusCode = 0;
//        String responseBodyAsString = "";
//        try {
//            httpPost = new HttpPost(url);
//            httpPost.addHeader(
//                    HTTP.CONTENT_TYPE,
//                    ContentType.APPLICATION_FORM_URLENCODED.withCharset(
//                            Consts.UTF_8).toString());
//            // 将表单的值放入postMethod中
//
//            httpPost.setEntity(doStringEntity(json));
//            response = httpClient.execute(httpPost);
//            entity = response.getEntity();
//            statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                responseBodyAsString = EntityUtils.toString(entity,
//                        Consts.UTF_8);
//                return JsonHelper.fromJson(responseBodyAsString, GlobalResult.class);
//            }
//        } catch (Exception e) {
//            log.error("soa exception   %%%%% http url:" + url);
//            log.error(getStackTrace(e));
//        } finally {
//            close(entity, httpPost, response);
//        }
//        return GlobalResult.fail(responseBodyAsString);
//    }
//
//
//
    public static String doStringEntityByCa(String json) throws Exception {
        String entity="";
        if (StringUtils.isNotBlank(json)) {
        StringBuilder ss = new StringBuilder();
            ss.append( "?");
            Map<String, Object> map = JsonHelper.fromJson(json, Map.class);
            for (Entry<String, Object> entry : map.entrySet()) {
                String entryKey = entry.getKey();
                String entryVal = String.valueOf(entry.getValue());
                ss.append(entryKey + "=" + entryVal + "&");
            }
            entity = ss.substring(0, ss.length() - 1);
        }
        return entity;
    }

    public static String paramToString(String json) throws Exception {
        String entity="";
        if (StringUtils.isNotBlank(json)) {
            StringBuilder ss = new StringBuilder();
            Map<String, Object> map = JsonHelper.fromJson(json, Map.class);
            for (Entry<String, Object> entry : map.entrySet()) {
                String entryKey = entry.getKey();
                String entryVal = String.valueOf(entry.getValue());
                ss.append(entryKey + "=" + entryVal + "&");
            }
            entity = ss.substring(0, ss.length() - 1);
        }
        return entity;
    }


    public static StringEntity doStringEntity(String json) throws Exception {
        StringEntity se = new StringEntity("");
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> map = JsonHelper.fromJson(json, Map.class);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Map<String, String> paramMap = Maps.newHashMap();
            for (Entry<String, Object> entry : map.entrySet()) {
                String entryKey = entry.getKey();
                String entryVal = String.valueOf(entry.getValue());
                paramMap.put(entryKey, entryVal);
                nvps.add(new BasicNameValuePair(entryKey, entryVal));
            }
            map.clear();
            paramMap.clear();
            se = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
        }
        return se;
    }

//    public static GlobalResult postAppJson(String url, String json, String header) {
//        HttpEntityEnclosingRequestBase httpPost = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        int statusCode = 0;
//        String responseBodyAsString = "";
//        try {
//            httpPost = new HttpPost(url);
//            httpPost.addHeader(
//                    HTTP.CONTENT_TYPE,
//                    ContentType.APPLICATION_FORM_URLENCODED.withCharset(
//                            Consts.UTF_8).toString());
//            httpPost.addHeader("Authorization", header);
//            // 将表单的值放入postMethod中
//
//            httpPost.setEntity(doStringEntity(json));
//            response = httpClient.execute(httpPost);
//            entity = response.getEntity();
//            statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                responseBodyAsString = EntityUtils.toString(entity,
//                        Consts.UTF_8);
//                return JsonHelper.fromJson(responseBodyAsString, GlobalResult.class);
//            }
//        } catch (Exception e) {
//            log.error("soa exception   %%%%% http url:" + url);
//            log.error(getStackTrace(e));
//        } finally {
//            close(entity, httpPost, response);
//        }
//        return GlobalResult.fail(responseBodyAsString);
//    }
//
//
    /**
     *
     *
     * @param url
     * @param json
     * @return
     */
    public static String postApplicationJson(String url, String json) {
        HttpEntityEnclosingRequestBase httpPost = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        int statusCode = 0;
        String responseBodyAsString = "";
        try {
            httpPost = new HttpPost(url);
            //解决中文乱码问题
            httpPost.addHeader(
                    HTTP.CONTENT_TYPE,
                    ContentType.APPLICATION_JSON.getMimeType());
            StringEntity entityp = new StringEntity(json, "utf-8");
            entityp.setContentEncoding("UTF-8");
            entityp.setContentType("application/json");
            httpPost.setEntity(entityp);
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                responseBodyAsString = EntityUtils.toString(entity,
                        Consts.UTF_8);
                return responseBodyAsString;
            }
        } catch (Exception e) {
            log.error("soa exception   %%%%% http url:" + url);
            log.error(getStackTrace(e));
        } finally {
            close(entity, httpPost, response);
        }
        return responseBodyAsString;
    }



    private static void close(HttpEntity entity, HttpRequestBase request,
                              CloseableHttpResponse response) {
        try {
            if (request != null){
                request.releaseConnection();
            }
            if (entity != null){
                EntityUtils.consume(entity);
            }
            if (response != null){
                response.close();
            }
        } catch (IllegalStateException e) {
            log.error(getStackTrace(e));
        } catch (IOException e) {
            log.error(getStackTrace(e));
        }
    }

    public static String getStackTrace(Exception e) {
         if(log.isErrorEnabled()){
             log.error(e.getMessage(), e);
         }
        return e.getMessage();
    }
    public static String getParam(HttpServletRequest request) {
        InputStream inputStream = null;
        InputStreamReader sr = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            sr = new InputStreamReader(inputStream);
            reader = new BufferedReader(sr);
            String str = "";
            StringBuilder result = new StringBuilder();

            while ((str = reader.readLine()) != null) {
                result.append(str);
            }
            return result.toString().replaceAll("\t", "");
        } catch (IOException e) {
            log.error("getParam is error",e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error("getParam is error",e);
                }
            }
            if (sr != null) {
                try {
                    sr.close();
                } catch (Exception e) {
                    log.error("getParam is error",e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("getParam is error",e);
                }
            }
        }
        return null;
    }

}
