package com.cy.project.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;

public class VerifyUsersUtil {

    private static final String SIGN="wrelkjfskedjfm";

    public static String getToken(String usersAccount){
        System.out.println(SIGN);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,2);

        return JWT.create()
                .withClaim("usersAccount", usersAccount)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));
    }

    public static boolean verify(String token){
        try {
            JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String getTokenInfo(String token){
        if (verify(token)){
            return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token).getClaim("usersAccount").asString();
        }
        return null;
    }

}
