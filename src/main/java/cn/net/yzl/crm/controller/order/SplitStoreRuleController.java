package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.SplitStoreRuleClient;
import cn.net.yzl.crm.dto.order.SplitStoreRulePageDTO;
import cn.net.yzl.crm.service.SplitStoreRuleService;
import cn.net.yzl.order.model.vo.order.SplitStoreProvinceDTO;
import cn.net.yzl.order.model.vo.order.SplitStoreRuleAddDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouchangsong
 */
@RestController
@RequestMapping("splitStoreRule")
@Api(tags = "分仓规则控制层")
public class SplitStoreRuleController {

    @Autowired
    private SplitStoreRuleClient splitStoreRuleClient;
    @Autowired
    private SplitStoreRuleService splitStoreRuleService;

    /**
     * 添加分仓规则
     *
     * @param list
     * @return
     */
    @PostMapping("v1/addRule")
    @ApiOperation(value = "添加分仓规则", notes = "若选择全部，provinceId=0，provinceName=全部")
    ComResponse<Boolean> addRule(HttpServletRequest request, @Valid @RequestBody List<SplitStoreRuleAddDTO> list) {
        String userNo = request.getHeader("userNo");
        return splitStoreRuleClient.addRule(list, userNo);
    }

    /**
     * 变更分仓规则状态
     *
     * @param storeNo
     * @return
     */
    @PostMapping("v1/updateRuleStatus")
    @ApiOperation(value = "变更分仓规则状态")
    ComResponse<Boolean> updateRuleStatus(HttpServletRequest request,
                                          @ApiParam(value = "仓库状态，0停用，1启用", name = "storeStatus") @RequestParam("storeStatus") Integer storeStatus,
                                          @ApiParam(value = "仓库编号", name = "storeNo") @RequestParam("storeNo") String storeNo) {
        String userNo = request.getHeader("userNo");
        return splitStoreRuleClient.updateRuleStatus(userNo, storeNo, storeStatus);
    }

    /**
     * 查询仓库覆盖省市
     *
     * @param storeNo
     * @return
     */
    @GetMapping("v1/getStoreProvinceList")
    @ApiOperation(value = "查询仓库覆盖省市")
    ComResponse<List<SplitStoreProvinceDTO>> getStoreProvinceList(@ApiParam(value = "仓库编号", name = "storeNo") @RequestParam("storeNo") String storeNo) {
        return splitStoreRuleClient.getStoreProvinceList(storeNo);
    }


    /**
     * 订单分仓规则分页查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("v1/getSplitStoreRuleList")
    @ApiOperation(value = "订单分仓规则分页查询")
    ComResponse<Page<SplitStoreRulePageDTO>> getSplitStoreRuleList(@ApiParam(name = "pageNo", value = "起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                                  @ApiParam(name = "pageSize", value = "每页多少条") @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return splitStoreRuleService.getSplitStoreRuleList(pageSize, pageNo);
    }
}
