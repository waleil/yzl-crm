package cn.net.yzl.crm.controller;

import cn.net.yzl.crm.model.Province;
import cn.net.yzl.crm.service.ProvinceService;
import cn.net.yzl.crm.service.micservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 19:00
 * @description:
 */
@RestController
@RequestMapping("test")
public class HelloController {
    @Autowired
    UserService userService;

    @Autowired
    ProvinceService provinceService;

    @GetMapping("getUser")
    public String getUser() {
        return userService.getUserName();
    }

    @GetMapping("saveProvince")
    public String saveProvince() {
        Province province = new Province();
        province.setAbbr("京");
        province.setAname("aname");
        province.setCode(1);
        province.setCountry_id(1);
        province.setName("name");
        province.setZname("zname");
        province.setPname("pname");
        provinceService.saveProvince(province);
        return "ok";
    }

    @GetMapping("getProvince")
    public String getProvince() {
        Province province = provinceService.getProvince();
        return province == null ? "null" : "ok";
    }
}
