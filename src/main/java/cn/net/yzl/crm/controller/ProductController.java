package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

@Api(value = "商品controller",tags = {"商品访问接口"})
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "新增产品和图片的关系")
    @PostMapping("insertRelationOfProductAndImgUrl")
    public ComResponse<Void> insertRelationOfProductAndImgUrl(@NotNull(message = "数据不能为空！") @RequestParam(value = "id") @ApiParam("当type=0时此处为产品编码，type=1时此处为套餐id") String id, @NotNull(message = "数据不能为空！") @RequestParam(value = "imgId") Integer imgId, @NotNull(message = "数据不能为空！") @RequestParam(value = "type") @ApiParam("0为产品，1为套餐") Integer type) {
        if(type==0||type==1){
            productService.insertRelationOfProductAndImgUrl(id, imgId, type);
            return ComResponse.success();
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }

    }

    @ApiOperation(value = "向逻辑图片库插入一条图片")
    @PostMapping("insertImage")
    public ComResponse<Integer> insertImage(@NotNull(message = "数据不能为空！") @RequestParam("url") String url,@NotNull(message = "数据不能为空！") @RequestParam("type") @ApiParam("1为图片，0为视频") Integer type) {
        if(type==0||type==1){
            return productService.insertImage(url,type);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

    @ApiOperation(value = "删除产品和图片的关系")
    @GetMapping("deleteRelationOfProductAndImgId")
    public ComResponse<Void> deleteRelationOfProductAndImgId(@NotNull(message = "数据不能为空！") @RequestParam("id") @NotNull(message = "id不能为空！") Integer id,@NotNull(message = "数据不能为空！") @RequestParam("type") Integer type) {
        if (type == 1||type == 0) {
            return productService.deleteRelationOfProductAndImgId(id, type);
        }else {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.PARAMS_ERROR_CODE.getMessage());
        }
    }

}
