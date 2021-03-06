package com.stone.mybatis.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.stone.mybatis.bean.Employee;
import com.stone.mybatis.dao.EmployeeMapper;
import com.stone.mybatis.dao.EmployeeMapperAnnotation;

/**
 * 1、接口式编程
 * 原生：		Dao		===>	DaoImpl
 * mybatis	Mapper	===>	xxxMapper.xml
 * 
 * 2、SqlSession代表和数据库的一次会话，用完必须关闭。
 * 3、SqlSession和Connection一样都是非线程安全，每次使用都应该去获取新的对象
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象（将接口和XML进行绑定）
 * EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 * mybatis的全局配置文件：包含数据库的连接池信息，事物管理器信息等系统运行环境信息
 * SQL映射文件保存了每一个SQL语句的映射信息。
 * 
 * @author lei.shi445
 *
 */
public class MybatisTest {
	
	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	
	/**
	 * 1、根据XML配置文件（全局配置文件）创建一个SqlSessionFactory对象
	 * 2、SQL映射文件，配置每一个SQL以及封装规则
	 * 3、将SQL映射文件注册到全局配置文件中
	 * 4、根据全局配置文件得到SqlSessionFactory
	 * 5、使用SqlSessionFactory获取到SqlSession对象执行增删改查，一个SqlSession对象就是代码和数据库的一次会话，用完关闭
	 * 6、使用SQL的唯一标识来告诉Mybatis执行那个SQL，SQL都是保存在SQL映射文件中
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//7、获取SqlSession实例，能直接执行已经映射的SQL语句
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Employee employee = sqlSession.selectOne("EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} finally {
			sqlSession.close();
		}
	}

	//接口式编程
	@Test
	public void test01() throws IOException {
		//1、获取SQLSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2、获取SqlSession对象
		SqlSession openSession = sqlSessionFactory.openSession();
		
		//3、获取接口的实现类对象，会为接口自动创建一个代理对象，代理对象去执行增删改查方法
		try {
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			openSession.close();
		}
	}
	
	@Test
	public void test02() throws IOException {
		//1、获取SQLSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2、获取SqlSession对象
		SqlSession openSession = sqlSessionFactory.openSession();
		
		//3、获取接口的实现类对象，会为接口自动创建一个代理对象，代理对象去执行增删改查方法
		try {
			EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			openSession.close();
		}
	}
	
}
