package cn.bugstack.mybatis.test;

import cn.bugstack.mybatis.binding.MapperRegistry;
import cn.bugstack.mybatis.session.SqlSession;
import cn.bugstack.mybatis.session.SqlSessionFactory;
import cn.bugstack.mybatis.session.defaults.DefaultSqlSessionFactory;
import cn.bugstack.mybatis.test.dao.IUserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 小傅哥，微信：fustack
 * @description 单元测试
 * @date 2022/3/26
 * @github https://github.com/fuzhengwei
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    /**
     * 4、执行流程
     * ① new MapperRegistry()：实例化MapperRegistry
     *
     * ② mapperRegistry.addMappers("org.liuc.mybatis.test.dao")：
     * 向MapperRegistry中注入Mapper接口的包路径，解析出Mapper接口并生成对应的代理工厂存入knownMapper容器中
     *
     * ③ new DefaultSqlSessionFactory(mapperRegistry)：
     * 实例化DefaultSqlSession时注入MapperRegistry，就能与映射器注册机衔接
     *
     * ④ sqlSessionFactory.openSession()：
     * 使用工厂实例化DefaultSqlSession，将③中注入的MapperRegistry，再次注入DefaultSqlSession中
     *
     * ④ sqlSession.getMapper(IUserDao.class)：
     * 通过DefaultSqlSession创建Mapper接口的代理对象，即从实例化时注入的MapperRegistry中获取映射器代理工厂创建映射器代理对象，并且给Mapper代理对象注入sqlSession
     *
     * ⑤ userDao.queryUserName("10001")：
     * 执行Mapper接口定义的方法，就会被反射拦截，执行Mapper代理对象的invoke方法，在invoke方法内调用sqlSession中定义的具体的SQL方法selectOne
     *
     * 5、总结
     * ① 前两章使用了简单工厂模式，封装DefaultSqlSession、MapperProxy的实例化过程，对外提供统一的方法，屏蔽了很多细节以及上下文的关联关系，都交由工厂管理，方便了用户使用
     *
     * ② SqlSession中的入参和返回值类型都用的泛型，因为需要传入指定Mapper接口的类对象，才能从MapperRegistry中获取指定Mapper接口的工厂，通过泛型能够使框架更具通用性
     */

    @Test
    public void test_MapperProxyFactory() {
        // 1. 注册 Mapper
        MapperRegistry registry = new MapperRegistry();
        registry.addMappers("cn.bugstack.mybatis.test.dao");

        // 2. 从 SqlSession 工厂获取 Session
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4. 测试验证
        String res = userDao.queryUserName("10001");
        logger.info("测试结果：{}", res);
    }

}
