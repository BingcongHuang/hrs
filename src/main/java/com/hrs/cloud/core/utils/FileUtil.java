package com.hrs.cloud.core.utils;

import com.hrs.cloud.boot.config.PropertiesUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtil extends FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);


    /**
     * 删除目录
     *
     * @author bingcong huang
     * @Date 2017/10/30 下午4:15
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //得到文件名后缀
    public static String getExtName(String fileName) {
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);

    }

    public static String saveFile(MultipartFile multipartFile, String dir) throws IOException {
        if (multipartFile.isEmpty()) {
            return "";
        }
        String extName = multipartFile.getOriginalFilename();
        String suffixName = "";
        if (extName.lastIndexOf(".") != -1) {
            suffixName = extName.substring(extName.lastIndexOf("."));
        }

        String date = DateUtil.format(new Date(), "yyyy/MM/dd/");
        String realName = System.nanoTime() + suffixName;
        String filePath = PropertiesUtils.getPropertiesValue("file.root") + "/" + dir + "/" + date + realName;
        String url = PropertiesUtils.getPropertiesValue("file.httppath") + "/" + dir + "/" + date + realName;

        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {// 文件路径不存在时，自动创建目录
            fileParent.mkdirs();
        }
        multipartFile.transferTo(file);
        return url;
    }


    /**
     * getTempFile:(根据目录获取临时文件路径).
     *
     * @param dir
     * @return
     * @author bingcong huang
     * @since JDK 1.8
     */
    public static Map<String, String> getTempFile(String dir, String suffixName) {
        String realName = System.nanoTime() + suffixName;
        String date = DateUtil.format(new Date(), "yyyy/MM/dd/");
        String filePath = PropertiesUtils.getPropertiesValue("file.root") + "/" + dir + "/" + date + realName;
        String url = PropertiesUtils.getPropertiesValue("file.httppath") + "/" + dir + "/" + date + realName;
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {// 文件路径不存在时，自动创建目录
            fileParent.mkdirs();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("file", filePath);
        map.put("url", url);
        return map;
    }



    public static String saveFileBackFilePath(MultipartFile multipartFile, String dir) throws IOException {
        if (multipartFile.isEmpty()) {
            return "";
        }
        String extName = multipartFile.getOriginalFilename();
        String suffixName = "";
        if (extName.lastIndexOf(".") != -1) {
            suffixName = extName.substring(extName.lastIndexOf("."));
        }
        String realName = System.nanoTime() + suffixName;
        String date = DateUtil.format(new Date(), "yyyy/MM/dd/");
        String filePath = PropertiesUtils.getPropertiesValue("file.root") + "/" + dir + "/" + date + realName;
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {// 文件路径不存在时，自动创建目录
            fileParent.mkdirs();
        }
        //multipartFile.transferTo(file);
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),file);
        return filePath;
    }


    public static String saveFileBackFilePath(String content, String suffixName, String dir) throws IOException {
        String realName = System.nanoTime() + suffixName;
        String date = DateUtil.format(new Date(), "yyyy/MM/dd/");
        String filePath = PropertiesUtils.getPropertiesValue("file.root") + "/" + dir + "/" + date + realName;
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {// 文件路径不存在时，自动创建目录
            fileParent.mkdirs();
        }
        WriteStringToFile(filePath, content);
        return filePath;
    }

    public static String saveFileBackFilePath(String realName, String content, String suffixName, String dir) throws IOException {
        String date = DateUtil.format(new Date(), "yyyy/MM/dd/");
        String filePath = PropertiesUtils.getPropertiesValue("file.root") + "/" + dir + "/" + date + realName;
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {// 文件路径不存在时，自动创建目录
            fileParent.mkdirs();
        }
        WriteStringToFile(filePath, content);
        return filePath;
    }

    public static void WriteStringToFile(String filePath, String content) {
        try {
            //FileWriter fw = new FileWriter(filePath, false);
            //BufferedWriter bw = new BufferedWriter(fw);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath)),"utf-8"));
            bw.write(content);// 往已有的文件上添加字符串
            bw.close();
            //fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String saveFileBackFilePathForZip(String content, String suffixName, String fileName, String dir) throws IOException {
        String realName = System.nanoTime() + suffixName;
        String date = DateUtil.format(new Date(), "yyyy/MM/dd/");
        String filePath = PropertiesUtils.getPropertiesValue("file.root") + "/" + dir + "/" + date + realName;
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {// 文件路径不存在时，自动创建目录
            fileParent.mkdirs();
        }
        WriteStringToZipFile(filePath, fileName, content);
        return filePath;
    }

    public static void WriteStringToZipFile(String filePath, String fileName, String content) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] buffer = content.getBytes();
            FileOutputStream fOutputStream = new FileOutputStream(file);
            ZipOutputStream zoutput = new ZipOutputStream(fOutputStream);
            String fileTempInfo = fileName.substring(0, fileName.lastIndexOf("."));
            ZipEntry zEntry = new ZipEntry(fileTempInfo + ".xml");
            zoutput.putNextEntry(zEntry);
            zoutput.write(buffer);
            zoutput.closeEntry();
            zoutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage getRemoteUrl(String url) {
        BufferedImage image = null;
        try {
            URL u = new URL(url);
            image = ImageIO.read(u);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String saveImage(byte[] bytes, String suffix, String dir) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bi1;
        try {
            bi1 = ImageIO.read(bais);
            String realName = System.nanoTime() + "." + suffix;
            String filePath = PropertiesUtils.getPropertiesValue("file.root") + "/" + dir + "/" + realName;
            String url = PropertiesUtils.getPropertiesValue("file.httppath") + "/" + dir + "/" + realName;
            File file = new File(filePath);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {// 文件路径不存在时，自动创建目录
                fileParent.mkdirs();
            }
            // 不管输出什么格式图片，此处不需改动
            ImageIO.write(bi1, "jpg", file);
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 批量上传文件 返回值为文件的新名字 UUID.randomUUID()+originalFilename
     *
     * @param multipartFiles
     * @param request
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static List<String> uploadFileList(MultipartFile multipartFiles[], HttpServletRequest request) throws IllegalStateException, IOException {
        List<String> newFileNames = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                //文件的原始名称
                String originalFilename = multipartFile.getOriginalFilename();
                String newFileName = null;
                if (multipartFile != null && originalFilename != null && originalFilename.length() > 0) {

                    newFileName = UUID.randomUUID() + originalFilename;
                    //存储图片的物理路径
                    String pic_path = request.getSession().getServletContext().getRealPath("/upload/enterprise");
                    //新图片路径
                    File targetFile = new File(pic_path, newFileName);
                    //内存数据读入磁盘
                    multipartFile.transferTo(targetFile);
                    newFileNames.add(newFileName);
                } else {
                    newFileNames.add("");
                }
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return newFileNames;
    }

    /**
     * 获取路径下的所有文件/文件夹
     *
     * @param directoryPath  需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     * @return
     */
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * @return int
     * @author bingcong huang
     * @description 获取网络文件大小
     * @date 2018/10/22 10:04
     * @params [url1]
     * @since JDK 1.7
     */
    public static long getFileLength(String url1) {
        long length = 0;
        URL url;
        try {
            url = new URL(url1);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            if (urlcon.getResponseCode() >= 400) {
                log.error("服务器响应错误");
            }
            //根据响应获取文件大小
            length = urlcon.getContentLengthLong();
            if (length <= 0) {
                log.error("无法获知文件大小");
            }
            urlcon.disconnect();
        } catch (MalformedURLException e) {
            log.error("URL协议错误", e);
        } catch (IOException e) {
            log.error("文件读取异常", e);
        }
        return length;
    }
    



    public static void main(String[] args) {
        System.out.println(getExtName("xxxx.jpg"));
    }
}