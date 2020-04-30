package pet.sankei.union.service;

import com.taobao.api.ApiException;
import pet.sankei.union.common.TbkHelper;
import pet.sankei.union.constant.WxConstant;
import pet.sankei.union.vo.goods.response.GoodsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private TbkHelper tbkHelper;

    public String findGoodsByTitle(String title) {
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
