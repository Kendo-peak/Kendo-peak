package util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wuhaotai
 * @title: Utils
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:37
 */
public class Utils {

    /**
     * 每个sheet页存储5w条数据，多了分sheet页
     */
    private static int perSheetCount = 50000;

    public static boolean IsNull(Object str) {
        if (null != str && !"".equals(str) && !"null".equals(str)) {
            return false;
        }
        return true;
    }


    /**
     * ********************************************************.<br>
     * [方法] formateDate <br>
     * [描述] 格式化日期 <br>
     * [参数] TODO(对参数的描述) <br>
     * [返回] String <br>
     * [时间] 2017-12-07 下午19:19:19 <br>
     * [作者] lvl
     * ********************************************************.<br>
     */
    public static String formateDate(int style) {
        String date = "";
        String type = "yyyyMMdd";
        switch (style) {
            case 0:
                type = "yyyy-MM-dd";
                break;
            case 1:
                type = "yyyyMMddHHmmss";
                break;
            case 2:
                type = "yyyy-MM-dd HH:mm:ss";
                break;
            case 3:
                type = "HH:mm:ss";
                break;
            case 4:
                type = "yyyyMMddHHmmssSSS";
                break;
            case 5:
                type = "yyyy";
                break;
            case 6:
                type = "yyyyMMdd";
                break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        date = sdf.format(new Date());
        return date;
    }

    //卡号脱敏
    public static void dealCard(List<Map<String, Object>> objList) {
        for (Map<String, Object> tmpMap : objList) {
            Object panObj = tmpMap.get("PAN");
            if (panObj != null && !"".equals(panObj.toString())) {
                String panStr = panObj.toString().substring(0, 6) + "******" + panObj.toString().substring(panObj.toString().length() - 4);
                tmpMap.put("PAN", panStr);
            }
        }
    }

    //代理商刷卡达标明细导出去空
    public static void changeNullPayByCard(Map<String, String> titleMap, Map<String, Object> excelMap) {
        for (String titleKey : titleMap.keySet()) {
            if (!excelMap.keySet().contains(titleKey)) {
                excelMap.put(titleKey, "暂无");
                continue;
            }
            if ("TYPE".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "反服务费" : "交易额达标返现"));
            }
            if ("IS_D001".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "否" : "是"));
            }
            if ("RECH_TYPE".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "Mpos" : "传统pos"));
            }
        }
    }

    //代理流量卡返现明细导出去空
    public static void changeNullFlow(Map<String, String> titleMap, Map<String, Object> excelMap) {
        for (String titleKey : titleMap.keySet()) {
            if (!excelMap.keySet().contains(titleKey)) {
                excelMap.put(titleKey, "暂无");
                continue;
            }
            if ("QUANTITY".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "非首次" : "首次"));
            }
            if ("RECURRENCE_CYCLE".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "月" : "日"));
            }
            if ("RECH_TYPE".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "传统_流量卡" : "lfb_vip"));
            }
        }
    }

    //代理商终端明细字段处理
    public static void changeNullTerminal(Map<String, String> titleMap, Map<String, Object> excelMap) {
        for (String titleKey : titleMap.keySet()) {
            if (!excelMap.keySet().contains(titleKey)) {
                excelMap.put(titleKey, "暂无");
                continue;
            }
            if ("ISBOUND".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "已绑定" : "未绑定"));
            }
            if ("ACTIVATE".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "激活" : "未激活"));
            }
            if ("RETURN_STATUS".equals(titleKey)) {
                excelMap.put(titleKey, (Integer.parseInt(String.valueOf(excelMap.get(titleKey))) == 1 ? "激活已返" : "激活未返"));
            }
            if ("IS_DAY_STAGE".equals(titleKey)) {
                int i = Integer.parseInt(String.valueOf(excelMap.get(titleKey)));
                switch (i) {
                    case -2:
                        excelMap.put(titleKey, "变更商户");
                        break;
                    case -1:
                        excelMap.put(titleKey, "已过考核期");
                        break;
                    case 0:
                        excelMap.put(titleKey, "是");
                        break;
                    case 1:
                        excelMap.put(titleKey, "第一阶段");
                        break;
                    case 2:
                        excelMap.put(titleKey, "第二阶段");
                        break;
                    case 3:
                        excelMap.put(titleKey, "第三阶段");
                        break;
                    case 4:
                        excelMap.put(titleKey, "第四阶段");
                        break;
                    default:
                        excelMap.put(titleKey, "暂无数据");
                        break;
                }
            }
            if ("IS_MON_STAGE".equals(titleKey)) {
                int i = Integer.parseInt(String.valueOf(excelMap.get(titleKey)));
                switch (i) {
                    case -2:
                        excelMap.put(titleKey, "变更商户");
                        break;
                    case -1:
                        excelMap.put(titleKey, "已过考核期");
                        break;
                    case 0:
                        excelMap.put(titleKey, "是");
                        break;
                    case 1:
                        excelMap.put(titleKey, "第一阶段");
                        break;
                    case 2:
                        excelMap.put(titleKey, "第二阶段");
                        break;
                    case 3:
                        excelMap.put(titleKey, "第三阶段");
                        break;
                    case 4:
                        excelMap.put(titleKey, "第四阶段");
                        break;
                    default:
                        excelMap.put(titleKey, "暂无数据");
                        break;
                }
            }
        }
    }

    //处理激活返现字段
    public static List<Map<String, Object>> activateCashBackDetailsList(List list) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Object o : list) {
            Map map = (Map) o;
            if ("".equals(String.valueOf(map.get("RECH_TYPE"))) || map.get("RECH_TYPE") != null) {
                if ("0".equals(String.valueOf(map.get("RECH_TYPE")))) {
                    map.put("RECH_TYPE", "传统Pos");
                }
                if ("1".equals(String.valueOf(map.get("RECH_TYPE")))) {
                    map.put("RECH_TYPE", "Mpos");
                }
            } else {
                map.put("RECH_TYPE", "暂无");
            }
            dataList.add(map);
        }
        return dataList;
    }

    //处理交易明细状态码字段
    public static List<Map<String, Object>> handleTransactionDetailsList(List list) {
        List<Map<String, Object>> dataList = new ArrayList();
        for (Object o : list) {
            Map map = (Map) o;
            //处理交易类型
            if ("".equals(String.valueOf(map.get("MSGTYPE"))) || map.get("MSGTYPE") != null) {
                if ("H007".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "消费");
                }
                if ("S007".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "流量卡消费");
                }
                if ("Y007".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "押金消费");
                }
                if ("V007".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "会员交易");
                }
                if ("H900".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "参数下载");
                }
                if ("H000".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "签到");
                }
                if ("H901".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "公钥下载");
                }
                if ("H902".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "主密钥交换、下载");
                }
                if ("H903".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "IC公钥、参数查询");
                }
                if ("H002".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "余额查询");
                }
                if ("H014".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "退货");
                }
                if ("H013".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "冲正");
                }
                if ("H905".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "应用版本更新查询");
                }
                if ("H906".equals(String.valueOf(map.get("MSGTYPE")))) {
                    map.put("MSGTYPE", "电子签名数据上送");
                }
            } else {
                map.put("MSGTYPE", "暂无");
            }
            //交易状态
            if ("".equals(String.valueOf(map.get("STATUS"))) || map.get("STATUS") != null) {
                if ("0".equals(String.valueOf(map.get("STATUS")))) {
                    map.put("STATUS", "成功");
                }
                if ("2".equals(String.valueOf(map.get("STATUS")))) {
                    map.put("STATUS", "失败");
                }
                if ("20".equals(String.valueOf(map.get("STATUS")))) {
                    map.put("STATUS", "待撤销");
                }
                if ("-1".equals(String.valueOf(map.get("STATUS")))) {
                    map.put("STATUS", "撤销");
                }
                if ("-2".equals(String.valueOf(map.get("STATUS")))) {
                    map.put("STATUS", "冲正");
                }
                if ("50".equals(String.valueOf(map.get("STATUS")))) {
                    map.put("STATUS", "待冲正");
                }
                if ("1".equals(String.valueOf(map.get("STATUS")))) {
                    map.put("STATUS", "补录");
                }
            } else {
                map.put("STATUS", "暂无");
            }
            //卡类型
            if ("".equals(String.valueOf(map.get("CARDTYPE"))) || map.get("CARDTYPE") != null) {
                if ("0".equals(String.valueOf(map.get("CARDTYPE")))) {
                    map.put("CARDTYPE", "未知");
                }
                if ("1".equals(String.valueOf(map.get("CARDTYPE")))) {
                    map.put("CARDTYPE", "借记卡");
                }
                if ("2".equals(String.valueOf(map.get("CARDTYPE")))) {
                    map.put("CARDTYPE", "贷记卡");
                }
            } else {
                map.put("CARDTYPE", "暂无");
            }
            //清算类型
            if ("".equals(String.valueOf(map.get("SETT_TYPE"))) || map.get("SETT_TYPE") != null) {
                if ("0".equals(String.valueOf(map.get("SETT_TYPE")))) {
                    map.put("SETT_TYPE", "S0");
                } else {
                    map.put("SETT_TYPE", "D1");
                }
            }
            //清算状态
            if ("".equals(String.valueOf(map.get("AU_STATE"))) || map.get("AU_STATE") != null) {
                if ("0".equals(String.valueOf(map.get("AU_STATE")))) {
                    map.put("AU_STATE", "未清算");
                } else {
                    map.put("AU_STATE", "已清算");
                }
            }
            dataList.add(map);
        }
        return dataList;
    }

    //处理代理商分润的字段
    public static List<Map<String, Object>> distributionDetailsList(List list) {
        List<Map<String, Object>> dataList = new ArrayList();
        for (Object o : list) {
            Map map = (Map) o;
            //处理商户类别
            if ("".equals(String.valueOf(map.get("MER_CATEGORY"))) || map.get("MER_CATEGORY") != null) {
                if ("0".equals(String.valueOf(map.get("MER_CATEGORY")))) {
                    map.put("MER_CATEGORY", "普通商户");
                }
                if ("1".equals(String.valueOf(map.get("MER_CATEGORY")))) {
                    map.put("MER_CATEGORY", "vip商户");
                }
            } else {
                map.put("MER_CATEGORY", "暂无");
            }
            //卡类型
            if ("".equals(String.valueOf(map.get("CARD_TYPE"))) || map.get("CARD_TYPE") != null) {
                if ("1".equals(String.valueOf(map.get("CARD_TYPE")))) {
                    map.put("CARD_TYPE", "借记卡");
                }
                if ("2".equals(String.valueOf(map.get("CARD_TYPE")))) {
                    map.put("CARD_TYPE", "贷记卡");
                }
            } else {
                map.put("CARD_TYPE", "暂无");
            }
            //结算方式
            if ("".equals(String.valueOf(map.get("SETTLE_METHOD"))) || map.get("SETTLE_METHOD") != null) {
                if ("0".equals(String.valueOf(map.get("SETTLE_METHOD")))) {
                    map.put("SETTLE_METHOD", "D0");
                }
                if ("1".equals(String.valueOf(map.get("SETTLE_METHOD")))) {
                    map.put("SETTLE_METHOD", "T1");
                }
            } else {
                map.put("SETTLE_METHOD", "暂无");
            }
            dataList.add(map);
        }
        return dataList;
    }

    //处理商户月统计数据
    public static List<Map<String, Object>> judgmentDataListByMonth(List list) {
        List<Map<String, Object>> updateList = new ArrayList<>();
        for (Object result : list) {
            Map<String, Object> map = (Map) result;
            String mer_category = String.valueOf(map.get("MER_CATEGORY"));
            switch (mer_category) {
                case "0":
                    map.put("MER_CATEGORY", "标准类");
                    break;
                case "1":
                    map.put("MER_CATEGORY", "优惠类");
                    break;
                case "2":
                    map.put("MER_CATEGORY", "减免类");
                    break;
                default:
                    map.put("MER_CATEGORY", "暂无");
                    break;
            }
            String legal_cer = String.valueOf(map.get("LEGAL_CER"));
            switch (legal_cer) {
                case "0":
                    map.put("LEGAL_CER", "身份证");
                    break;
                case "1":
                    map.put("LEGAL_CER", "护照");
                    break;
                default:
                    map.put("LEGAL_CER", "暂无");
            }
            String mer_status = String.valueOf(map.get("MER_STATUS"));
            switch (mer_status) {
                case "0":
                    map.put("MER_STATUS", "正式");
                    break;
                case "1":
                    map.put("MER_STATUS", "停用");
                    break;
                default:
                    map.put("MER_STATUS", "暂无");
            }
            String settle_cycle = String.valueOf(map.get("SETTLE_CYCLE"));
            switch (settle_cycle) {
                case "0":
                    map.put("SETTLE_CYCLE", "T0");
                    break;
                case "1":
                    map.put("SETTLE_CYCLE", "D1");
                    break;
                default:
                    map.put("SETTLE_CYCLE", "暂无");
            }
            String picture = String.valueOf(map.get("PICTURE"));
            switch (picture) {
                case "0":
                    map.put("PICTURE", "未上传");
                    break;
                case "1":
                    map.put("PICTURE", "已上传");
                    break;
                default:
                    map.put("PICTURE", "暂无");
            }
            updateList.add(map);
        }
        return updateList;
    }

    //终端信息转换数据
    public static List<Map<String, Object>> changeTerminalInformation(List list) {
        List<Map<String, Object>> updateList = new ArrayList<>();
        for (Object result : list) {
            Map<String, Object> map = (Map) result;
            String mer_category = String.valueOf(map.get("MER_CATEGORY"));
            switch (mer_category) {
                case "0":
                    map.put("ISBOUND", "未绑定");
                    break;
                case "1":
                    map.put("ISBOUND", "已绑定");
                    break;
                default:
                    map.put("ISBOUND", "暂无");
                    break;
            }
            updateList.add(map);
        }
        return updateList;
    }

    //交易管理算合计
    public static List<Map<String, Object>> machiningJson(List list) {
        List dateList = new ArrayList();
        //合计Map
        Map countMap = new HashMap(16);
        BigDecimal total = new BigDecimal("0");
        BigDecimal amount = new BigDecimal("0");
        BigDecimal reduction_total = new BigDecimal("0");
        BigDecimal reduction_amount = new BigDecimal("0");
        BigDecimal special_total = new BigDecimal("0");
        BigDecimal special_amount = new BigDecimal("0");
        BigDecimal discount_total = new BigDecimal("0");
        BigDecimal discount_amount = new BigDecimal("0");
        BigDecimal standard_total = new BigDecimal("0");
        BigDecimal standard_amount = new BigDecimal("0");
        BigDecimal debit_total = new BigDecimal("0");
        BigDecimal debit_amount = new BigDecimal("0");
        BigDecimal credit_total = new BigDecimal("0");
        BigDecimal credit_amount = new BigDecimal("0");
        for (Object o : list) {
            Map map = (Map) o;
            if ("0".equals(String.valueOf(map.get("DATA")))) {
                return dateList;
            }
            String Json = String.valueOf(map.get("DATA"));
            //将字符串转成Map
            Map jsonMap = (Map) com.alibaba.fastjson.JSON.parse(Json);
            Set<String> webKey = mapWeb.keySet();
            Set<String> JsonSet = jsonMap.keySet();
            for (String s : webKey) {
                if (!JsonSet.contains(s)) {
                    jsonMap.put(s, "0");
                }
            }
            //将Map发到集合里
            dateList.add(jsonMap);
            //算合计
            total = total.add(new BigDecimal(String.valueOf(jsonMap.get("TOTAL"))));
            amount = amount.add(new BigDecimal(String.valueOf(jsonMap.get("AMOUNT"))));
            reduction_total.add(new BigDecimal(String.valueOf(jsonMap.get("REDUCTION_TOTAL"))));
            if (jsonMap.get("REDUCTION_AMOUNT") == null) {
                reduction_amount = reduction_amount.add(new BigDecimal("0"));
            } else {
                reduction_amount = reduction_amount.add(new BigDecimal(String.valueOf(jsonMap.get("REDUCTION_AMOUNT"))));
            }
            special_total = special_total.add(new BigDecimal(String.valueOf(jsonMap.get("SPECIAL_TOTAL"))));
            if (jsonMap.get("SPECIAL_AMOUNT") == null) {
                special_amount = special_amount.add(new BigDecimal("0"));
            } else {
                special_amount = special_amount.add(new BigDecimal(String.valueOf(jsonMap.get("SPECIAL_AMOUNT"))));
            }
            discount_total = discount_total.add(new BigDecimal(String.valueOf(jsonMap.get("DISCOUNT_TOTAL"))));
            if (jsonMap.get("DISCOUNT_AMOUNT") == null) {
                discount_amount = discount_amount.add(new BigDecimal("0"));
            } else {
                discount_amount = discount_amount.add(new BigDecimal(String.valueOf(jsonMap.get("DISCOUNT_AMOUNT"))));
            }
            standard_total = standard_total.add(new BigDecimal(String.valueOf(jsonMap.get("STANDARD_TOTAL"))));
            if (jsonMap.get("STANDARD_AMOUNT") == null) {
                standard_amount = standard_amount.add(new BigDecimal("0"));
            } else {
                standard_amount = standard_amount.add(new BigDecimal(String.valueOf(jsonMap.get("STANDARD_AMOUNT"))));
            }
            debit_total = debit_total.add(new BigDecimal(String.valueOf(jsonMap.get("DEBIT_TOTAL"))));
            if (jsonMap.get("DEBIT_AMOUNT") == null) {
                debit_amount = debit_amount.add(new BigDecimal("0"));
            } else {
                debit_amount = debit_amount.add(new BigDecimal(String.valueOf(jsonMap.get("DEBIT_AMOUNT"))));
            }
            credit_total = credit_total.add(new BigDecimal(String.valueOf(jsonMap.get("CREDIT_TOTAL"))));
            if (jsonMap.get("CREDIT_AMOUNT") == null) {
                credit_amount = credit_amount.add(new BigDecimal("0"));
            } else {
                credit_amount = credit_amount.add(new BigDecimal(String.valueOf(jsonMap.get("CREDIT_AMOUNT"))));
            }
        }
        countMap.put("TOTAL", total);
        countMap.put("AMOUNT", amount);
        countMap.put("REDUCTION_TOTAL", reduction_total);
        countMap.put("REDUCTION_AMOUNT", reduction_amount);
        countMap.put("SPECIAL_TOTAL", special_total);
        countMap.put("SPECIAL_AMOUNT", special_amount);
        countMap.put("DISCOUNT_TOTAL", discount_total);
        countMap.put("DISCOUNT_AMOUNT", discount_amount);
        countMap.put("STANDARD_TOTAL", standard_total);
        countMap.put("STANDARD_AMOUNT", standard_amount);
        countMap.put("DEBIT_TOTAL", debit_total);
        countMap.put("DEBIT_AMOUNT", debit_amount);
        countMap.put("CREDIT_TOTAL", credit_total);
        countMap.put("CREDIT_AMOUNT", credit_amount);
        dateList.add(countMap);
        return dateList;
    }

    public static Map<String, String> mapWeb = new HashMap<String, String>() {
        {
            put("STANDARD_TOTAL", "0");
            put("STANDARD_AMOUNT", "0");
            put("REDUCTION_TOTAL", "0");
            put("REDUCTION_AMOUNT", "0");
            put("DISCOUNT_TOTAL", "0");
            put("DISCOUNT_AMOUNT", "0");
            put("SPECIAL_TOTAL", "0");
            put("SPECIAL_AMOUNT", "0");
            put("DEBIT_TOTAL", "0");
            put("DEBIT_AMOUNT", "0");
            put("CREDIT_TOTAL", "0");
            put("CREDIT_AMOUNT", "0");
        }
    };

    //交易统计最后一行合计
    public static List<Map<String, Object>> CountJson(List list) {
        List<Map<String, Object>> dateList = new ArrayList<>();
        for (Object o : list) {
            Map<String, Object> map = (Map) o;
            if ("1".equals(String.valueOf(map.get("date_type")))) {
                if ("".equals(map.get("AGENT_NUM")) || map.get("AGENT_NUM") == null) {
                    map.put("AGENT_NUM", "合计");
                    map.put("LOCALDATE", " ");
                    map.put("AGENT_NAME", " ");
                }
            } else {
                if ("".equals(map.get("AGENT_NUM")) || map.get("AGENT_NUM") == null) {
                    map.put("AGENT_NUM", "合计");
                    map.put("CREATE_DATE", " ");
                    map.put("AGENT_NAME", " ");
                }
            }
            dateList.add(map);
        }
        return dateList;
    }

    //处理增值服务数据
    public static List<Map<String, Object>> judgmentDataList(List list, Map param) {
        List<Map<String, Object>> updateList = new ArrayList<>();
        int i = 0;
        for (Object o : list) {
            Map<String, Object> map = (Map) o;
            String type = String.valueOf(map.get("TYPE"));
            switch (type) {
                case "1":
                    map.put("TYPE", "充值");
                    break;
                case "2":
                    map.put("TYPE", "消费");
                    break;
                default:
                    map.put("TYPE", "无");
                    break;
            }
            String insuranceType = String.valueOf(map.get("INSURANCE_TYPE"));
            switch (insuranceType) {
                case "1":
                    map.put("INSURANCE_TYPE", "保险");
                    break;
                case "2":
                    map.put("INSURANCE_TYPE", "短信");
                    break;
                case "3":
                    map.put("INSURANCE_TYPE", "通用");
                    break;
                default:
                    map.put("INSURANCE_TYPE", "无");
                    break;
            }
            updateList.add(map);
            i++;
            if (list.size() == i) {
                HashMap<String, Object> OneMap = new HashMap<>();
                OneMap.put("TYPE", " ");
                OneMap.put("CREATE_DATE", " ");
                OneMap.put("CREATE_TIME", " ");
                OneMap.put("CREATE_NAME", " ");
                OneMap.put("AGENT_NUM", "合计:");
                OneMap.put("AGENT_NAME", "总金额");
                OneMap.put("AMOUNT", param.get("sumamount"));
                OneMap.put("NOTE", "总数量");
                OneMap.put("INSURANCE_TYPE", param.get("countnum"));
                updateList.add(OneMap);
            }
        }
        return updateList;
    }

    //处理历史消费统计数据
    public static List<Map<String, Object>> processingDataList(List list, Map param) {
        List<Map<String, Object>> updateList = new ArrayList<>();
        int i = 0;
        for (Object o : list) {
            Map<String, Object> map = (Map) o;
            updateList.add(map);
            i++;
            if (list.size() == i) {
                HashMap<String, Object> OneMap = new HashMap<>();
                OneMap.put("SERIAL", " ");
                OneMap.put("AGENT_NUM", "合计:");
                OneMap.put("AGENT_NAME", " ");
                OneMap.put("SHOW_MOBILE", " ");
                OneMap.put("SHOW_CERT_NO", " ");
                OneMap.put("CHANNEL_NO", " ");
                OneMap.put("CREATE_DATE", " ");
                OneMap.put("CREATE_TIME", " ");
                OneMap.put("NAME", param.get("countnum"));
                OneMap.put("TRANSACTION_AMOUNT", param.get("channelmoney"));
                OneMap.put("CHANNEL_COST", param.get("consumen"));
                updateList.add(OneMap);
            }
        }
        return updateList;
    }

    //处理交易信息导出
    public static List<Map<String, Object>> changTransaction(List list) {
        for (Object o : list) {
            Map map = (Map) o;
            String status = String.valueOf(map.get("STATUS"));
            switch (status) {
                case "-1":
                    map.put("STATUS", "撤销");
                    break;
                case "-2":
                    map.put("STATUS", "冲正");
                    break;
                case "0":
                    map.put("STATUS", "成功");
                    break;
                case "1":
                    map.put("STATUS", "补录");
                    break;
                case "2":
                    map.put("STATUS", "失败");
                    break;
                default:
                    break;
            }
            String cardtype = String.valueOf(map.get("CARDTYPE"));
            switch (cardtype) {
                case "0":
                    map.put("CARDTYPE", "未知");
                    break;
                case "1":
                    map.put("CARDTYPE", "借记卡");
                    break;
                case "2":
                    map.put("CARDTYPE", "贷记卡");
                    break;
                case "3":
                    map.put("CARDTYPE", "预付费卡");
                    break;
                default:
                    break;
            }
            String settType = String.valueOf(map.get("SETT_TYPE"));
            switch (settType) {
                case "0":
                    map.put("SETT_TYPE", "S0");
                    break;
                case "1":
                    map.put("SETT_TYPE", "D1");
                    break;
                default:
                    break;
            }
            String auState = String.valueOf(map.get("AU_STATE"));
            switch (auState) {
                case "0":
                    map.put("AU_STATE", "未清");
                    break;
                case "1":
                    map.put("AU_STATE", "已清");
                    break;
                default:
                    break;
            }
            String biState = String.valueOf(map.get("BI_STATE"));
            switch (biState) {
                case "0":
                    map.put("BI_STATE", "未对账");
                    break;
                case "1":
                    map.put("BI_STATE", "已对账");
                    break;
                default:
                    break;
            }
            String phonepay = String.valueOf(map.get("PHONEPAY"));
            switch (phonepay) {
                case "0":
                    map.put("PHONEPAY", "普通交易");
                    break;
                case "1":
                    map.put("PHONEPAY", "手机Pay");
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    /**
     * 判断一个Object 是否能转数字
     *
     * @param obj
     * @return boolean
     */
    public static boolean isNumber(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Number) {
            return true;
        } else if (obj instanceof String) {
            try {
                Double.parseDouble((String) obj);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * String null 转 ""
     *
     * @param
     * @return boolean
     */
    public static String castStringNullToEmpty(String value) {
        if (value == null || "null".equals(toLowerCase(value))) {
            value = "";
        }
        return value;
    }

    //将字符串中大写字母转小写字母
    public static String toLowerCase(String str) {
        char[] s = str.toCharArray();
        for (int i = 0; i < s.length; i++) {
            if (s[i] >= 'A' && s[i] <= 'Z') {
                s[i] += 32;
            }
        }
        str = "";
        for (int j = 0; j < s.length; j++) {
            str += s[j];
        }
        return str;
    }


}
