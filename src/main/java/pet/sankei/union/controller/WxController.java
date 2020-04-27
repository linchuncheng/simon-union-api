package pet.sankei.union.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pet.sankei.union.common.R;
import pet.sankei.union.service.WxService;
import pet.sankei.union.vo.wx.request.ValidateSignReq;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jensen Lam
 * @date 2020-03-03 10:38
 **/
@RestController
@RequestMapping("/wx")
@Slf4j
@AllArgsConstructor
public class WxController {
    private WxService wxService;

    /**
     * 获得唯一接口凭证
     * @return
     */
    @GetMapping("/getAccessToken")
    public R<String> getAccessToken() {
        return wxService.getAccessToken();
    }

    @GetMapping("/tokenSign")
    public String validateSign(ValidateSignReq req) {
        return wxService.validateSign(req);
    }

    @PostMapping("/tokenSign")
    public void receiveMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        wxService.receiveMessage(request, response);
    }

}
