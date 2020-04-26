package me.jensvn.service;

import me.jensvn.common.TbkHelper;
import me.jensvn.constant.WxConstant;
import me.jensvn.vo.goods.response.GoodsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private TbkHelper tbkHelper;

    public String getGoodsInfo(String content) {
        List<GoodsResponse> goodList;
        try {
            goodList = tbkHelper.getGoodList(content);
        } catch (ApiException e) {
            e.printStackTrace();
            return "商品不存在";
        }
        if (goodList.isEmpty()) {
            return "商品不存在";
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
