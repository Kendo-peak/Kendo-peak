package dao;

import java.util.List;
import java.util.Map;
public interface UserDao {

    List<Map<String,Object>> getAllUser();

    List<Map<String,Object>> distributionDetailsExportExcel(Map map);
}
