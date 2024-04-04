package org.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 추가1
public class ResponseUser {
    private String email;
    private String name;
    private String userId;

    private List<ResponseOrder> orders; // 추가2
}
