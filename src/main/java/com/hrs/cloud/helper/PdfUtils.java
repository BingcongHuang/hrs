package com.hrs.cloud.helper;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName PdfUtils
 * @Description: TODO
 * @author bingcong huang
 * @Version V1.0
 **/
@Slf4j
public class PdfUtils {



    /**
     * 生成pdf文件
     *
     * @param sourcePdf pdf模板
     * @param pdfValue  pdf内容值
     * @return
     */
    public static File makePdf(String sourcePdf, String destPdf, Map<String, String> pdfValue) {
        PdfReader reader;
        FileOutputStream out = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper;
        try {
            //临时文件
            File tempFile = new File(destPdf + "/" + UUID.randomUUID().toString() + ".pdf");
            // 输出流
            out = new FileOutputStream(tempFile);
            // 读取pdf模板
            reader = new PdfReader(sourcePdf);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //BaseFont bf  = BaseFont.createFont("/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            //BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            //BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            form.addSubstitutionFont(bf);
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {

                //获取form域的名称
                String name = it.next().toString();
                //获取form域的类型
                int p = form.getFieldType(name);
                if (log.isInfoEnabled()) {
                    log.info("模板key:{} 类型:{}" ,name,p);
                }
                //checkbox类型为2
                if (p == 2) {
                    //checkbox value为"是"
                    if (pdfValue.get(name) != null) {
                        log.info("check:"+pdfValue.get(name));
                        form.setField(name, pdfValue.get(name), true);
                    }
                } else if (p == 4) {
                    //textfield类型为4
                    form.setFieldProperty(name, "textfont", bf, null);
                    form.setField(name, pdfValue.get(name));
                }

            }
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();


            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

//            byte[] result = File2byte(tempFile);
//            //删除掉生成的文件
////            if(tempFile.exists()){
////                tempFile.delete();
////            }
            return tempFile;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return null;
    }


    private static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
}
