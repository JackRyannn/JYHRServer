package com.yhdyy.jyhrserver.bos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MsgRequestBody implements Serializable {
    public MsgRequestBody() {
    }

    /**
     * 请求id
     */
    private String id;
    /**
     * jsonrpc版本
     */
    private String jsonrpc;
    /**
     * 接口名称
     */
    private String method;

    /**
     * 参数数组
     */
    private List<Param> params;

    @Data
    public static class Param implements Serializable {
        /**
         * 机构编码
         */
        String hospital_id;
        /**
         * RSA加密参数
         */
        String param_info;
    }



}
