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
    public File downList(Map<String, Object> wb,String fileName,String operator_type) throws Exception {
        FileOutputStream fos = null;
        File file = null;
        Workbook workBook=null;
        try {
            String dateDir= Utils.formateDate(6);
            File location = new File(System.getProperty("user.dir") +File.separator+"ExcelFile");
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
                if(workBook!=null){
                workBook.write(fos);
                return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
    }

}
