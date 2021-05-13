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

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, CartItem> hashOperations;

    private final ProductRepository pr;

    public CartService(ProductRepository productRepository) {
        this.pr = productRepository;
    }

    /**
     * 購物車購物車key商品總數和總金額
     * @param redisKey
     * @return
     */
    public Map<String, Integer> getNumsAndPrice(String redisKey) {

        int totalNums = 0;
        int totalPrice = 0;
        List<CartItem> cartItemList = hashOperations.values(redisKey);
        for (CartItem cartItem : cartItemList) {
            totalNums += cartItem.getCount();
            totalPrice += cartItem.getSubTotalPrice();
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("totalNums", totalNums);
        map.put("totalPrice", totalPrice);
        return map;
    }

    /**
     * 購物車內商品
     * @param redisKey
     * @return
     */
    public List<CartItem> getCart(String redisKey) {

        return hashOperations.values(redisKey);
    }

    /**
     * 將UUID購物車併入帳號購物車
     * @param usersAccount 帳號
     * @param UUID 未登入下儲存購物車的redis key
     */
    public void mergeCart(String usersAccount, String UUID) {

        List<CartItem> cartItemList = hashOperations.values(UUID);
        for (CartItem cartItem : cartItemList) {
            int id = cartItem.getProduct().getProductId();

            if (hashOperations.hasKey(usersAccount, String.valueOf(id))) {
                int count = cartItem.getCount() + hashOperations.get(usersAccount, String.valueOf(id)).getCount();
                int price = cartItem.getProduct().getProductPrice();
                cartItem.setCount(count);
                cartItem.setSubTotalPrice(price * count);
            }
            hashOperations.put(usersAccount, String.valueOf(id), cartItem);
        }
        redisTemplate.delete(UUID);
    }

    /**
     * 增加商品到購物車ForUsersAccount
     * @param redisKey
     * @param id 商品id
     * @param nums 增加商品數量
     */
    public void addToCartForUsersAccount(String redisKey, Integer id, Integer nums) {

        CartItem item;
        if (hashOperations.hasKey(redisKey, String.valueOf(id))) {
            item = hashOperations.get(redisKey, String.valueOf(id));
            item.setCount(item.getCount() + nums);
        } else {
            Product product = pr.findById(id).orElse(null);
            item = new CartItem();
            item.setProduct(product);
            item.setCount(nums);
        }

        item.setSubTotalPrice(item.getProduct().getProductPrice() * item.getCount());

        hashOperations.put(redisKey, String.valueOf(id), item);
    }

    /**
     * 增加商品到購物車ForUUID,儲存時間5分鐘
     * @param redisKey
     * @param id 商品id
     * @param nums 增加商品數量
     */
    public void addToCartForUUID(String redisKey, Integer id, Integer nums) {

        CartItem item;
        if (hashOperations.hasKey(redisKey, String.valueOf(id))) {
            item = hashOperations.get(redisKey, String.valueOf(id));
            item.setCount(item.getCount() + nums);
        } else {
            Product product = pr.findById(id).orElse(null);
            item = new CartItem();
            item.setProduct(product);
            item.setCount(nums);
        }

        item.setSubTotalPrice(item.getProduct().getProductPrice() * item.getCount());

        hashOperations.put(redisKey, String.valueOf(id), item);

        //未登入設定儲存時間
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
    }

    /**
     * 更新購物車商品數量
     * @param redisKey
     * @param id 商品id
     * @param nums 更新商品數量
     */
    public void updateNumsToCart(String redisKey, Integer id, Integer nums) {

        CartItem item = hashOperations.get(redisKey, String.valueOf(id));

        item.setCount(nums);

        item.setSubTotalPrice(item.getProduct().getProductPrice() * nums);

        hashOperations.put(redisKey, String.valueOf(id), item);
    }

    /**
     * 刪除購物車商品
     * @param redisKey
     * @param id 刪除商品的id
     */
    public void deleteProductToCart(String redisKey, Integer id) {

        hashOperations.delete(redisKey, String.valueOf(id));
    }

    /**
     * 刪除購物車多個商品
     * @param redisKey
     * @param list 刪除商品的id集合
     */
    public void deleteProducts(String redisKey,List<Integer> list){

        for (Integer id: list) {
            hashOperations.delete(redisKey, String.valueOf(id));
        }
    }
}
