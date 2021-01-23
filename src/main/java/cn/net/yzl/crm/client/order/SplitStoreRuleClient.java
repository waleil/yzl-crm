package cn.net.yzl.crm.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.SplitStoreProvinceDTO;
import cn.net.yzl.order.model.vo.order.SplitStoreProvinceNamesDTO;
import cn.net.yzl.order.model.vo.order.SplitStoreRuleAddDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouchangsong
 */
@FeignClient(name = "splitStoreRuleClient", url = "${api.gateway.url}/orderService/splitStoreRule")
public interface SplitStoreRuleClient {

    /**
     * 添加分仓规则
     *
     * @param list
     * @return
     */
    @PostMapping("v1/addRule")
    ComResponse<Boolean> addRule(@Valid @RequestBody List<SplitStoreRuleAddDTO> list, @RequestParam String userNo);

    /**
     * 变更分仓规则状态
     *
     * @param storeNo
     * @return
     */
    @PutMapping("v1/updateRuleStatus")
    ComResponse<Boolean> updateRuleStatus(@RequestParam String userNo, @RequestParam String storeNo, @RequestParam Integer storeStatus);

    /**
     * 查询仓库覆盖省市
     *
     * @param storeNo
     * @return
     */
    @GetMapping("v1/getStoreProvinceList")
    ComResponse<List<SplitStoreProvinceDTO>> getStoreProvinceList(@RequestParam String storeNo);


    /**
     *
     * 根据多个仓库编号查询
     * @param storeNoList
     * @return
     */
    @GetMapping("v1/getStoreProvinceByStoreNoList")
    ComResponse<List<SplitStoreProvinceNamesDTO>> getStoreProvinceByStoreNoList(@RequestParam List<String> storeNoList);
}
