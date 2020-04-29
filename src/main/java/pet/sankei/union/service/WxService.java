package pet.sankei.union.service;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pet.sankei.union.common.R;
import pet.sankei.union.common.SignHelper;
import pet.sankei.union.common.WxXmlUtil;
import pet.sankei.union.config.PubConfig;
import pet.sankei.union.constant.WxConstant;
import pet.sankei.union.enums.WxEnum;
import pet.sankei.union.vo.wx.request.ValidateSignReq;

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
    @Autowired
    private GoodsService goodsService;

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
        String resultXml;
        switch (msgType) {
            case WxEnum.MsgType_TEXT:
                resultXml = this.acceptText(map);
                break;
            case WxEnum.MsgType_EVENT:
                resultXml = this.acceptEvent(map);
                break;
            default:
                map.put(WxConstant.CONTENT, "该信息类型不支持");
                Map<String, String> resultMap = WxXmlUtil.withBase(map);
                resultMap.put(WxConstant.MSG_TYPE, WxEnum.MsgType_TEXT);
                resultXml = WxXmlUtil.mapToXml(map);
        }
        WxXmlUtil.writeToResponse(response.getOutputStream(), resultXml);
    }

    public String acceptText(Map<String, String> map) throws Exception {
        String content = map.get(WxConstant.CONTENT);
        Map<String, String> resultMap = WxXmlUtil.withBase(map);
        resultMap.put(WxConstant.MSG_TYPE, WxEnum.MsgType_TEXT);
        resultMap.put(WxConstant.CONTENT, goodsService.getGoodsInfo(content));
        return WxXmlUtil.mapToXml(resultMap);
    }

    public String acceptEvent(Map<String, String> map) throws Exception {
        Map<String, String> resultMap = WxXmlUtil.withBase(map);
        resultMap.put(WxConstant.MSG_TYPE, WxEnum.MsgType_TEXT);
        if (WxEnum.Event.subscribe.getMsg().equals(map.get(WxConstant.EVENT))) {
            resultMap.put(WxConstant.CONTENT, WxConstant.WELCOME);
        }
        return WxXmlUtil.mapToXml(resultMap);
    }


}
