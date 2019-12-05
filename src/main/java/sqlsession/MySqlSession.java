package sqlsession;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MySqlSession {

    private SqlSession sqlSession;
    private SqlSessionFactory factory ;
    private InputStream resource;

    public SqlSession getSqlSession() throws IOException {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        resource = Resources.getResourceAsStream("mybatis-config.xml");
        factory = builder.build(resource);
        return factory.openSession();
    }

    public <T> T getDao(Class<T> clazz) throws IOException {
        return getSqlSession().getMapper(clazz);
    }

    public void destroy() throws IOException {
        sqlSession.close();
        resource.close();
    }
}
