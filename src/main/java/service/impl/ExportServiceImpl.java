package service.impl;

import dao.UserDao;
import service.ExportService;
import sqlsession.MySqlSession;
import task.ExportExcel;
import task.IncrementLogDownloadTask;
import util.Utils;

import java.io.IOException;
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

    private MySqlSession mySqlSession = new MySqlSession();

    private IncrementLogDownloadTask task = new IncrementLogDownloadTask();

    /**
     *
     *********************************************************.<br>
     * [方法] increaseExcel <br>
     * [描述] 生成表格<br>
     * [参数] [List](对参数的描述) <br>
     * [返回] boolean <br>
     * [日期] 2019/12/4
     * [时间] 16:31
     * [作者] wuhaotai
     *********************************************************.<br>
     */
    @Override
    public boolean increaseExcel(List<LinkedHashMap<String,String>> reqList){
        Map map=null;
        try {
            //判断导出什么表 查相对应的sql
            List<Map<String,Object>> dataList=findListByExcel(reqList);
            //生成表头
            LinkedHashMap<String, String> titileMap=createTitileMap(reqList);
            //生成文件名
            String fileName=createFileName(reqList);
            for (Object o:reqList){
                map=(Map)o;
            }
            //创建人
            String createMan=String.valueOf(map.get("createMan"));
            //操作类型
            String operator_type=String.valueOf(map.get("operator_type"));
            //根据数据生成excel文件
            Map<String, Object> wb=null;
            wb = ExportExcel.exportListExcelClearExcel(wb, dataList, titileMap, fileName);
            boolean flag = task.downList(wb,fileName,createMan,operator_type);
            map.clear();
            return flag==true ? true:false;
        } catch (Exception e) {
            e.printStackTrace();
            map.clear();
            return false;
        }
    }

    /**
     *
     *********************************************************.<br>
     * [方法] findListByExcel <br>
     * [描述] 判断导出什么表 查相对应的sql<br>
     * [参数]  * @param null(对参数的描述) <br>
     * [返回]  <br>
     * [日期] 2019/12/5
     * [时间] 10:00
     * [作者] wuhaotai
     *********************************************************.<br>
     */
    private List<Map<String,Object>> findListByExcel(List reqList) throws IOException {
        //list中0下标是检索条件
        Map<String,Object> map = (Map)reqList.get(0);
        int operator_type=Integer.parseInt(String.valueOf(map.get("operator_type")));
        switch (operator_type){
            case 18:
                //代理商分润明细导出
                //查数据
                List<Map<String, Object>> list = mySqlSession.getDao(UserDao.class).distributionDetailsExportExcel(map);
                //mySqlSession.destroy();
                //处理数据
                Utils.dealCard(list);
                List<Map<String, Object>> dataList = Utils.distributionDetailsList(list);
                return dataList;
            case 19:
                //代理商交易明细导出
                break;
            case 24:
                //代理商激活返现明细导出
                break;
            case 21:
                //代理商终端明细导出
                break;
            case 22:
                //代理商流量卡返现明细导出
                break;
            case 23:
                //代理商刷卡达标明细导出
                break;
           default:
               break;
        }
        return null;
    }
    /**
     *
     *********************************************************.<br>
     * [方法] createTitileMap <br>
     * [描述] 生成表头<br>
     * [参数]  * @param null(对参数的描述) <br>
     * [返回]  <br>
     * [日期] 2019/12/5
     * [时间] 10:00
     * [作者] wuhaotai
     *********************************************************.<br>
     */
    private LinkedHashMap<String, String> createTitileMap(List<LinkedHashMap<String,String>> reqList){
        //1下标是表头
        LinkedHashMap<String, String> titleMap=reqList.get(1);
        return titleMap;
    }
    /**
     *
     *********************************************************.<br>
     * [方法] createFileName <br>
     * [描述] 生成表名 <br>
     * [参数]  * @param null(对参数的描述) <br>
     * [返回]  <br>
     * [日期] 2019/12/5
     * [时间] 10:04
     * [作者] wuhaotai
     *********************************************************.<br>
     */
    private String createFileName(List reqList){
        //list中0下标是检索条件
        Map<String,Object> map = (Map)reqList.get(0);
        int operator_type=Integer.parseInt(String.valueOf(map.get("operator_type")));
        switch (operator_type){
            case 18:
                //代理商分润明细导出
                return Utils.formateDate(1)+"代理商分润明细";
            case 19:
                //代理商交易明细导出
                return Utils.formateDate(1)+"代理商交易明细";
            case 24:
                //代理商激活返现明细导出
                return Utils.formateDate(1)+"代理商激活返现明细";
            case 21:
                //代理商终端明细导出
                return Utils.formateDate(1)+"代理商终端明细";
            case 22:
                //代理商流量卡返现明细导出
                return Utils.formateDate(1)+"代理商流量卡返现明细";
            case 23:
                //代理商刷卡达标明细导出
                return Utils.formateDate(1)+"代理商刷卡达标明细";
            default:
                break;
        }
        return "";
    }
}
