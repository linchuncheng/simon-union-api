package pet.sankei.union.service;

import com.taobao.api.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pet.sankei.union.common.TbkHelper;
import pet.sankei.union.common.WxXmlUtil;
import pet.sankei.union.constant.WxConstant;
import pet.sankei.union.enums.WxEnum;
import pet.sankei.union.vo.goods.response.GoodsResponse;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class WxTextHandler {
    private TbkHelper tbkHelper;

    // 商品导购
    public void goodsGuide(String content, HttpServletResponse response, Map<String, String> map) throws Exception {
        Map<String, String> result = WxXmlUtil.of(map);
        result.put(WxConstant.MSG_TYPE, WxEnum.MsgType_TEXT);
        result.put(WxConstant.CONTENT, this.findGoodsByTitle(content));
        WxXmlUtil.sendMessage(response.getOutputStream(), result);
    }


    // 转换淘口令
    public void convertTpwd(String content, HttpServletResponse response, Map<String, String> map) throws Exception {

        // 口令转换链接

        // 链接转换新口令
//        tbkHelper.createTpwd(content);

        // 封装返回口令
        Map<String, String> result = WxXmlUtil.of(map);
        result.put(WxConstant.MSG_TYPE, WxEnum.MsgType_TEXT);
        result.put(WxConstant.CONTENT, this.findGoodsByTitle(content));
        WxXmlUtil.sendMessage(response.getOutputStream(), result);
    }

    // 标题找商品
    private String findGoodsByTitle(String title) {
        List<GoodsResponse> goodList;
        try {
            goodList = tbkHelper.findGoodsByTitle(title);
        } catch (ApiException e) {
            e.printStackTrace();
            return "商品不存在";
        }
        if (goodList.isEmpty()) {
            return "[可怜]店家太小气了，都没有设置任何优惠！换一个商品试试！";
        } else {
            StringBuilder sb = new StringBuilder();
            for (GoodsResponse goodsResponse : goodList) {
                String goodInfo;
                if (null == goodsResponse.getCouponAmount()) {
                    goodInfo = String.format(WxConstant.NORMAL_FORMAT, goodsResponse.getTitle(),
                            goodsResponse.getZkFinalPrice(), goodsResponse.getUrl());
                } else {
                    BigDecimal result = new BigDecimal(goodsResponse.getZkFinalPrice())
                            .subtract(new BigDecimal(goodsResponse.getCouponAmount()));

                    goodInfo = String.format(WxConstant.COUPON_FORMAT, goodsResponse.getTitle(), goodsResponse.getZkFinalPrice(),
                            result, goodsResponse.getCouponShareUrl(), goodsResponse.getUrl());
                }
                goodInfo += WxConstant.LINE;
                sb.append(goodInfo);
            }
            return sb.toString();
        }
    }

}
