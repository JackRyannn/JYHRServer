package com.yhdyy.jyhrserver.bos;

import lombok.Data;

@Data
public class ResponseBo {
    private String jsonrpc;
    private String id;
    private boolean result;
}