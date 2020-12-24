package cn.net.yzl.crm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.dto.order.ListParamsDTO;
import cn.net.yzl.crm.model.OrderMember;
import cn.net.yzl.crm.service.IOrderMemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "订单管理")
@Slf4j
@RestController
@RequestMapping(value = OrderMemberController.PATH)
public class OrderMemberController {
    public static final String PATH = "ordermember";

    @Autowired
    private IOrderMemberService service;

    @ApiOperation(value="分页查询订单列表")
    @PostMapping("listPage")
    public GeneralResult<Page<OrderMember>> listPage(@RequestBody ListParamsDTO dto) {
        Map<String, Object> params = new HashMap<>();
        BeanUtil.copyProperties(dto, params);
        Page<OrderMember> resultList = service.selectPage(params);
        return GeneralResult.success(resultList);
    }


    @ApiOperation(value="删除")
    @PostMapping("deleteById")
    public GeneralResult<Boolean> deleteById(@RequestParam("id")
                                             @NotBlank(message="订单id不能为空")
                                             @ApiParam(name="id",value="订单id",required=true)  Integer id) {
        //这个应该是假删除，需要加个是否删除的状态
        return GeneralResult.success(Boolean.TRUE);
    }




}
