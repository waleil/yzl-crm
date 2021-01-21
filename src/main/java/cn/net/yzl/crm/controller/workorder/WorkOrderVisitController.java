package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.client.workorder.WorkOrderVisitClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.product.model.vo.product.dto.ProductListDTO;
import cn.net.yzl.product.model.vo.product.vo.ProductSelectVO;
import cn.net.yzl.workorder.model.db.WorkOrderVisitBean;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitCriteriaTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @Autowired
    private ProductClient productClient;

    @ApiOperation(value = "商品下拉列表（首单、最后一次购买商品）")
    @GetMapping("v1/listProduct")
    public ComResponse<Page<ProductListDTO>> listProduct(ProductSelectVO productSelectVO){
        return productClient.queryListProduct(productSelectVO);
    }

    /**
     *  分页查询回访工单
     * @param criteriaTO
     * @return
     */
    @PostMapping("v1/list")
    @ApiOperation(value = "分页查询回访工单", notes = "分页查询回访工单")
    public ComResponse<Page<WorkOrderVisitBean>> listByCriteriaTO(@RequestBody WorkOrderVisitCriteriaTO criteriaTO){
        ComResponse<Page<WorkOrderVisitBean>> result = workOrderVisitClient.listPageByCriteria(criteriaTO);
        return result;
    }

    /**
     *  根据Code查询回访工单
     * @param code
     * @return
     */
    @GetMapping("v1/getByCode")
    @ApiOperation(value = "根据CODE查询回访工单", notes = "根据CODE查询回访工单")
    public ComResponse<WorkOrderVisitBean> getByCode(@RequestParam("code") @ApiParam(value = "回访工单标识",name = "code") Integer code){
        ComResponse<WorkOrderVisitBean> result = workOrderVisitClient.getByCode(code);
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

    @PostMapping("v1/isListPage")
    @ApiOperation(value = "查询我的回访工单列表",notes = "查询我的回访工单列表")
    public ComResponse<Page<WorkOrderVisitBean>> isListPage(@RequestBody IsListPageDTO isListPage){
        isListPage.setStaffNO(QueryIds.userNo.get());
        return workOrderVisitClient.isListPage(isListPage);
    }
}
