package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.order.SplitStoreRuleClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.order.SplitStoreRulePageDTO;
import cn.net.yzl.crm.service.SplitStoreRuleService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.SplitStoreProvinceDTO;
import cn.net.yzl.order.model.vo.order.SplitStoreRuleAddDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    ComResponse<Boolean> addRule(@Valid @RequestBody List<SplitStoreRuleAddDTO> list) {
        if (list.size() == 0) {
            throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        }
        String userNo = Optional.ofNullable(QueryIds.userNo.get()).orElse("");
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
    ComResponse<Boolean> updateRuleStatus(
            @ApiParam(value = "仓库状态，0停用，1启用") @RequestParam Integer storeStatus,
            @ApiParam(value = "仓库编号") @RequestParam String storeNo) {
        return splitStoreRuleClient.updateRuleStatus(QueryIds.userNo.get(), storeNo, storeStatus);
    }

    /**
     * 查询仓库覆盖省市
     *
     * @param storeNo
     * @return
     */
    @GetMapping("v1/getStoreProvinceList")
    @ApiOperation(value = "查询仓库覆盖省市")
    ComResponse<List<SplitStoreProvinceDTO>> getStoreProvinceList(@ApiParam(value = "仓库编号") @RequestParam String storeNo) {
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
    ComResponse<Page<SplitStoreRulePageDTO>> getSplitStoreRuleList(@ApiParam(value = "起始页") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                                                   @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return splitStoreRuleService.getSplitStoreRuleList(pageSize, pageNo);
    }
}
