package com.cy.project.listener;

import com.cy.project.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class RedisExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private OrdersService os;

    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.onMessage(message, pattern);
        System.out.println(message);
        if ("orders".equals(message.toString().substring(0,6))){
            os.checkOrdersExpiration(Integer.valueOf(message.toString().substring(7)));
        }
    }

}
