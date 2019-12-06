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
import java.util.Map;

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

    //卡号脱敏
    public static void dealCard(List<Map<String, Object>> objList){
        for (Map<String, Object> tmpMap : objList){
            Object panObj=tmpMap.get("PAN");
            if (panObj!=null && !"".equals(panObj.toString())){
                String panStr=panObj.toString().substring(0,6)+"******"+panObj.toString().substring(panObj.toString().length()-4);
                tmpMap.put("PAN",panStr);
            }
        }
    }
    //代理商刷卡达标明细导出去空
    public static void changeNullPayByCard(Map<String,String> titleMap,Map<String,Object> excelMap){
        for (String titleKey : titleMap.keySet()) {
            if (!excelMap.keySet().contains(titleKey)) {
                excelMap.put(titleKey,"暂无");
                continue;
            }
            if ("TYPE".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "反服务费":"交易额达标返现"));
            }
            if ("IS_D001".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "否":"是"));
            }
            if ("RECH_TYPE".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "Mpos":"传统pos"));
            }
        }
    }
    //代理流量卡返现明细导出去空
    public static void changeNullFlow(Map<String,String> titleMap,Map<String,Object> excelMap){
        for (String titleKey : titleMap.keySet()) {
            if (!excelMap.keySet().contains(titleKey)) {
                excelMap.put(titleKey,"暂无");
                continue;
            }
            if ("QUANTITY".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "非首次":"首次"));
            }
            if ("RECURRENCE_CYCLE".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "月":"日"));
            }
            if ("RECH_TYPE".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "传统_流量卡":"lfb_vip"));
            }
        }
    }
    //代理商终端明细字段处理
    public static void changeNullTerminal(Map<String,String> titleMap,Map<String,Object> excelMap){
        for (String titleKey : titleMap.keySet()) {
            if (!excelMap.keySet().contains(titleKey)) {
                excelMap.put(titleKey,"暂无");
                continue;
            }
            if ("ISBOUND".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "已绑定":"未绑定"));
            }
            if ("ACTIVATE".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "激活":"未激活"));
            }
            if ("RETURN_STATUS".equals(titleKey)){
                excelMap.put(titleKey,(Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "激活已返":"激活未返"));
            }
            if ("IS_DAY_STAGE".equals(titleKey)){
                int i = Integer.parseInt(String.valueOf(excelMap.get(titleKey)));
                switch (i) {
                    case -2:
                        excelMap.put(titleKey,"变更商户");
                        break;
                    case -1:
                        excelMap.put(titleKey,"已过考核期");
                        break;
                    case 0:
                        excelMap.put(titleKey,"是");
                        break;
                    case 1:
                        excelMap.put(titleKey,"第一阶段");
                        break;
                    case 2:
                        excelMap.put(titleKey,"第二阶段");
                        break;
                    case 3:
                        excelMap.put(titleKey,"第三阶段");
                        break;
                    case 4:
                        excelMap.put(titleKey,"第四阶段");
                        break;
                    default:
                        excelMap.put(titleKey,"暂无数据");
                        break;
                }
            }
            if ("IS_MON_STAGE".equals(titleKey)){
                int i = Integer.parseInt(String.valueOf(excelMap.get(titleKey)));
                switch (i) {
                    case -2:
                        excelMap.put(titleKey,"变更商户");
                        break;
                    case -1:
                        excelMap.put(titleKey,"已过考核期");
                        break;
                    case 0:
                        excelMap.put(titleKey,"是");
                        break;
                    case 1:
                        excelMap.put(titleKey,"第一阶段");
                        break;
                    case 2:
                        excelMap.put(titleKey,"第二阶段");
                        break;
                    case 3:
                        excelMap.put(titleKey,"第三阶段");
                        break;
                    case 4:
                        excelMap.put(titleKey,"第四阶段");
                        break;
                    default:
                        excelMap.put(titleKey,"暂无数据");
                        break;
                }
            }
        }
    }
    //处理激活返现字段
    public static List<Map<String, Object>> activateCashBackDetailsList(List list){
        List<Map<String, Object>> dataList=new ArrayList<>();
        for (Object o:list){
            Map map=(Map)o;
            if ("".equals(String.valueOf(map.get("RECH_TYPE"))) || map.get("RECH_TYPE")!=null) {
                if ("0".equals(String.valueOf(map.get("RECH_TYPE")))) {
                    map.put("RECH_TYPE", "传统Pos");
                }
                if ("1".equals(String.valueOf(map.get("RECH_TYPE")))) {
                    map.put("RECH_TYPE", "Mpos");
                }
            }else {
                map.put("RECH_TYPE", "暂无");
            }
            dataList.add(map);
        }
        return dataList;
    }
    //处理交易明细状态码字段
    public static List<Map<String, Object>> handleTransactionDetailsList(List list){
        List<Map<String, Object>> dataList=new ArrayList();
        for (Object o:list){
            Map map=(Map)o;
            //处理交易类型
            if ("".equals(String.valueOf(map.get("MSGTYPE")))||map.get("MSGTYPE")!=null){
                if("H007".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","消费");
                }
                if("S007".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","流量卡消费");
                }
                if("Y007".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","押金消费");
                }
                if("V007".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","会员交易");
                }
                if("H900".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","参数下载");
                }
                if("H000".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","签到");
                }
                if("H901".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","公钥下载");
                }
                if("H902".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","主密钥交换、下载");
                }
                if("H903".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","IC公钥、参数查询");
                }
                if("H002".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","余额查询");
                }
                if("H014".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","退货");
                }
                if("H013".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","冲正");
                }
                if("H905".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","应用版本更新查询");
                }
                if("H906".equals(String.valueOf(map.get("MSGTYPE")))){
                    map.put("MSGTYPE","电子签名数据上送");
                }
            }else {
                map.put("MSGTYPE","暂无");
            }
            //交易状态
            if ("".equals(String.valueOf(map.get("STATUS")))||map.get("STATUS")!=null){
                if ("0".equals(String.valueOf(map.get("STATUS")))){
                    map.put("STATUS","成功");
                }
                if ("2".equals(String.valueOf(map.get("STATUS")))){
                    map.put("STATUS","失败");
                }
                if ("20".equals(String.valueOf(map.get("STATUS")))){
                    map.put("STATUS","待撤销");
                }
                if ("-1".equals(String.valueOf(map.get("STATUS")))){
                    map.put("STATUS","撤销");
                }
                if ("-2".equals(String.valueOf(map.get("STATUS")))){
                    map.put("STATUS","冲正");
                }
                if ("50".equals(String.valueOf(map.get("STATUS")))){
                    map.put("STATUS","待冲正");
                }
                if ("1".equals(String.valueOf(map.get("STATUS")))){
                    map.put("STATUS","补录");
                }
            }else {
                map.put("STATUS","暂无");
            }
            //卡类型
            if ("".equals(String.valueOf(map.get("CARDTYPE")))||map.get("CARDTYPE")!=null){
                if ("0".equals(String.valueOf(map.get("CARDTYPE")))){
                    map.put("CARDTYPE","未知");
                }
                if ("1".equals(String.valueOf(map.get("CARDTYPE")))){
                    map.put("CARDTYPE","借记卡");
                }
                if ("2".equals(String.valueOf(map.get("CARDTYPE")))){
                    map.put("CARDTYPE","贷记卡");
                }
            }else {
                map.put("CARDTYPE","暂无");
            }
            //清算类型
            if ("".equals(String.valueOf(map.get("SETT_TYPE")))||map.get("SETT_TYPE")!=null){
                if ("0".equals(String.valueOf(map.get("SETT_TYPE")))){
                    map.put("SETT_TYPE","S0");
                }else {
                    map.put("SETT_TYPE","D1");
                }
            }
            //清算状态
            if ("".equals(String.valueOf(map.get("AU_STATE")))||map.get("AU_STATE")!=null){
                if ("0".equals(String.valueOf(map.get("AU_STATE")))){
                    map.put("AU_STATE","未清算");
                }else {
                    map.put("AU_STATE","已清算");
                }
            }
            dataList.add(map);
        }
        return dataList;
    }

    //处理代理商分润的字段
    public static List<Map<String, Object>> distributionDetailsList(List list){
        List<Map<String, Object>> dataList=new ArrayList();
        for (Object o:list){
            Map map=(Map)o;
            //处理商户类别
            if ("".equals(String.valueOf(map.get("MER_CATEGORY")))||map.get("MER_CATEGORY")!=null){
                if("0".equals(String.valueOf(map.get("MER_CATEGORY")))){
                    map.put("MER_CATEGORY","普通商户");
                }
                if("1".equals(String.valueOf(map.get("MER_CATEGORY")))){
                    map.put("MER_CATEGORY","vip商户");
                }
            }else {
                map.put("MER_CATEGORY","暂无");
            }
            //卡类型
            if ("".equals(String.valueOf(map.get("CARD_TYPE")))||map.get("CARD_TYPE")!=null){
                if ("1".equals(String.valueOf(map.get("CARD_TYPE")))){
                    map.put("CARD_TYPE","借记卡");
                }
                if ("2".equals(String.valueOf(map.get("CARD_TYPE")))){
                    map.put("CARD_TYPE","贷记卡");
                }
            }else {
                map.put("CARD_TYPE","暂无");
            }
            //结算方式
            if ("".equals(String.valueOf(map.get("SETTLE_METHOD")))||map.get("SETTLE_METHOD")!=null){
                if ("0".equals(String.valueOf(map.get("SETTLE_METHOD")))){
                    map.put("SETTLE_METHOD","D0");
                }
                if ("1".equals(String.valueOf(map.get("SETTLE_METHOD")))){
                    map.put("SETTLE_METHOD","T1");
                }
            }else {
                map.put("SETTLE_METHOD","暂无");
            }
            dataList.add(map);
        }
        return dataList;
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


    /**
     * 判断一个Object 是否能转数字
     * @param obj
     * @return boolean
     */
    public static boolean isNumber (Object obj) {
        if (obj == null){
            return false;
        }
        if (obj instanceof Number) {
            return true;
        } else if (obj instanceof String){
            try{
                Double.parseDouble((String)obj);
                return true;
            }catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * String null 转 ""
     * @param obj
     * @return boolean
     */
    public static String castStringNullToEmpty(String value) {
        if (value == null || "null".equals(toLowerCase(value))){
            value = "";
        }
        return value;
    }

    //将字符串中大写字母转小写字母
    public static String toLowerCase(String str) {
        char[] s=str.toCharArray();
        for(int i=0;i<s.length;i++){
            if(s[i]>='A' && s[i]<='Z'){
                s[i]+=32;
            }
        }
        str="";
        for(int j=0;j<s.length;j++){
            str+=s[j];
        }
        return str;
    }
}
