package com.cy.project.service;

import com.cy.project.util.VerifyUsersUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.sql.DataSource;
import java.util.Map;

@SpringBootTest
public class UsersTest {

    @Test
    public void test1(){
        String abc123 = VerifyUsersUtil.getToken("abc123");
        System.out.println(abc123);
    }

    @Test
    public void test2(){
        boolean verify = VerifyUsersUtil.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2Vyc0FjY291bnQiOiJhYmMxMjMiLCJleHAiOjE2MTk2MTE5NTR9.MyPdwEIWmssklXa_dZAmqRgSArpSAbKZTcUFm91npKY");
        System.out.println(verify);
    }

    @Test
    public void test3(){
        String tokenInfo = VerifyUsersUtil.getTokenInfo("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2Vyc0FjY291bnQiOiJhYmMxMjMiLCJleHAiOjE2MTk2MTIxNDF9.kfeFfbEx0iWodLSSM6h6d0_q12JpmsJLExHdk_9w0Jk");
        System.out.println(tokenInfo);
    }

    @Autowired
    private ApplicationContext appContext;
    @Autowired
    private JpaProperties jpaProperties;
    @Autowired
    private DataSource dataSource;

    @Test
    public void test4(){
//        Map<String, Object> stringObjectMap = new HibernateProperties().determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());



        System.out.println(appContext.getEnvironment().getProperty("user.timezone"));
        for (Map.Entry en : System.getProperties().entrySet()) {
            System.out.println(en);
        }

    }

}
