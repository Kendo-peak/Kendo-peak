package sqlsession;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MySqlSession {

    public static SqlSession getSqlSession() throws IOException {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        InputStream resource = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = builder.build(resource);
        resource.close();
        return factory.openSession();
    }
}
