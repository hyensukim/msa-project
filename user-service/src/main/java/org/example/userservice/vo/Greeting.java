package org.example.userservice.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Greeting {

//    @Value("${greeting.message}")
    public static String message;

    @Value("${greeting.message}")
    public void setMessage(String value){
        message = value;
    }
}
