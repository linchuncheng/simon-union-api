package pet.sankei.union.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Jensen Lam
 * @date 2020-03-17 16:08
 **/
@Data
@Component
public class PubConfig {

    /**
     * 微信
     */
    @Value("${wx.token}")
    private String token;
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;
    @Value("${wx.encodingAESKey}")
    private String encodingAESKey;
    @Value("${wx.accessTokenUrl}")
    private String accessTokenUrl;
    @Value("${wx.createMenuUrl}")
    private String createMenuUrl;

    /**
     * ---------淘宝联盟--------
     */
    @Value("${taobao.server}")
    private String taobaoServer;
    @Value("${taobao.mama.key}")
    private String mamaKey;
    @Value("${taobao.mama.secret}")
    private String mamaSecret;
    @Value("${taobao.mama.adzoneId}")
    private Long adzoneId;

    /**
     * -------京东联盟----------
     */
    @Value("${jd.server}")
    private String jdServer;
    @Value("${jd.union.key}")
    private String jdKey;
    @Value("${jd.union.secret}")
    private String jdSecret;
    @Value("${jd.union.siteId}")
    private String jdSiteId;

}
