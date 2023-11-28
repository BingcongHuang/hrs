package com.hrs.cloud.helper.io;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class ResourceRender {
    public static InputStream resourceLoader(String fileFullPath) throws IOException {
        ClassPathResource cpr = new ClassPathResource(fileFullPath);
        return cpr.getInputStream();
    }
}
