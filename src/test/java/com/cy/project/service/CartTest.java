package com.cy.project.service;

import com.cy.project.entity.CartItem;
import com.cy.project.entity.Product;
import com.cy.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class CartTest {

//    @Autowired
//    private StringRedisTemplate srt;

    @Resource
    private RedisTemplate<String,CartItem> redisTemplate;

//    private HashOperations<String,String,CartItem> opsForHash;

    @Autowired
    private ProductRepository pr;
    @Test
    public void test1(HttpServletRequest request, HttpServletResponse response,@CookieValue(value = "myUUID",required = false)String myUUID){

        HttpSession session = request.getSession();

        String usersAccount = (String)session.getAttribute("users");

        int id=100;
        int count=3;


        if (usersAccount==null&&myUUID==null){
            usersAccount=UUID.randomUUID().toString();

            response.addCookie(new Cookie("myUUID", usersAccount));

            System.out.println(UUID.randomUUID().toString());
        }else {
            usersAccount=myUUID;
            System.out.println("myUUID");
        }


        Product product = pr.findById(id).orElse(null);



        CartItem item = new CartItem();
        item.setProduct(product);
        item.setCount(count);
        item.setSubTotalPrice(product.getProductPrice()*count);

        HashOperations<String,Integer,CartItem>  opsForHash=redisTemplate.opsForHash();
        opsForHash.put(usersAccount,id,item);


        List<Object> abc123 = redisTemplate.opsForHash().values(usersAccount);
        for (Object pr:abc123) {
            System.out.println(pr);
        }
    }

    @Test
    public void test2(){

        int id=100;
        int count=3;

        HashOperations<String,Integer,CartItem>  opsForHash=redisTemplate.opsForHash();
        String usersAccount=UUID.randomUUID().toString();

        Product product = pr.findById(id).orElse(null);

        System.out.println(UUID.randomUUID().toString());

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setCount(count);
        item.setSubTotalPrice(product.getProductPrice()*count);

        opsForHash.put("abc123",id,item);


        List<Object> abc123 = redisTemplate.opsForHash().values("abc123");
        for (Object pr:abc123) {
            System.out.println(pr);
        }
    }
}
