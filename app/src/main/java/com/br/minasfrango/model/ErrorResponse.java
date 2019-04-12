package com.br.minasfrango.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.ResponseBody;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse  {
    private String title;
    private String detail;
    private int code;
    private String developerMessage;
}
