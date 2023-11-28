package com.hrs.cloud.core.utils;

import com.hrs.cloud.boot.config.PropertiesUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;

public class FtpUtils {
	
		private static Logger logger = LoggerFactory.getLogger(FtpUtils.class);
		
		
        //ftp服务器地址
        public String hostname = PropertiesUtils.getPropertiesValue("ftp.hostname");
        //ftp服务器端口号默认为21
        public Integer port = Integer.parseInt(PropertiesUtils.getPropertiesValue("ftp.port"));
        //ftp登录账号
        public String username = PropertiesUtils.getPropertiesValue("ftp.username");
        //ftp登录密码
        public String password = PropertiesUtils.getPropertiesValue("ftp.password");
		//ftp开启状态
        public boolean open = Boolean.parseBoolean(PropertiesUtils.getPropertiesValue("ftp.open"));

        public FTPClient ftpClient = null;
        
        /**
         * 初始化ftp服务器
         */
        public  void initFtpClient() {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            try {
            	logger.info("connecting...ftp服务器:"+this.hostname+":"+this.port); 
                ftpClient.connect(hostname, port); //连接ftp服务器
                ftpClient.login(username, password); //登录ftp服务器
                int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
                if(!FTPReply.isPositiveCompletion(replyCode)){
                	logger.info("connect failed...ftp服务器:"+this.hostname+":"+this.port); 
                }
                logger.info("connect successfu...ftp服务器:"+this.hostname+":"+this.port); 
            }catch (MalformedURLException e) { 
               e.printStackTrace(); 
            }catch (IOException e) { 
               e.printStackTrace(); 
            } 
        }

