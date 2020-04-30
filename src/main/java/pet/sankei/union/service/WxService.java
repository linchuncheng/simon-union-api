package pet.sankei.union.service;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pet.sankei.union.common.R;
import pet.sankei.union.common.SignHelper;
import pet.sankei.union.common.WxXmlUtil;
import pet.sankei.union.config.CopyWritingConfig;
import pet.sankei.union.config.PubConfig;
import pet.sankei.union.constant.WxConstant;
import pet.sankei.union.enums.WxEnum;
import pet.sankei.union.vo.wx.request.ValidateSignReq;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class WxService {
    private RestTemplate restTemplate;
    private PubConfig pubConfig;
    private SignHelper signHelper;
    private CopyWritingConfig copyWritingConfig;
    private WxTextHandler wxTextHandler;

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
        Map<String, String> map = WxXmlUtil.toMap(inputStream);
        String msgType = map.get(WxConstant.MSG_TYPE);
        switch (msgType) {
            case WxEnum.MsgType_TEXT:
                this.handleText(map, response);
                break;
            case WxEnum.MsgType_EVENT:
                this.handleEvent(map, response);
                break;
            default:
                Map<String, String> result = WxXmlUtil.of(map);
                result.put(WxConstant.MSG_TYPE, WxEnum.MsgType_TEXT);
                result.put(WxConstant.CONTENT, "暂不支持该消息类型");
                WxXmlUtil.sendMessage(response.getOutputStream(), result);
        }
    }

    // 处理文本
    public void handleText(Map<String, String> map, HttpServletResponse response) throws Exception {
        String content = map.get(WxConstant.CONTENT);
        // 解析口令、链接
        if (this.checkTpwd(content)) {
            wxTextHandler.convertTpwd(content, response, map);
        }
        // 导购
        List<String> guideWords = Stream.of(copyWritingConfig.getGuideTriggers().split("\\|")).collect(Collectors.toList());
        boolean triggerGuide = false;
        for (String guideWord : guideWords) {
            if (content.startsWith(guideWord)) {
                triggerGuide = true;
                content = content.replaceFirst(guideWord, "");
                break;
            }
        }
        if (triggerGuide) {
            wxTextHandler.goodsGuide(content, response, map);
            return;
        }

    }

    // 处理事件
    public void handleEvent(Map<String, String> map, HttpServletResponse response) throws Exception {
        Map<String, String> result = WxXmlUtil.of(map);
        result.put(WxConstant.MSG_TYPE, WxEnum.MsgType_TEXT);
        // 订阅公众号事件
        if (WxEnum.Event.subscribe.getMsg().equals(map.get(WxConstant.EVENT))) {
            result.put(WxConstant.CONTENT, copyWritingConfig.getWelcome());
        }
        WxXmlUtil.sendMessage(response.getOutputStream(), result);
    }

    private boolean checkTpwd(String content) {
        return true;
    }

}
