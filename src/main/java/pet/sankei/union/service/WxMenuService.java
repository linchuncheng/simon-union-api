package pet.sankei.union.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pet.sankei.union.common.R;
import pet.sankei.union.config.PubConfig;
import pet.sankei.union.enums.WxEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class WxMenuService {
    private PubConfig pubConfig;
    private RestTemplate restTemplate;
    private WxService wxService;

    public R createMenu(JSONObject request) {
        R<String> accessTokenR = wxService.getAccessToken();
        if (!accessTokenR.getSuccess()) {
            return accessTokenR;
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("ACCESS_TOKEN", accessTokenR.getData());
        String resultStr = restTemplate.postForEntity(pubConfig.getCreateMenuUrl(), request, String.class, map).getBody();
        JSONObject resultJson = JSON.parseObject(resultStr);
        int errCode = resultJson.getIntValue(WxEnum.ResultCode.errCode.getMsg());
        if (errCode != 0) {
            log.warn("创建自定义菜单失败,{}", resultJson);
            return R.fail("创建自定义菜单失败");
        }

        log.info("创建自定义菜单成功，{}", resultJson);
        return R.success("创建自定义菜单成功", resultJson);
    }
}
