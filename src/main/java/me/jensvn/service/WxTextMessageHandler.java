package me.jensvn.service;

import me.jensvn.common.Consumer;
import me.jensvn.common.WxXmlUtil;
import me.jensvn.constant.WxConstant;
import me.jensvn.enums.WxEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WxTextMessageHandler implements Consumer<Map<String, String>>, InitializingBean {
    @Autowired
    private GoodsService goodsService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Consumer.register("wx.message", "text", this);
    }

    @Override
    public Object accept(Map<String, String> map) throws Exception {
        String content = map.get(WxConstant.CONTENT);
        Map<String, String> resultMap = WxXmlUtil.withBase(map);
        resultMap.put(WxConstant.MSG_TYPE, WxEnum.MsgType.TEXT.getMsg());
        resultMap.put(WxConstant.CONTENT, goodsService.getGoodsInfo(content));
        return WxXmlUtil.mapToXml(resultMap);
    }

}
