package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OrderSettingClient;
import cn.net.yzl.crm.service.product.ProductService;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingDTO;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingProduct;
import cn.net.yzl.order.model.vo.order.UpdateOrderCheckSettingDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("orderCheckSetting")
@Api(tags = "订单管理")
public class OrderCheckSettingController {

    @Autowired
    private OrderSettingClient orderFein;

    @Autowired
    private ProductService productService;


    @ApiOperation(value = "查询全部免审配置")
    @GetMapping("v1/findAllNonCheckSettings")
    public ComResponse<List<OrderCheckSettingDTO>> findAllNonCheckSettings() {

        ComResponse<List<OrderCheckSettingDTO>> result = orderFein.findAllNonCheckSettings(null);
        return result;
    }

    @ApiOperation(value = "删除规则关联商品")
    @PostMapping("v1/deleteNonCheckSettings")
    public ComResponse<Boolean> deleteNonCheckSettings(@RequestBody @Valid UpdateOrderCheckSettingDTO dto, HttpServletRequest request) {
        ComResponse<Boolean> result = orderFein.deleteNonCheckSettings(dto);
        return result;
    }

    @ApiOperation(value = "新增规则关联商品")
    @PostMapping("v1/createNonCheckSettings")
    public ComResponse<Boolean> createNonCheckSettings(@RequestBody @Valid UpdateOrderCheckSettingDTO dto, HttpServletRequest request) {
        ComResponse<Boolean> result = orderFein.createNonCheckSettings(dto);
        return result;
    }

    @ApiOperation(value = "更新规则")
    @PostMapping("v1/updateNonCheckSettings")
    public ComResponse<Boolean> updateNonCheckSettings(@RequestBody @Valid OrderCheckSettingDTO dto, HttpServletRequest request) {

        ComResponse<Boolean> result = orderFein.updateNonCheckSettings(dto);
        return result;
    }

    @ApiOperation(value = "查询免审规则已选商品")
    @GetMapping("v1/selectSettingedProducts")
    public ComResponse<Page<OrderCheckSettingProduct>> selectSettingedProducts(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                                               @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                                                                               @NotBlank(message = "免审类型不能为空")
                                                                               @ApiParam(name = "settingType", value = "免审规则类型", required = true)
                                                                               @RequestParam Integer settingType) {

        ComResponse<Page<OrderCheckSettingProduct>> result = orderFein.selectSettingedProducts(pageNo, pageSize, settingType);
        return result;
    }
    @ApiOperation(value = "查询所有商品")
    @GetMapping("v1/selectAllProducts")
    public ComResponse<Page<ProductListDTO>> selectAllProducts(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                               @RequestParam(required = false, defaultValue = "15") Integer pageSize,
                                                               @RequestParam(required = false) Integer Status,
                                                               @RequestParam(required = false)  String prdoductNos) {
        ProductSelectVO productSelectVO = new ProductSelectVO();
        productSelectVO.setKeyword(prdoductNos);
        productSelectVO.setPageNo(pageNo);
        productSelectVO.setPageSize(pageSize);
        productSelectVO.setStatus(Status);
        return  productService.queryListProduct(productSelectVO);
    }
}
