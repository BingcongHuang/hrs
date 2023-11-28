package com.hrs.cloud.core.utils.MBean;

import org.springframework.util.CollectionUtils;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

public class EndPointsUtil {

   public static String  getEndPoints() throws MalformedObjectNameException,
            NullPointerException, UnknownHostException, AttributeNotFoundException,
            InstanceNotFoundException, MBeanException, ReflectionException {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
       ObjectName obj = null;
        if(!CollectionUtils.isEmpty(objs)){
            obj=objs.stream().findFirst().get();
        }
        String hostname = InetAddress.getLocalHost().getHostName();
        InetAddress addresses = InetAddress.getByName(hostname);
        String scheme = mbs.getAttribute(obj, "scheme").toString();
        String port = obj.getKeyProperty("port");
        String host = addresses.getHostAddress();
        String endPoints = scheme + "://" + host + ":" + port;

        return endPoints;
    }
}
