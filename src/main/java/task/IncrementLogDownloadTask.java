package task;

import org.apache.poi.ss.usermodel.Workbook;
import util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author wuhaotai
 * @title: ExportServiceImpl
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:18
 */
public class IncrementLogDownloadTask {
    public boolean downList(Map<String, Object> wb,String fileName,String createMan,String operator_type) throws Exception {
        Map responseMap = new HashMap(16);
        FileOutputStream fos = null;
        File file = null;
        Workbook workBook=null;
        try {
            String dateDir= Utils.formateDate(6);
            //File location = new File(System.getProperty("user.dir") +File.separator+"ExcelFile"+File.separator + dateDir);
            File location = new File("D://");
            if (!location.exists()) {
                location.mkdirs();
            }
                File isfile = new File(location.getAbsolutePath());
                if (!isfile.exists()) {
                    isfile.mkdirs();
                }
                file = new File(location.getAbsolutePath(), fileName + ".xlsx");
                file.createNewFile();
                fos = new FileOutputStream(file);
                workBook = (Workbook) wb.get("workBook");
                if(workBook==null){
                    return false;
                }
                workBook.write(fos);
                responseMap.put("operator_type", operator_type);
                responseMap.put("filename", dateDir + File.separator + fileName + ".xlsx");
                responseMap.put("operator_content", "导出成功，详情请下载excel");
                responseMap.put("addman", createMan);
                responseMap.put("addtime", Utils.formateDate(2));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                responseMap.put("operator_type", operator_type);
                responseMap.put("filename", "errorFile");
                responseMap.put("operator_content", "出现异常,联系技术人员");
                responseMap.put("addman", createMan);
                responseMap.put("addtime", Utils.formateDate(2));
                return false;
            }
    }

}
