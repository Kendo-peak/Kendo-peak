package util;

/**
 * @author wuhaotai
 * @title: Utils
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/416:37
 */
public class Utils {
    public static boolean IsNull(Object str){
        if(null !=str&&!"".equals(str)&&!"null".equals(str)){
            return false;
        }
        return true;
    }
}
