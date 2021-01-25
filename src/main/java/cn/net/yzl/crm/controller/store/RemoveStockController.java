package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.RemoveStockFeignService;
import cn.net.yzl.model.dto.RemoveStockDto;
import cn.net.yzl.model.vo.RemoveStockRaramsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/25 21:36
 */
@RestController
@Slf4j
@Api(value = "仓储中心心心心心-出库制单", tags = {"仓储中心心心心心-出库制单"})
@RequestMapping("removestock")
public class RemoveStockController {


    @Autowired
    private RemoveStockFeignService removeStockFeignService;

    @ApiOperation(value = "分页查询出库制表单表", notes = "分页查询出库制表单表")
    @PostMapping("v1/selectRemoveStoreListPage")
    public ComResponse<Page<RemoveStockDto>> selectRemoveStoreListPage(@RequestBody RemoveStockRaramsVo removeStockRaramsVo){
        return removeStockFeignService.selectRemoveStoreListPage(removeStockRaramsVo);
    }

}
