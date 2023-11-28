package com.hrs.cloud.boot.config;

import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class FreeMarkerConfig {

    @Value("${mainDomain}")
    private String mainDomain;

    @Resource
    protected freemarker.template.Configuration configuration;

    @PostConstruct
    public void setSharedVariable() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("mainDomain", mainDomain);
        try {
            configuration.setSharedVaribles(variables);
        } catch (TemplateModelException e) {
            log.error("setSharedVariable exception",e);
        }
    }
}
