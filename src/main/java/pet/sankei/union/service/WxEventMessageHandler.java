package pet.sankei.union.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import pet.sankei.union.common.Consumers;
import pet.sankei.union.common.WxXmlUtil;
import pet.sankei.union.constant.WxConstant;
import pet.sankei.union.enums.WxEnum;

import java.util.Map;

@Service
public class WxEventMessageHandler implements Consumers.Consumer<Map<String, String>>, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        Consumers.register("wx.message", "event", this);
    }

    @Override
    public Object accept(Map<String, String> map) throws Exception {
        Map<String, String> resultMap = WxXmlUtil.withBase(map);
        resultMap.put(WxConstant.MSG_TYPE, WxEnum.MsgType.TEXT.getMsg());
        if (WxEnum.Event.subscribe.getMsg().equals(map.get(WxConstant.EVENT))) {
            resultMap.put(WxConstant.CONTENT, WxConstant.WELCOME);
        }
        return WxXmlUtil.mapToXml(resultMap);
    }

}
