package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.workorder.WorkOrderVisitClient;
import cn.net.yzl.workorder.model.db.WorkOrderVisitBean;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitCriteriaTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *  回访工单相关接口
 */
@RestController
@RequestMapping("visit")
@Api(tags = "回访工单相关接口")
public class WorkOrderVisitController {

    @Autowired
    private WorkOrderVisitClient workOrderVisitClient;

    /**
     *  分页查询回访工单
     * @param criteriaTO
     * @return
     */
    @GetMapping("/v1/list")
    @ApiOperation(value = "分页查询回访工单", notes = "分页查询回访工单")
    public ComResponse<Page<WorkOrderVisitBean>> listByCriteriaTO(WorkOrderVisitCriteriaTO criteriaTO){
        ComResponse<Page<WorkOrderVisitBean>> result = workOrderVisitClient.listPageByCriteria(criteriaTO);
        return result;
    }

    /**
     *  根据ID查询回访工单
     * @param _id
     * @return
     */
    @GetMapping("v1/getById")
    @ApiImplicitParam(name = "id", value = "主键信息", required = true, dataType = "String")
    @ApiOperation(value = "根据ID查询回访工单", notes = "根据ID查询回访工单")
    public ComResponse<WorkOrderVisitBean> getById(@RequestParam("_id") String _id){
        ComResponse<WorkOrderVisitBean> result = workOrderVisitClient.getById(_id);
        return result;
    }

    /**
     *  新增一条回访工单
     * @param workOrderVisitBean
     * @return
     */
    @PostMapping("v1/add")
    @ApiOperation(value = "新增回访工单", notes = "新增回访工")
    public ComResponse<Void> add(@RequestBody WorkOrderVisitBean workOrderVisitBean){
        ComResponse<Void> result = workOrderVisitClient.add(workOrderVisitBean);
        return result;
    }
}
