package pet.sankei.union.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import pet.sankei.union.common.R;
import pet.sankei.union.service.WxMenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jensen Lam
 * @date 2020-03-20 21:06
 **/
@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class MenuController {
    private WxMenuService wxMenuService;

    @PostMapping("/create")
    public R create(@RequestBody JSONObject req) {
        return wxMenuService.createMenu(req);
    }
}
