package com.hrs.cloud.boot;

import com.hrs.cloud.enums.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.TimeZone;

/**
 * @ClassName AbstractApplication
 * @Description: TODO
 * @author bingcong huang
 * @Date 2022-08-19
 * @Version V1.0
 **/
public abstract  class AbstractApplication {
    private static final Logger log = LoggerFactory.getLogger(AbstractApplication.class);
    private static final String ENV_EX = "--spring.profiles.active=";
    private static final String ENV_PROPERTY_NAME = "user.env";
    @Value("${spring.application.name}")
    private String applicationName;

    public AbstractApplication() {
    }

    public static void start(String[] args) {
        String env = Env.LOCAL.name();
        if (args != null && args.length > 0) {
            String[] var2 = args;
            int var3 = args.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String arg = var2[var4];
                if (null != arg && arg.startsWith("--spring.profiles.active=")) {
                    env = arg.substring("--spring.profiles.active=".length());
                }
            }
        }

        System.setProperty("user.env", env.toLowerCase());
        System.setProperty("sun.net.client.defaultConnectTimeout", "2000");
        System.setProperty("sun.net.client.defaultReadTimeout", "1500");
        System.setProperty("user.timezone", "GMT+8");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }
}
