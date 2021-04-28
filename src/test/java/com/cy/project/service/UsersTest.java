package com.cy.project.service;

import com.cy.project.util.VerifyUsersUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

}
