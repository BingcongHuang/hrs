/**
 * @Project：wh-aircraft-apply
 * @Title：ToolUtil.java
 * @Description：
 * @Package com.ws.modular.third.eport.utils
 * @author：yangyucheng
 * @date：2018年5月29日
 * @Copyright: 上海顺益信息科技有限公司 All rights reserved.
 * @version V1.0
 */
package com.hrs.cloud.core.utils;

import com.hrs.cloud.boot.config.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileToolUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileToolUtil.class);

    private static String SEND_ID = PropertiesUtils.getPropertiesValue("message.senderid");

    public static String getFileName(String type) {
        //String date = DateUtil.getAllTime();
        String name = "CN_" + type + "_1P0_" + SEND_ID + "_" + DateUtil.format(new Date(), "yyyyMMddhhmmssSSS") + ".zip";
        logger.info(String.format("fileName:%s", name));
        return name;
    }

    /**
     * 生成传输人messageId
     *
     * @param type
     * @param sendId
     * @return
     */
    public static String getFileNameBySendId(String type, String sendId) {
        //线程等待100毫秒
        try {
            // 休眠制定时间（单位：毫秒）
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            logger.error("线程等待100毫秒出错");
        }
        String name = "CN_" + type + "_1P0_" + sendId + "_" + DateUtil.format(new Date(), "yyyyMMddhhmmssSSS") + ".zip";
        logger.info(String.format("fileName:%s", name));
        return name;
    }

    public static void ftp(String dir, String xml, String messageType) {
        ftp(dir, xml, messageType, false);
    }

    public static void ftp(String dir, String xml, String messageType, boolean delayed) {
        //生产文件并且FTP
        String dm = null;
        try {
            dm = FileUtil.saveFileBackFilePath(xml, ".xml", dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FtpUtils ftp = new FtpUtils();
        String root = PropertiesUtils.getPropertiesValue("ftp.rootPath");
        ftp.uploadFile(root, FileToolUtil.getFileName(messageType), dm);
        if (delayed) { //设置延时，如果同一时间发送报文，导致文件相同出现问题
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
