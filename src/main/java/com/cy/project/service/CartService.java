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
     * @param redisKey 帳號或UUID
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
     * @param redisKey 帳號或UUID
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
     * @param productId 商品id
     * @param addNums 增加商品數量
     */
    public void addToCartForUsersAccount(String redisKey, Integer productId, Integer addNums) {

        CartItem item;
        if (hashOperations.hasKey(redisKey, String.valueOf(productId))) {
            item = hashOperations.get(redisKey, String.valueOf(productId));
            item.setCount(item.getCount() + addNums);
        } else {
            Product product = pr.findById(productId).orElse(null);
            item = new CartItem();
            item.setProduct(product);
            item.setCount(addNums);
        }

        item.setSubTotalPrice(item.getProduct().getProductPrice() * item.getCount());

        hashOperations.put(redisKey, String.valueOf(productId), item);
    }

    /**
     * 增加商品到購物車ForUUID,儲存時間5分鐘
     * @param redisKey
     * @param productId 商品id
     * @param addNums 增加商品數量
     */
    public void addToCartForUUID(String redisKey, Integer productId, Integer addNums) {

        CartItem item;
        if (hashOperations.hasKey(redisKey, String.valueOf(productId))) {
            item = hashOperations.get(redisKey, String.valueOf(productId));
            item.setCount(item.getCount() + addNums);
        } else {
            Product product = pr.findById(productId).orElse(null);
            item = new CartItem();
            item.setProduct(product);
            item.setCount(addNums);
        }

        item.setSubTotalPrice(item.getProduct().getProductPrice() * item.getCount());

        hashOperations.put(redisKey, String.valueOf(productId), item);

        //未登入設定儲存時間
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
    }

    /**
     * 更新購物車商品數量
     * @param redisKey
     * @param productId 商品id
     * @param updateNums 更新商品數量
     */
    public void updateNumsToCart(String redisKey, Integer productId, Integer updateNums) {

        CartItem item = hashOperations.get(redisKey, String.valueOf(productId));

        item.setCount(updateNums);

        item.setSubTotalPrice(item.getProduct().getProductPrice() * updateNums);

        hashOperations.put(redisKey, String.valueOf(productId), item);
    }

    /**
     * 刪除購物車商品
     * @param redisKey
     * @param productId 刪除商品的id
     */
    public void deleteProductToCart(String redisKey, Integer productId) {

        hashOperations.delete(redisKey, String.valueOf(productId));
    }

    /**
     * 刪除購物車多個商品
     * @param redisKey
     * @param productIdList 刪除商品的id集合
     */
    public void deleteProducts(String redisKey,List<Integer> productIdList){

        for (Integer productId: productIdList) {
            hashOperations.delete(redisKey, String.valueOf(productId));
        }
    }
}
