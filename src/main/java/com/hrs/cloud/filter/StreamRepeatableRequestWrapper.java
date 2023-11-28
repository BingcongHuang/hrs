package com.hrs.cloud.filter;

import com.hrs.cloud.helper.io.HttpHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 解决: request.getInputStream()只能读取一次的问题
 * 目标: 流可重复读
 */
@Slf4j
public class StreamRepeatableRequestWrapper extends HttpServletRequestWrapper {

 
    /**
     * 请求体
     */
    private String mBody;
 
    public StreamRepeatableRequestWrapper(HttpServletRequest request) {
        super(request);
        // 将body数据存储起来
        mBody = getBody(request);
    }
 
    /**
     * 获取请求体
     * @param request 请求
     * @return 请求体
     */
    private String getBody(HttpServletRequest request) {
            return HttpHelper.getParam(request);

    }
 
    /**
     * 获取请求体
     * @return 请求体
     */
    public String getBody() {
        return mBody;
    }
 
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
 
    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 创建字节数组输入流
        final ByteArrayInputStream bais = new ByteArrayInputStream(mBody.getBytes(Charset.defaultCharset()));
 
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
 
            @Override
            public boolean isReady() {
                return false;
            }
 
            @Override
            public void setReadListener(ReadListener readListener) {
 
            }
 
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
}
