package service;

import java.util.Map;

/**
 * @author wuhaotai
 * @title: ExportService
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:15
 */
public interface ExportService {
    //生成表文件
    public boolean increaseExcel(Map map);
}
