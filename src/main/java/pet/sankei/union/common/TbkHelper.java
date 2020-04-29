package pet.sankei.union.common;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import lombok.extern.slf4j.Slf4j;
import pet.sankei.union.config.PubConfig;
import pet.sankei.union.constant.TbkConstant;
import pet.sankei.union.vo.goods.response.GoodsResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jensen Lam
 * @date 2020-03-17 20:48
 **/
@Slf4j
@Component
public class TbkHelper {
    @Autowired
    private PubConfig pubConfig;

    private TaobaoClient taobaoClient;

    @Bean
    public void init() {
        this.taobaoClient = new DefaultTaobaoClient(pubConfig.getTaobaoServer(), pubConfig.getUnionKey(), pubConfig.getUnionSecret());
    }

    /**
     * 获得查询的商品列表
     *
     * @param goodTitle
     * @return
     */
    public List<GoodsResponse> getGoodList(String goodTitle) throws ApiException {
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setPageSize(TbkConstant.pageSize);
        req.setPageNo(TbkConstant.pageNo);
        req.setIsOverseas(TbkConstant.isOverseas);
        req.setIsTmall(TbkConstant.isTmall);
        req.setSort(TbkConstant.sort);
        req.setQ(goodTitle);
        req.setHasCoupon(TbkConstant.hasCoupon);
        req.setAdzoneId(pubConfig.getAdzoneId());
        req.setNeedFreeShipment(TbkConstant.needFreeShipment);
        req.setNeedPrepay(TbkConstant.needPrepay);

        TbkDgMaterialOptionalResponse response = taobaoClient.execute(req);

        List<TbkDgMaterialOptionalResponse.MapData> resultList = response.getResultList();
        if (null == resultList || resultList.isEmpty()) {
            return Collections.emptyList();
        }

        log.info("原始数据 = {}", JSONObject.toJSONString(resultList));
        List<GoodsResponse> goodsResponseList = resultList.stream().map(mapData -> {
            GoodsResponse goodsResponse = new GoodsResponse();
            BeanUtils.copyProperties(mapData, goodsResponse);
            goodsResponse.setUrl("mapData.getUrl()");
            goodsResponse.setCouponShareUrl("mapData.getCouponShareUrl()");
//            goodsResponse.setUrl(mapData.getUrl());
//            goodsResponse.setCouponShareUrl(mapData.getCouponShareUrl());
//            goodsResponse.setUrl(genShortUrl(mapData.getUrl()));
//            goodsResponse.setCouponShareUrl(genShortUrl(mapData.getCouponShareUrl()));
            return goodsResponse;
        }).collect(Collectors.toList());
        log.info("查询的商品列表 = {}", JSONObject.toJSONString(goodsResponseList));
        return goodsResponseList;
    }


//    public String genShortUrl(String longUrl) {
//        longUrl = "http:" + longUrl;
//        TbkSpreadGetRequest.TbkSpreadRequest tbkSpreadRequest = new TbkSpreadGetRequest.TbkSpreadRequest();
//        tbkSpreadRequest.setUrl(longUrl);
//
//        TbkSpreadGetRequest req = new TbkSpreadGetRequest();
//        req.setRequests(Arrays.asList(tbkSpreadRequest));
//
//        TbkSpreadGetResponse response = null;
//        try {
//            response = taobaoClient.execute(req);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//
//        if (!StringUtils.isEmpty(response.getSubCode())) {
//            return null;
//        }
//        String content = response.getResults().get(0).getContent();
//        log.info("生成的短链接 = {}", content);
//        return content;
//    }

}
