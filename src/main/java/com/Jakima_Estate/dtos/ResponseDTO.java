package com.Jakima_Estate.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private boolean success;
    private String message;
    private Object data;

    public static ResponseDTO success(String message, Object data) {
        return new ResponseDTO(true, message, data);
    }

    public static ResponseDTO error(String message) {
        return new ResponseDTO(false, message, null);
    }
}
