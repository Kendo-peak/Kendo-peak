package test;

import dao.UserDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class UserTest {

    private UserDao userDao;
    private SqlSession sqlSession;
    private SqlSessionFactory factory ;
    private InputStream resource;

    @Before
    public void init() throws IOException {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        resource = Resources.getResourceAsStream("mybatis-config.xml");
        factory = builder.build(resource);
    }

    @Test
    public void fun() {
        sqlSession = factory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        System.out.println(userDao.getAllUser());
    }

    @After
    public void destroy() throws IOException {
        sqlSession.close();
        resource.close();
    }
}
