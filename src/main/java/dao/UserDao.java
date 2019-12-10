package dao;

import java.util.List;
import java.util.Map;
public interface UserDao {

    List<Map<String,Object>> getAllUser();

    //代理商分润明细导出
    List<Map<String,Object>> distributionDetailsExportExcel(Map map);

    //代理商交易明细导出
    List<Map<String,Object>> transactionDetailsExportExcel(Map<String, Object> map);

    //代理商激活返现明细导出
    List<Map<String,Object>> activateCashBackDetailsExportExcel(Map<String, Object> map);

    //代理商终端明细导出
    List<Map<String,Object>> getexportterminalDetails(Map<String, Object> map);

    //代理商流量卡返现明细导出
    List<Map<String,Object>> getFlowDetailsExport(Map<String, Object> map);

    //代理商刷卡达标明细导出
    List<Map<String,Object>> getpayByCardDetailedExport(Map<String, Object> map);

    //保存到jobtask表
    void saveJobTask(Map map);

    //商户月统计
    List<Map<String,Object>> getMerchantMonthExport(Map<String, Object> map);

    //终端明细导出
    List<Map<String,Object>> getTerminalInformationData(Map<String, Object> map);

    //交易统计年月日报表导出
    List<Map<String,Object>> getDateCountJsonExcelExport(Map<String, Object> map);

    //账户记录导出明细
    List<Map<String,Object>> getExportDataBycount(Map<String, Object> map);

    //历史消费详情导出
    List<Map<String,Object>> getexportExcelByAppreciation(Map<String, Object> map);

    //交易信息导出
    List<Map<String,Object>> getList(Map<String, Object> map);

}
