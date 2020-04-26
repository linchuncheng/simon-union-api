package me.jensvn.service;

import me.jensvn.common.Consumer;
import me.jensvn.common.WxXmlUtil;
import me.jensvn.constant.WxConstant;
import me.jensvn.enums.WxEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WxEventMessageHandler implements Consumer<Map<String, String>>, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        Consumer.register("wx.message", "event", this);
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
