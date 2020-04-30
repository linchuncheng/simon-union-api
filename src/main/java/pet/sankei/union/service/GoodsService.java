package pet.sankei.union.service;

import com.taobao.api.ApiException;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkTpwdCreateResponse;
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

}
