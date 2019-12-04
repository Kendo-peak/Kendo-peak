package socket.startMain;
import socket.nettyServer.Server;

/**
 * @author wuhaotai
 * @title: startAppaction
 * @projectName exportExcel
 * @description: TODO
 * @date 2019/12/414:34
 */
public class startAppaction {

    public static void main(String[] args) {
        try {
            new Server().service();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
