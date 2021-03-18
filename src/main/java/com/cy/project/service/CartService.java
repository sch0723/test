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

    public CartService(ProductRepository productRepository) {
        this.pr = productRepository;
    }

    private HashOperations<String, String, CartItem> getOpsForHash(){
        return redisTemplate.opsForHash();
    }

    //購物車總數和總金額
    public Map<String, Integer> getNumsAndPrice(String redisKey) {

        int totalNums = 0;
        int totalPrice = 0;
        List<CartItem> cartItemList = getOpsForHash().values(redisKey);
        for (CartItem cartItem : cartItemList) {
            totalNums += cartItem.getCount();
            totalPrice += cartItem.getSubTotalPrice();
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("totalNums", totalNums);
        map.put("totalPrice", totalPrice);
        return map;
    }

    //購物車清單
    public List<CartItem> getCart(String redisKey) {

        return getOpsForHash().values(redisKey);
    }

    //合併UUID和帳號購物車
    public void mergeCart(String usersAccount, String UUID) {

        List<CartItem> cartItemList = getOpsForHash().values(UUID);
        for (CartItem cartItem : cartItemList) {
            int id = cartItem.getProduct().getProductId();

            if (getOpsForHash().hasKey(usersAccount, String.valueOf(id))) {
                int count = cartItem.getCount() + getOpsForHash().get(usersAccount, String.valueOf(id)).getCount();
                int price = cartItem.getProduct().getProductPrice();

                cartItem.setCount(count);
                cartItem.setSubTotalPrice(price * count);
            }
            getOpsForHash().put(usersAccount, String.valueOf(id), cartItem);
        }
        redisTemplate.delete(UUID);
    }

    //增加商品到購物車ForUsersAccount
    public Map<String, Integer> addToCartForUsersAccount(String redisKey, Integer id, Integer nums) {

        CartItem item;
        if (getOpsForHash().hasKey(redisKey, String.valueOf(id))) {
            item = getOpsForHash().get(redisKey, String.valueOf(id));
            item.setCount(item.getCount() + nums);
        } else {
            Product product = pr.findById(id).orElse(null);
            item = new CartItem();
            item.setProduct(product);
            item.setCount(nums);
        }

        item.setSubTotalPrice(item.getProduct().getProductPrice() * item.getCount());

        getOpsForHash().put(redisKey, String.valueOf(id), item);

        return getNumsAndPrice(redisKey);
    }
    //增加商品到購物車ForUUID
    public Map<String, Integer> addToCartForUUID(String redisKey, Integer id, Integer nums) {

        CartItem item;
        if (getOpsForHash().hasKey(redisKey, String.valueOf(id))) {
            item = getOpsForHash().get(redisKey, String.valueOf(id));
            item.setCount(item.getCount() + nums);
        } else {
            Product product = pr.findById(id).orElse(null);
            item = new CartItem();
            item.setProduct(product);
            item.setCount(nums);
        }

        item.setSubTotalPrice(item.getProduct().getProductPrice() * item.getCount());

        getOpsForHash().put(redisKey, String.valueOf(id), item);

        //未登入設定儲存時間
        redisTemplate.expire(redisKey, 60, TimeUnit.SECONDS);

        return getNumsAndPrice(redisKey);
    }

    //更新購物車商品數量
    public Map<String, Integer> updateNumsToCart(String redisKey, Integer id, Integer nums) {

        CartItem item = getOpsForHash().get(redisKey, String.valueOf(id));

        item.setCount(nums);

        item.setSubTotalPrice(item.getProduct().getProductPrice() * nums);

        getOpsForHash().put(redisKey, String.valueOf(id), item);

        return getNumsAndPrice(redisKey);
    }

    //刪除購物車商品
    public Map<String, Integer> deleteProductToCart(String redisKey, Integer id) {

        getOpsForHash().delete(redisKey, String.valueOf(id));

        return getNumsAndPrice(redisKey);
    }

    //刪除購物車多個商品
    public void deleteProducts(String redisKey,List<Integer> list){
        for (Integer id: list) {
            getOpsForHash().delete(redisKey, String.valueOf(id));
        }
    }

}
