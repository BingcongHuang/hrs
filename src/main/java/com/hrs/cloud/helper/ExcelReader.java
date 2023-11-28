package com.hrs.cloud.helper;

import com.hrs.cloud.entity.ToolInst;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 读取Excel内容
 */
public class ExcelReader {
    private static Logger logger = Logger.getLogger(ExcelReader.class.getName()); // 日志打印类

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";


    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType 文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    /**
     * 读取Excel文件内容
     * @param fileName 要读取的Excel文件所在路径
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<ToolInst> readExcel(String fileName) {

        Workbook workbook = null;
        InputStream inputStream = null;

        try {
            // 获取Excel后缀名
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            // 获取Excel文件
            File excelFile = new File(fileName);
            if (!excelFile.exists()) {
                logger.warning("指定的Excel文件不存在！");
                return null;
            }

            // 获取Excel工作簿
            inputStream = new FileInputStream(excelFile);
            workbook = getWorkbook(inputStream, fileType);

            // 读取excel中的数据
            List<ToolInst> resultDataList = parseExcel(workbook);

            return resultDataList;
        } catch (Exception e) {
            logger.warning("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.warning("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 读取Excel文件内容
     * @param file 上传的Excel文件
     * @return 读取结果列表，读取失败时返回null
     */
    public static List<ToolInst> readExcel(MultipartFile file) {

        Workbook workbook = null;

        try {
            // 获取Excel后缀名
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty() || fileName.lastIndexOf(".") < 0) {
                logger.warning("解析Excel失败，因为获取到的Excel文件名非法！");
                return null;
            }
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

            // 获取Excel工作簿
            workbook = getWorkbook(file.getInputStream(), fileType);

            // 读取excel中的数据
            List<ToolInst> resultDataList = parseExcel(workbook);

            return resultDataList;
        } catch (Exception e) {
            logger.warning("解析Excel失败，文件名：" + file.getOriginalFilename() + " 错误信息：" + e.getMessage());
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
            } catch (Exception e) {
                logger.warning("关闭数据流出错！错误信息：" + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 解析Excel数据
     * @param workbook Excel工作簿对象
     * @return 解析结果
     */
    private static List<ToolInst> parseExcel(Workbook workbook) {
        List<ToolInst> resultDataList = new ArrayList<>();
        // 解析sheet
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);

            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }

            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
                logger.warning("解析Excel失败，在第一行没有读取到任何数据！");
            }

            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);

                if (null == row) {
                    continue;
                }

                ToolInst resultData = convertRowToData(row);
                if (null == resultData) {
                    logger.warning("第 " + row.getRowNum() + "行数据不合法，已忽略！");
                    continue;
                }
                resultDataList.add(resultData);
            }
        }

        return resultDataList;
    }

    /**
     * 将单元格内容转换为字符串
     * @param cell
     * @return
     */
    private static String convertCellValueToString(Cell cell) {
        if(cell==null){
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();

                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:   //布尔
                Boolean booleanValue = cell.getBooleanCellValue();
                returnValue = booleanValue.toString();
                break;
            case BLANK:     // 空值
                break;
            case FORMULA:   // 公式
                returnValue = cell.getCellFormula();
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }

    /**
     * 提取每一行中需要的数据，构造成为一个结果数据对象
     *
     * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
     *
     * @param row 行数据
     * @return 解析后的行数据对象，行数据错误时返回null
     */
    private static ToolInst convertRowToData(Row row) {
        ToolInst resultData = new ToolInst();

        Cell cell;
        int cellNum = 0;
        // 获取件号
        cell = row.getCell(cellNum++);
        resultData.setToolCode(convertCellValueToString(cell));
        // 获取序号
        cell = row.getCell(cellNum++);
        resultData.setSeqNo(convertCellValueToString(cell));
        // 获取名称
        cell = row.getCell(cellNum++);
        resultData.setToolName(convertCellValueToString(cell));
        // 获取英文名称
        cell = row.getCell(cellNum++);
        resultData.setToolEname(convertCellValueToString(cell));
        // 获取适用机型
        cell = row.getCell(cellNum++);
        resultData.setAircraftType(convertCellValueToString(cell));
        // 获取厂家
        cell = row.getCell(cellNum++);
        resultData.setMerchant(convertCellValueToString(cell));
        // 获取出厂日期
        cell = row.getCell(cellNum++);
        resultData.setProductionDate(convertCellValueToString(cell));
        // 获取数量
        cell = row.getCell(cellNum++);
        String stock = convertCellValueToString(cell);
        if (null == stock || "".equals(stock)) {
            // 为空
            resultData.setStock(0);
        } else {
            resultData.setStock(Integer.parseInt(stock));
        }
        // 获取原价
        cell = row.getCell(cellNum++);
        String oraginalPrice = convertCellValueToString(cell);
        if (null == oraginalPrice || "".equals(oraginalPrice)) {
            // 为空
            resultData.setOraginalPrice(BigDecimal.ZERO);
        } else {
            resultData.setOraginalPrice(new BigDecimal(oraginalPrice));
        }
        // 获取日租金
        cell = row.getCell(cellNum++);
        String dailyRent = convertCellValueToString(cell);
        if (null == dailyRent || "".equals(dailyRent)) {
            // 为空
            resultData.setDailyRent(BigDecimal.ZERO);
        } else {
            resultData.setDailyRent(new BigDecimal(dailyRent));
        }
        // 获取所属公司
        cell = row.getCell(cellNum++);
        resultData.setCompany(convertCellValueToString(cell));
        // 获取联系人
        cell = row.getCell(cellNum++);
        resultData.setLinkman(convertCellValueToString(cell));
        // 获取联系电话
        cell = row.getCell(cellNum++);
        resultData.setPhoneNum(convertCellValueToString(cell));
        // 获取主要技术参数
        cell = row.getCell(cellNum++);
        resultData.setTechnicalParam(convertCellValueToString(cell));
        // 获取手册章节
        cell = row.getCell(cellNum++);
        resultData.setManualChapters(convertCellValueToString(cell));
        // 获取工具设备状态
        cell = row.getCell(cellNum++);
        resultData.setToolStatus(convertCellValueToString(cell));
        // 获取特别说明
        cell = row.getCell(cellNum++);
        resultData.setSpecialDescription(convertCellValueToString(cell));
        // 获取备注
        cell = row.getCell(cellNum++);
        resultData.setRemark(convertCellValueToString(cell));
        return resultData;
    }
}
