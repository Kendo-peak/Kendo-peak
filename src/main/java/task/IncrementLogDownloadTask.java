package task;

import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wuhaotai
 * @title: ExportServiceImpl
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:18
 */
public class IncrementLogDownloadTask {
    public boolean downList(List<Map<String,Object>> list, Map<String,String> titleMap, String fileName, String createMan, String operator_type) throws Exception{
        Map responseMap=new HashMap(16);
        //30M
        List excelList = new ArrayList<Object[]>();
        int size = titleMap.keySet().size();
        String[] exceltitle = new String[size];
        int[] titleWeight = new int[size];
        try {
            if (list.size()==0){
                int j=0;
                for (String key : titleMap.keySet()) {
                    exceltitle[j] = titleMap.get(key);
                    titleWeight[j] = 15;
                    j++;
                }
            }
            boolean flag = true;
            for (Map<String,Object> map : list) {
                String[] order = new String[size];
                int i =0;
                for (String key : titleMap.keySet()) {
                    String data = String.valueOf(map.get(key));
                    if (Utils.IsNull(data)){
                        data = "暂无";
                    }
                    order[i] = data;
                    if (flag){
                        exceltitle[i] = titleMap.get(key);
                        titleWeight[i] = 15;
                    }
                    i++;
                }
                excelList.add(order);
                flag = false;
            }
            //清除数据，释放内存
            list.clear();
            String dateDir= Utils.formateDate(6);
            File location = new File(System.getProperty("user.dir") +File.separator+"ExcelFile"+File.separator + dateDir);
            if (!location.exists()) {
                location.mkdirs();
            }

            Utils.poi(exceltitle,excelList, titleWeight, fileName, location.getAbsolutePath(), "xlsx");
            responseMap.put("operator_type",operator_type);
            responseMap.put("filename",dateDir+File.separator+fileName+".xlsx");
            responseMap.put("operator_content","导出成功，详情请下载excel");
            responseMap.put("addman",createMan);
            responseMap.put("addtime",Utils.formateDate(2));
            excelList.clear();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            responseMap.put("operator_type",operator_type);
            responseMap.put("filename","errorFile");
            responseMap.put("operator_content","出现异常,联系技术人员");
            responseMap.put("addman",createMan);
            responseMap.put("addtime",Utils.formateDate(2));
            excelList.clear();
            return false;
        }
    }
}
