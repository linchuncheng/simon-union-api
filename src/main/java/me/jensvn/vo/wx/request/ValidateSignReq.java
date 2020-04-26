package me.jensvn.vo.wx.request;

import lombok.Data;

@Data
public class ValidateSignReq {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
