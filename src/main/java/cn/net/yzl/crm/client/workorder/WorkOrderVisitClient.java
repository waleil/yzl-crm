package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.workorder.model.db.WorkOrderVisitBean;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitCriteriaTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "workOrderVisitClient", url = "${api.gateway.url}/workorderServer/visit")
public interface WorkOrderVisitClient {

    /**
     *  新增一条回访工单
     * @param workOrderVisitBean
     * @return
     */
    @PostMapping("v1/add")
    @ApiOperation(value = "新增回访工单", notes = "新增回访工")
    ComResponse<Void> add(@RequestBody WorkOrderVisitBean workOrderVisitBean);


    /**
     *  根据ID查询回访工单
     * @param _id
     * @return
     */
    @GetMapping("v1/getById")
    @ApiImplicitParam(name = "id", value = "主键信息", required = true, dataType = "String")
    @ApiOperation(value = "根据ID查询回访工单", notes = "根据ID查询回访工单")
    ComResponse<WorkOrderVisitBean> getById(@RequestParam("_id") String _id);

    /**
     *  分页查询回访工单列表
     * @param criteriaTO
     * @return
     */
    @GetMapping("v1/list")
    @ApiOperation(value = "查询回访工单列表", notes = "查询回访工单列表")
    ComResponse<Page<WorkOrderVisitBean>> listPageByCriteria(@SpringQueryMap WorkOrderVisitCriteriaTO criteriaTO);
}
