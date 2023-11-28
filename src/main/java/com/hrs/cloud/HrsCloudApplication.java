package com.hrs.cloud;


import com.hrs.cloud.boot.AbstractApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Slf4j
@ServletComponentScan
@EnableAsync
@SpringBootApplication
public class HrsCloudApplication extends AbstractApplication {

    public static void main(String[] args) throws UnknownHostException {
        start(args);
        ConfigurableApplicationContext run = SpringApplication.run(HrsCloudApplication.class, args);
        Environment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("admin.web.port");
        String path = env.getProperty("admin.web.path");
        log.info("\n-----------------------start end-----------------------------------\n\t" +
                "应用名称: \thrs-cloud\n\t" +
                "部署环境: \t"+env.getProperty("user.env")+"\n\t" +
                "调用地址: \thttp://" + ip + ":" + port + path + "/user/login\n\t" +
                "----------------------------------------------------------");
    }





}
