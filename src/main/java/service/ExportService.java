package service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author wuhaotai
 * @title: ExportService
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:15
 */
public interface ExportService {
    //生成表文件
    public boolean increaseExcel(List<LinkedHashMap<String,String>> reqList) throws Exception;
}
