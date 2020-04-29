package pet.sankei.union.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.sankei.union.common.R;
import pet.sankei.union.service.WxService;
import pet.sankei.union.vo.wx.request.ValidateSignReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jensen Lam
 * @date 2020-03-03 10:38
 **/
@RestController
@RequestMapping("/base")
@Slf4j
@AllArgsConstructor
public class WxBaseController {
    private WxService wxService;

    /**
     * 获得唯一接口凭证
     * @return
     */
    @GetMapping("/getAccessToken")
    public R<String> getAccessToken() {
        return wxService.getAccessToken();
    }

}
