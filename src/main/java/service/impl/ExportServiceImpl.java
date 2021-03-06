package service.impl;

import dao.UserDao;
import fastdfs.FastDFSClient;
import org.apache.ibatis.session.SqlSession;
import service.ExportService;
import sqlsession.MySqlSession;
import task.ExportExcel;
import task.IncrementLogDownloadTask;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuhaotai
 * @title: ExportServiceImpl
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:18
 */
public class ExportServiceImpl implements ExportService {

    private IncrementLogDownloadTask task = new IncrementLogDownloadTask();

    /**
     * ********************************************************.<br>
     * [方法] increaseExcel <br>
     * [描述] 生成表格<br>
     * [参数] [List](对参数的描述) <br>
     * [返回] boolean <br>
     * [日期] 2019/12/4
     * [时间] 16:31
     * [作者] wuhaotai
     * ********************************************************.<br>
     */
    @Override
    public void increaseExcel(List<LinkedHashMap<String, String>> reqList) {
        Map<String, Object> map = (Map) reqList.get(0);
        List<Map<String, Object>> dataList = null;
        LinkedHashMap<String, String> titileMap = null;
        try {
            //判断导出什么表 查相对应的sql
            dataList = findListByExcel(reqList);
            //生成表头
            titileMap = createTitileMap(reqList);
            //生成文件名
            String fileName = createFileName(reqList);
            //创建人
            String createMan = String.valueOf(map.get("creatMan"));
            //操作类型
            String operator_type = String.valueOf(map.get("operator_type"));
            //根据数据生成excel文件
            Map<String, Object> wb = null;
            wb = ExportExcel.exportListExcelClearExcel(wb, dataList, titileMap, fileName);
            File file = task.downList(wb, fileName, operator_type);
            map.clear();
            if (file.exists()) {
                //上传文件
                Map<String, String> saveMap = new HashMap(16);
                Map<String, String> metaList = new HashMap<>();
                metaList.put("fileName", fileName);
                metaList.put("fileType", fileName.substring(fileName.lastIndexOf(".") + 1));
                metaList.put("author", createMan);
                metaList.put("date", String.valueOf(Utils.formateDate(0)));
                //group1/M00/00/00/CgqyYl0CLD2AHkiHABaE6B6DRK0737.jpg
                String fid = FastDFSClient.uploadFile(file, file.getName(), metaList);
                if (!Utils.IsNull(fid)) {
                    //保存信息到jobtask
                    String dateDir = Utils.formateDate(6);
                    saveMap.put("fileid", fid);
                    saveMap.put("operator_type", operator_type);
                    saveMap.put("filename", dateDir + File.separator + fileName + ".xlsx");
                    saveMap.put("operator_content", "导出成功，详情请下载excel");
                    saveMap.put("addman", createMan);
                    saveMap.put("addtime", Utils.formateDate(2));
                    saveJobtask(saveMap);
                    saveMap.clear();
                    //删除本地文件
                    file.delete();
                } else {
                    saveMap.put("fileid", "");
                    saveMap.put("operator_type", operator_type);
                    saveMap.put("filename", "errorFile");
                    saveMap.put("operator_content", "出现异常,联系技术人员");
                    saveMap.put("addman", createMan);
                    saveMap.put("addtime", Utils.formateDate(2));
                    saveJobtask(saveMap);
                    saveMap.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.clear();
        } finally {
            dataList.clear();
            titileMap.clear();
        }
    }


    /**
     * ********************************************************.<br>
     * [方法] saveJobtask <br>
     * [描述] 保存到jobtask<br>
     * [参数]  * @param null(对参数的描述) <br>
     * [返回]  <br>
     * [日期] 2019/12/9
     * [时间] 10:05
     * [作者] wuhaotai
     * ********************************************************.<br>
     */
    private void saveJobtask(Map map) throws IOException {
        try (SqlSession session = MySqlSession.getSqlSession()) {
            UserDao userDao = session.getMapper(UserDao.class);
            userDao.saveJobTask(map);
        }
    }

    /**
     * ********************************************************.<br>
     * [方法] findListByExcel <br>
     * [描述] 判断导出什么表 查相对应的sql<br>
     * [参数]  * @param null(对参数的描述) <br>
     * [返回]  <br>
     * [日期] 2019/12/5
     * [时间] 10:00
     * [作者] wuhaotai
     * ********************************************************.<br>
     */
    private List<Map<String, Object>> findListByExcel(List<LinkedHashMap<String, String>> reqList) throws IOException {
        List<Map<String, Object>> dataList = null;
        try (SqlSession session = MySqlSession.getSqlSession()) {
            UserDao userDao = session.getMapper(UserDao.class);
            //list中0下标是检索条件
            Map<String, Object> map = (Map) reqList.get(0);
            LinkedHashMap<String, String> titleMap = reqList.get(1);
            int operator_type = Integer.parseInt(String.valueOf(map.get("operator_type")));
            switch (operator_type) {
                case 18:
                    //代理商分润明细导出
                    List<Map<String, Object>> list = userDao.distributionDetailsExportExcel(map);
                    //处理数据
                    Utils.dealCard(list);
                    dataList = Utils.distributionDetailsList(list);
                    break;
                case 19:
                    //代理商交易明细导出
                    List<Map<String, Object>> transactionList = userDao.transactionDetailsExportExcel(map);
                    //处理数据
                    Utils.dealCard(transactionList);
                    dataList = Utils.handleTransactionDetailsList(transactionList);
                    break;
                case 24:
                    //代理商激活返现明细导出
                    List<Map<String, Object>> activateCashBackList = userDao.activateCashBackDetailsExportExcel(map);
                    dataList = Utils.activateCashBackDetailsList(activateCashBackList);
                    break;
                case 21:
                    //代理商终端明细导出
                    dataList = userDao.getexportterminalDetails(map);
                    for (Map<String, Object> stringObjectMap : dataList) {
                        Utils.changeNullTerminal(titleMap, stringObjectMap);
                    }
                    break;
                case 22:
                    //代理商流量卡返现明细导出
                    dataList = userDao.getFlowDetailsExport(map);
                    for (Map<String, Object> stringObjectMap : dataList) {
                        Utils.changeNullFlow(titleMap, stringObjectMap);
                    }
                    break;
                case 23:
                    //代理商刷卡达标明细导出
                    dataList = userDao.getpayByCardDetailedExport(map);
                    for (Map<String, Object> stringObjectMap : dataList) {
                        Utils.changeNullPayByCard(titleMap, stringObjectMap);
                    }
                    break;
                case 25:
                    //商户月统计明细导出
                    dataList = Utils.judgmentDataListByMonth(userDao.getMerchantMonthExport(map));
                    break;
                case 26:
                    //终端明细导出
                    dataList = Utils.changeTerminalInformation(userDao.getTerminalInformationData(map));
                    break;
                case 27:
                    //交易管理日导出
                    dataList = Utils.CountJson(Utils.machiningJson(userDao.getDateCountJsonExcelExport(map)));
                    break;
                case 28:
                    //交易管理月导出
                    dataList = Utils.CountJson(Utils.machiningJson(userDao.getDateCountJsonExcelExport(map)));
                    break;
                case 29:
                    //交易管理日导出
                    dataList = Utils.CountJson(Utils.machiningJson(userDao.getDateCountJsonExcelExport(map)));
                    break;
                case 30:
                    //账户记录明细导出
                    dataList =Utils.judgmentDataList(userDao.getExportDataBycount(map),map);
                    break;
                case 31:
                    //历史消费详情导出
                    dataList =Utils.processingDataList(userDao.getexportExcelByAppreciation(map),map);
                    break;
                case 32:
                    //交易信息导出
                    dataList =Utils.changTransaction(userDao.getList(map));
                    break;
                default:
                    break;
            }
            return dataList;
        }
    }

    /**
     * ********************************************************.<br>
     * [方法] createTitileMap <br>
     * [描述] 生成表头<br>
     * [参数]  * @param null(对参数的描述) <br>
     * [返回]  <br>
     * [日期] 2019/12/5
     * [时间] 10:00
     * [作者] wuhaotai
     * ********************************************************.<br>
     */
    private LinkedHashMap<String, String> createTitileMap(List<LinkedHashMap<String, String>> reqList) {
        //1下标是表头
        LinkedHashMap<String, String> titleMap = reqList.get(1);
        return titleMap;
    }

    /**
     * ********************************************************.<br>
     * [方法] createFileName <br>
     * [描述] 生成表名 <br>
     * [参数]  * @param null(对参数的描述) <br>
     * [返回]  <br>
     * [日期] 2019/12/5
     * [时间] 10:04
     * [作者] wuhaotai
     * ********************************************************.<br>
     */
    private String createFileName(List reqList) {
        //list中0下标是检索条件
        Map<String, Object> map = (Map) reqList.get(0);
        int operator_type = Integer.parseInt(String.valueOf(map.get("operator_type")));
        switch (operator_type) {
            case 18:
                //代理商分润明细导出
                return Utils.formateDate(1) + "代理商分润明细";
            case 19:
                //代理商交易明细导出
                return Utils.formateDate(1) + "代理商交易明细";
            case 24:
                //代理商激活返现明细导出
                return Utils.formateDate(1) + "代理商激活返现明细";
            case 21:
                //代理商终端明细导出
                return Utils.formateDate(1) + "代理商终端明细";
            case 22:
                //代理商流量卡返现明细导出
                return Utils.formateDate(1) + "代理商流量卡返现明细";
            case 23:
                //代理商刷卡达标明细导出
                return Utils.formateDate(1) + "代理商刷卡达标明细";
            case 25:
                //商户月统计明细导出
                return Utils.formateDate(1) + "商户月统计明细";
            case 26:
                //yhtrade终端明细导出
                return Utils.formateDate(1) + "终端明细";
            case 27:
                //交易管理日导出
                return Utils.formateDate(1) + "交易管理日导出";
            case 28:
                //交易管理日导出
                return Utils.formateDate(1) + "交易管理月导出";
            case 29:
                //交易管理年导出
                return Utils.formateDate(1) + "交易管理年导出";
            case 30:
                //账户记录导出明细
                return Utils.formateDate(1) + "账户记录导出明细";
            case 31:
                //账户记录导出明细
                return Utils.formateDate(1) + "历史消费导出明细";
            case 32:
                //账户记录导出明细
                return Utils.formateDate(1) + "交易信息导出";
            default:
                break;
        }
        return "";
    }
}
