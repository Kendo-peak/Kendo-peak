package util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wuhaotai
 * @title: Utils
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:37
 */
public class Utils {

    /** 每个sheet页存储5w条数据，多了分sheet页 */
    private static int perSheetCount = 50000;

    public static boolean IsNull(Object str){
        if(null !=str&&!"".equals(str)&&!"null".equals(str)){
            return false;
        }
        return true;
    }

    /**
     *
     *********************************************************.<br>
     * [方法] formateDate <br>
     * [描述] 格式化日期 <br>
     * [参数] TODO(对参数的描述) <br>
     * [返回] String <br>
     * [时间] 2017-12-07 下午19:19:19 <br>
     * [作者] lvl
     *********************************************************.<br>
     */
    public static String formateDate(int style){
        String date="";
        String type="yyyyMMdd";
        switch (style) {
            case 0:
                type="yyyy-MM-dd";
                break;
            case 1:
                type="yyyyMMddHHmmss";
                break;
            case 2:
                type="yyyy-MM-dd HH:mm:ss";
                break;
            case 3:
                type="HH:mm:ss";
                break;
            case 4:
                type="yyyyMMddHHmmssSSS";
                break;
            case 5:
                type="yyyy";
                break;
            case 6:
                type="yyyyMMdd";
                break;
        }
        SimpleDateFormat sdf=new SimpleDateFormat(type);
        date=sdf.format(new Date());
        return date;
    }


    /**
     * @Author: gxg
     * @Date: 2019/11/18 10:38
     * @Description:  数据量大时分sheet页
     * @Version: 1.0
     */
    public static File poi(String[] exceltitle, List<Object[]> list, int[] colunwidth, String fielName, String path, String type) throws IOException {
        // 创建HSSFWorkbook对象(excel的文档对象)
        File file = null;
        FileOutputStream fos = null;
        Workbook wb=null;
        try {
            File isfile = new File(path);
            if (!isfile.exists()) {
                isfile.mkdirs();
            }
            file = new File(path, fielName + "." + type);
            file.createNewFile();
            fos = new FileOutputStream(file);
            if ("xlsx".equals(type)) {
                wb = new XSSFWorkbook();
                int listSize = list.size();
                for (int page = 0 ; page < listSize/perSheetCount+1 ; page ++){
                    List<Object[]> listFrom = (List<Object[]>) list.subList((page) * perSheetCount, (page + 1) * perSheetCount > listSize ? list.size() : (page + 1) * perSheetCount);
                    List<Object[]> listTo = new ArrayList();
                    for (Object[] objects : listFrom) {
                        listTo.add(objects);
                    }
                    if (null != exceltitle) {
                        listTo.add(0,exceltitle);
                    }
                    // 建立新的sheet对象（excel的表单）
                    Sheet sheet = wb.createSheet("sheet"+(page+1));
                    // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
                    // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
                    CellStyle cellStyle = wb.createCellStyle();
                    Font fontStyle = wb.createFont();
                    fontStyle.setFontName("宋体");
                    fontStyle.setFontHeightInPoints((short) 14);
                    cellStyle.setFont(fontStyle);
                    // 在sheet里创建第二行
                    for (int i = 0; i < listTo.size(); i++) {
                        Row row = sheet.createRow(i);
                        row.setRowStyle(cellStyle);
                        for (int j = 0; j < listTo.get(i).length; j++) {
                            //设置单元格值
                            row.createCell(j).setCellValue(listTo.get(i)[j] + "");
                            //设置指定列的列宽，256 * 50这种写法是因为width参数单位是单个字符的256分之一
                            if (colunwidth[j] != 0) {
                                sheet.setColumnWidth(j, 256 * colunwidth[j]);
                            }
                        }
                    }
                }
                wb.write(fos);
            } else if ("xls".equals(type)) {
                wb = new HSSFWorkbook();
                // 建立新的sheet对象（excel的表单）
                int listSize = list.size();
                for (int page = 0 ; page < listSize/perSheetCount+1 ; page ++) {
                    List<Object[]> listFrom = (List<Object[]>) list.subList((page) * perSheetCount, (page + 1) * perSheetCount > listSize ? list.size() : (page + 1) * perSheetCount);
                    List<Object[]> listTo = new ArrayList();
                    for (Object[] objects : listFrom) {
                        listTo.add(objects);
                    }
                    if (null != exceltitle) {
                        listTo.add(0,exceltitle);
                    }
                    Sheet sheet = wb.createSheet("sheet"+(page+1));
                    // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
                    // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
                    CellStyle cellStyle = wb.createCellStyle();
                    Font fontStyle = wb.createFont();
                    fontStyle.setFontName("宋体");
                    fontStyle.setFontHeightInPoints((short) 14);
                    cellStyle.setFont(fontStyle);
                    // 在sheet里创建第二行
                    for (int i = 0; i < listTo.size(); i++) {
                        Row row = sheet.createRow(i);
                        row.setRowStyle(cellStyle);
                        for (int j = 0; j < listTo.get(i).length; j++) {
                            //设置单元格值
                            row.createCell(j).setCellValue(listTo.get(i)[j] + "");
                            //设置指定列的列宽，256 * 50这种写法是因为width参数单位是单个字符的256分之一
                            if (colunwidth[j] != 0) {
                                sheet.setColumnWidth(j, 256 * colunwidth[j]);
                            }
                        }
                    }
                }
                wb.write(fos);
            }
        } catch (IOException e) {
            throw e;
        }finally {
            if(null!=list&&list.size()>0){
                list.clear();
            }
            if(null !=fos){
                fos.close();
            }
            if(null!=wb){
                wb.close();
            }
        }

        return file;
    }
}
