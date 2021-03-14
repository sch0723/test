package com.cy.project.service;

import com.cy.project.entity.CartItem;
import com.cy.project.entity.Product;
import com.cy.project.repository.ProductRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CartService {

    @Resource
    private RedisTemplate<String, CartItem> redisTemplate;

    private final ProductRepository pr;

    public CartService(ProductRepository productRepository){
        this.pr=productRepository;
    }

    //購物車總數和總金額
    public Map<String, Integer> getNumsAndPrice(String Str){

        HashOperations<String,Integer,CartItem> opsForHash=redisTemplate.opsForHash();

        int totalNums=0;
        int totalPrice=0;
        List<CartItem> cartItemList = opsForHash.values(Str);
        for (CartItem cartItem:cartItemList) {
            totalNums+=cartItem.getCount();
            totalPrice+=cartItem.getSubTotalPrice();
        }

        Map<String,Integer> map=new HashMap<>();
        map.put("totalNums",totalNums);
        map.put("totalPrice",totalPrice);
        return map;
    }

    //合併UUID和帳號購物車
    public void mergeCart(String usersAccount, String UUID){

        HashOperations<String,Integer,CartItem> opsForHash=redisTemplate.opsForHash();

        List<CartItem> cartItemList = opsForHash.values(UUID);
        for (CartItem cartItem:cartItemList) {
            int id=cartItem.getProduct().getProductId();

            if(opsForHash.hasKey(usersAccount,id)){
                int count=cartItem.getCount()+opsForHash.get(usersAccount,id).getCount();
                int price=cartItem.getProduct().getProductPrice();

                cartItem.setCount(count);
                cartItem.setSubTotalPrice(price*count);
            }
            opsForHash.put(usersAccount,id,cartItem);
        }
        redisTemplate.delete(UUID);
    }

    //增加商品到購物車
    public Map<String, Integer> addToCart(String redisKEY,Integer id,Integer nums){

        Product product = pr.findById(id).orElse(null);
        HashOperations<String,Integer,CartItem> opsForHash=redisTemplate.opsForHash();
        CartItem item = new CartItem();
        item.setProduct(product);
        if(opsForHash.hasKey(redisKEY,id)){
            item.setCount(opsForHash.get(redisKEY,id).getCount()+nums);
        }else {
            item.setCount(nums);
        }

        item.setSubTotalPrice(product.getProductPrice()*item.getCount());

        opsForHash.put(redisKEY,id,item);

        String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        if (redisKEY.matches(regex)) {
            redisTemplate.expire(redisKEY,60, TimeUnit.SECONDS);
        }
        return getNumsAndPrice(redisKEY);
    }

    //更新購物車商品數量
    public Map<String, Integer> updateNumsToCart(String redisKEY,Integer id,Integer nums){

        Product product = pr.findById(id).orElse(null);
        HashOperations<String,Integer,CartItem> opsForHash=redisTemplate.opsForHash();
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setCount(nums);
        item.setSubTotalPrice(product.getProductPrice()*nums);

        opsForHash.put(redisKEY,id,item);

        return getNumsAndPrice(redisKEY);
    }

    //刪除購物車商品
    public Map<String, Integer> deleteProductToCart(String redisKEY,Integer id){

        Product product = pr.findById(id).orElse(null);
        HashOperations<String,Integer,CartItem> opsForHash=redisTemplate.opsForHash();

        opsForHash.delete(redisKEY,id);

        return getNumsAndPrice(redisKEY);
    }

}
