package me.jensvn.service;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jensvn.common.*;
import me.jensvn.config.PubConfig;
import me.jensvn.constant.WxConstant;
import me.jensvn.enums.WxEnum;
import me.jensvn.vo.request.wx.ValidateSignReq;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class WxService {
    private RestTemplate restTemplate;
    private PubConfig pubConfig;
    private SignHelper signHelper;

    public R<String> getAccessToken() {
        Map<String, String> map = new HashMap<>(16);
        map.put(WxConstant.APP_ID, pubConfig.getAppId());
        map.put(WxConstant.APP_SECRET, pubConfig.getAppSecret());

        String body = restTemplate.getForEntity(pubConfig.getAccessTokenUrl(), String.class, map).getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        String accessToken = jsonObject.getString(WxConstant.ACCESS_TOKEN);
        if (null == accessToken) {
            log.warn("获取accessToken失败");
            return R.error("获取accessToken失败");
        }

        log.info("获取accessToken成功，accessToken = {}", accessToken);
        return R.success("获取accessToken成功", accessToken);
    }

    public String validateSign(ValidateSignReq req) {
        log.debug("ValidateSignReq ==> {}", req);
        if (signHelper.validate(req.getSignature(), req.getTimestamp(), req.getNonce(), req.getEchostr())) {
            log.info("校验签名成功，echostr= {}", req.getEchostr());
            return req.getEchostr();
        } else {
            return null;
        }
    }

    public void receiveMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletInputStream inputStream = request.getInputStream();
        Map<String, String> map = WxXmlUtil.streamToMap(inputStream);
        String msgType = map.get(WxConstant.MSG_TYPE);
        String resultXml = Consumer.consume("wx.message", msgType, map, o -> {
            map.put(WxConstant.CONTENT, "该信息类型不支持");
            Map<String, String> resultMap = WxXmlUtil.withBase(map);
            resultMap.put(WxConstant.MSG_TYPE, WxEnum.MsgType.TEXT.getMsg());
            return WxXmlUtil.mapToXml(resultMap);
        });
        WxXmlUtil.writeToResponse(response.getOutputStream(), resultXml);
    }

}