        /**
        * 上传文件
        * @param pathname ftp服务保存地址
        * @param fileName 上传到ftp的文件名
        *  @param originfilename 待上传文件的名称（绝对地址） * 
        * @return
        */
        public boolean uploadFile( String pathname, String fileName,String originfilename){
        	if(!open){
        		return false;
        	}
            boolean flag = false;
            InputStream inputStream = null;
            try{
                logger.info("开始上传文件");
                inputStream = new FileInputStream(new File(originfilename));
                initFtpClient();
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
                CreateDirecroty(pathname);
                ftpClient.makeDirectory(pathname);
                ftpClient.changeWorkingDirectory(pathname);
                //ftpClient.enterLocalPassiveMode();
                ftpClient.storeFile(fileName, inputStream);
                inputStream.close();
                ftpClient.logout();
                flag = true;
                logger.info("上传文件成功");
            }catch (Exception e) {
                logger.info("上传文件失败");
                e.printStackTrace();
            }finally{
                if(ftpClient.isConnected()){ 
                    try{
                        ftpClient.disconnect();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                } 
                if(null != inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                } 
            }
            return true;
        }
        /**
         * 上传文件
         * @param pathname ftp服务保存地址
         * @param fileName 上传到ftp的文件名
         * @param inputStream 输入文件流 
         * @return
         */
        public boolean uploadFile( String pathname, String fileName,InputStream inputStream){
            boolean flag = false;
            try{
                logger.info("开始上传文件");
                initFtpClient();
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
                CreateDirecroty(pathname);
                ftpClient.makeDirectory(pathname);
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.storeFile(fileName, inputStream);
                inputStream.close();
                ftpClient.logout();
                flag = true;
                logger.info("上传文件成功");
            }catch (Exception e) {
                logger.info("上传文件失败");
                e.printStackTrace();
            }finally{
                if(ftpClient.isConnected()){ 
                    try{
                        ftpClient.disconnect();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                } 
                if(null != inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                } 
            }
            return true;
        }
        //改变目录路径
         public boolean changeWorkingDirectory(String directory) {
                boolean flag = true;
                try {
                    flag = ftpClient.changeWorkingDirectory(directory);
                    if (flag) {
                      logger.info("进入文件夹" + directory + " 成功！");

                    } else {
                        logger.info("进入文件夹" + directory + " 失败！开始创建文件夹");
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return flag;
            }

        //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
        public boolean CreateDirecroty(String remote) throws IOException {
            boolean success = true;
            String directory = remote + "/";
            // 如果远程目录不存在，则递归创建远程服务器目录
            if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
                int start = 0;
                int end = 0;
                if (directory.startsWith("/")) {
                    start = 1;
                } else {
                    start = 0;
                }
                end = directory.indexOf("/", start);
                String path = "";
                String paths = "";
                while (true) {
                    String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                    path = path + "/" + subDirectory;
                    if (!existFile(path)) {
                        if (makeDirectory(subDirectory)) {
                            changeWorkingDirectory(subDirectory);
                        } else {
                            logger.info("创建目录[" + subDirectory + "]失败");
                            changeWorkingDirectory(subDirectory);
                        }
                    } else {
                        changeWorkingDirectory(subDirectory);
                    }

                    paths = paths + "/" + subDirectory;
                    start = end + 1;
                    end = directory.indexOf("/", start);
                    // 检查所有目录是否创建完毕
                    if (end <= start) {
                        break;
                    }
                }
            }
            return success;
        }

      //判断ftp服务器文件是否存在    
        public boolean existFile(String path) throws IOException {
                boolean flag = false;
                FTPFile[] ftpFileArr = ftpClient.listFiles(path);
                if (ftpFileArr.length > 0) {
                    flag = true;
                }
                return flag;
            }
        //创建目录
        public boolean makeDirectory(String dir) {
            boolean flag = true;
            try {
                flag = ftpClient.makeDirectory(dir);
                if (flag) {
                    logger.info("创建文件夹" + dir + " 成功！");

                } else {
                    logger.info("创建文件夹" + dir + " 失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }
        
        /** * 下载文件 * 
        * @param pathname FTP服务器文件目录 * 
        * @param filename 文件名称 * 
        * @param localpath 下载后的文件路径 * 
        * @return */
        public  boolean downloadFile(String pathname, String filename, String localpath){ 
            boolean flag = false; 
            OutputStream os=null;
            try { 
                logger.info("开始下载文件");
                initFtpClient();
                //切换FTP目录 
                ftpClient.changeWorkingDirectory(pathname); 
                FTPFile[] ftpFiles = ftpClient.listFiles(); 
                for(FTPFile file : ftpFiles){ 
                    if(filename.equalsIgnoreCase(file.getName())){ 
                        File localFile = new File(localpath + "/" + file.getName()); 
                        os = new FileOutputStream(localFile); 
                        ftpClient.retrieveFile(file.getName(), os); 
                        os.close(); 
                    } 
                } 
                ftpClient.logout(); 
                flag = true; 
                logger.info("下载文件成功");
            } catch (Exception e) { 
                logger.info("下载文件失败");
                e.printStackTrace(); 
            } finally{ 
                if(ftpClient.isConnected()){ 
                    try{
                        ftpClient.disconnect();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                } 
                if(null != os){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                } 
            } 
            return flag; 
        }
        /** * 下载目录下的文件 * 
         * @param pathname FTP服务器文件目录 * 
         * @param filename 文件名称 * 
         * @param localpath 下载后的文件路径 * 
         * @return */
         public  boolean downloadFileByDirectory(String pathname,String localpath){ 
             boolean flag = false; 
             OutputStream os=null;
             try { 
                 logger.info("开始下载文件");
                 initFtpClient();
                 //切换FTP目录 
                 ftpClient.changeWorkingDirectory(pathname); 
                 ftpClient.enterLocalPassiveMode();
                 logger.info("扫描目录文件....");
                 FTPFile[] ftpFiles = ftpClient.listFiles();
                 Integer fileCount = null;
                 if (ftpFiles != null ) {
                	 fileCount = ftpFiles.length;
                 }
                 logger.info("扫描目录文件结束...." + fileCount);
                 for(FTPFile file : ftpFiles){ 
                         File localFile = new File(localpath + "/" + file.getName()); 
                         os = new FileOutputStream(localFile); 
                         ftpClient.retrieveFile(file.getName(), os); 
                         os.close(); 
                 } 
                 ftpClient.logout(); 
                 flag = true; 
                 logger.info("下载文件成功");
             } catch (Exception e) { 
                 logger.info("下载文件失败");
                 e.printStackTrace(); 
             } finally{ 
                 if(ftpClient.isConnected()){ 
                     try{
                         ftpClient.disconnect();
                     }catch(IOException e){
                         e.printStackTrace();
                     }
                 } 
                 if(null != os){
                     try {
                         os.close();
                     } catch (IOException e) {
                         e.printStackTrace();
                     } 
                 } 
             } 
             return flag; 
         }  
        
        /** * 删除文件 * 
        * @param pathname FTP服务器保存目录 * 
        * @param filename 要删除的文件名称 * 
        * @return */ 
        public boolean deleteFile(String pathname, String filename){ 
            boolean flag = false; 
            try { 
                logger.info("开始删除文件");
                initFtpClient();
                //切换FTP目录 
                ftpClient.changeWorkingDirectory(pathname); 
                ftpClient.dele(filename); 
                ftpClient.logout();
                flag = true; 
                logger.info("删除文件成功");
            } catch (Exception e) { 
                logger.info("删除文件失败");
                e.printStackTrace(); 
            } finally {
                if(ftpClient.isConnected()){ 
                    try{
                        ftpClient.disconnect();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                } 
            }
            return flag; 
        }
        
        public static void main(String[] args) {
            FtpUtils ftp =new FtpUtils(); 
            ftp.uploadFile("/cds9data/send", "server_new.log", "D:/test/server_new.log");
            logger.info("ok");
        }
}