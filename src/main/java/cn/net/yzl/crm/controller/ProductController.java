package cn.net.yzl.crm.controller;

import cn.net.yzl.crm.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "商品controller",tags = {"商品访问接口"})
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;


}
