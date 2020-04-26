package me.jensvn.vo.request.wx;

import lombok.Data;

@Data
public class ValidateSignReq {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
