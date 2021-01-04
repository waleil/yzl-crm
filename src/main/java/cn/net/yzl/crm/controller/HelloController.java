package cn.net.yzl.crm.controller;

import cn.net.yzl.crm.model.Province;
import cn.net.yzl.crm.service.ProvinceService;
import cn.net.yzl.crm.service.micservice.UserService;
import cn.net.yzl.crm.utils.FastdfsUtils;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static cn.net.yzl.common.util.HttpUtil.sendGetRequest;

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

    @Autowired
    FastdfsUtils fastdfsUtils;

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

    @PostMapping("uploadfile")
    public String uploadfile(@RequestParam("file") MultipartFile file) throws IOException {
        StorePath upload = fastdfsUtils.upload(file);
        return upload.getPath();

    }

    @GetMapping("testUrl")
    public String testUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("userNo", "12");
        map.put("gateway", "true");

        Map<String, String> p = new HashMap<>();
        p.put("id", "1");


        System.out.println("123");

        String result = sendGetRequest("http://192.168.32.94:8080/product/selectById", p, map);
        return result;
    }




//    @GetMapping("downfile")
//    public String downfile(String path) throws IOException {
//        InputStream download = fastdfsUtils.download(path, "");
//
//
//
//    }
}
