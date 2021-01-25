package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.DateFormatUtil;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.crm.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.workorder.common.Constant;
import cn.net.yzl.workorder.model.db.WorkOrderFlowBean;
import cn.net.yzl.workorder.model.dto.*;
import cn.net.yzl.workorder.model.enums.DeptTypeEnums;
import cn.net.yzl.workorder.model.enums.OperationTypeEnums;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.product.model.vo.product.dto.ProductMainInfoDTO;
import cn.net.yzl.workorder.model.db.WorkOrderBean;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("workOrder")
@Api(tags = "智能工单")
public class WorkOrderController {

    @Autowired
    private WorkOrderClient workOrderClient;

    @Autowired
    private ProductClient productClient;

    /**
     * 查询我的回访分页列表
     *
     * @param isListPageDTO
     * @return
     */
    @PostMapping(value = "v1/isListPage")
    @ApiOperation(value = "查询我的回访分页列表", notes = "查询我的回访分页列表")
    public ComResponse<Page<WorkOrderBean>> isListPage(@RequestBody IsListPageDTO isListPageDTO) {
        isListPageDTO.setStaffNO(QueryIds.userNo.get());
        ComResponse<Page<WorkOrderBean>> listPage = workOrderClient.isListPage(isListPageDTO);
        Page<WorkOrderBean> pageWorkOrderBean = listPage.getData();
        if (null == pageWorkOrderBean) {
            return ComResponse.success();
        }
        List<WorkOrderBean> workOrderBeans = pageWorkOrderBean.getItems();
        String productNames = new String();
        for (WorkOrderBean workOrderBean : workOrderBeans) {
            productNames += "," + workOrderBean.getProductCode();
        }
        productNames = productNames.substring(1);
        List<ProductMainInfoDTO> data = productClient.queryProducts(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            Map<String, ProductMainInfoDTO> collect = data.stream().collect(Collectors.toMap(ProductMainInfoDTO::getProductCode, Function.identity()));
            workOrderBeans.stream().forEach(workOrderBean -> {
                if (workOrderBean.getProductCode().equals(collect.get(workOrderBean.getProductCode()).getProductCode())) {
                    workOrderBean.setProductName(collect.get(workOrderBean.getProductCode()).getName());
                }
            });
        }
        pageWorkOrderBean.setItems(workOrderBeans);

        return ComResponse.success(pageWorkOrderBean);
    }

    @PostMapping("v1/pageList")
    @ApiOperation(value = "智能工单: 热线工单管理-列表", notes = "智能工单: 热线工单管理-列表")
    public ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageList(@RequestBody FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO) {
        ComResponse<Page<FindWorkOrderHotlinePageListVO>> pageComResponse = workOrderClient.pageList(findWorkOrderHotlinePageListDTO);
        return pageComResponse;
    }

    /**
     * 智能工单：热线工单管理-回收
     *
     * @param updateRecyclingDTO
     * @return
     */
    @ApiOperation(value = "智能工单：热线工单管理-回收", notes = "智能工单：热线工单管理-回收")
    @PostMapping("v1/updateRecycling")
    public ComResponse<Void> updateRecycling(@Validated @RequestBody UpdateRecyclingDTO updateRecyclingDTO) {
        updateRecyclingDTO.setStaffNo(QueryIds.userNo.get());
        updateRecyclingDTO.setOperator(QueryIds.userName.get());
        updateRecyclingDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        return workOrderClient.updateRecycling(updateRecyclingDTO);
    }

    @ApiOperation(value = "回访工单管理列表", notes = "回访工单管理列表")
    @PostMapping(value = "v1/listPage")
    public ComResponse<Page<WorkOrderBean>> listPage(@Validated @RequestBody WorkOrderVisitVO workOrderVisitVO) {
        ComResponse<Page<WorkOrderBean>> listPage = workOrderClient.listPage(workOrderVisitVO);
        Page<WorkOrderBean> pageWorkOrderBean = listPage.getData();
        if (null == pageWorkOrderBean) {
            return ComResponse.success();
        }
        List<WorkOrderBean> workOrderBeans = pageWorkOrderBean.getItems();
        String productNames = new String();
        for (WorkOrderBean workOrderBean : workOrderBeans) {
            productNames += "," + workOrderBean.getProductCode();
        }
        productNames = productNames.substring(1);
        List<ProductMainInfoDTO> data = productClient.queryProducts(productNames).getData();
        if (!CollectionUtils.isEmpty(data)) {
            Map<String, ProductMainInfoDTO> collect = data.stream().collect(Collectors.toMap(ProductMainInfoDTO::getProductCode, Function.identity()));
            workOrderBeans.stream().forEach(workOrderBean -> {
                if (workOrderBean.getProductCode().equals(collect.get(workOrderBean.getProductCode()).getProductCode())) {
                    workOrderBean.setProductName(collect.get(workOrderBean.getProductCode()).getName());
                }
            });
        }
        pageWorkOrderBean.setItems(workOrderBeans);

        return ComResponse.success(pageWorkOrderBean);
    }

    /**
     * 智能工单：热线工单管理-单数据调整
     *
     * @param
     * @return
     */
    @PostMapping("v1/updateSingleAdjust")
    @ApiOperation(value = "智能工单：热线工单管理-单数据调整", notes = "智能工单：热线工单管理-单数据调整")
    public ComResponse<Void> updateSingleAdjust(@Validated @RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO) {
        updateSingleAdjustDTO.setStaffNo(QueryIds.userNo.get());
        updateSingleAdjustDTO.setOperator(QueryIds.userName.get());
        updateSingleAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateSingleAdjustDTO.setAcceptStatus(2);//人工触发 改为已接受
        return workOrderClient.updateSingleAdjust(updateSingleAdjustDTO);
    }

    @ApiOperation(value = "查询所有用户首次购买商品", notes = "查询所有用户首次购买商品")
    @GetMapping("v1/queryFirstProduct")
    public ComResponse<List<ProductMainInfoDTO>> queryFirstProduct() {
        String data = workOrderClient.queryFirstProduct().getData();
        return productClient.queryProducts(data);
    }

    @ApiOperation(value = "查询所有用户最后一次购买商品", notes = "查询所有用户最后一次购买商品")
    @GetMapping("v1/queryLastProduct")
    public ComResponse<List<ProductMainInfoDTO>> queryLastProduct() {
        String data = workOrderClient.queryLastProduct().getData();
        return productClient.queryProducts(data);
    }

    /**
     * 智能工单：热线工单管理-多数据调整
     * @param
     * @return
     */
    @PostMapping("v1/updateMoreAdjust")
    @ApiOperation(value = "智能工单：热线工单管理-多数据调整", notes = "智能工单：热线工单管理-多数据调整")
    public ComResponse<Void> updateMoreAdjust(@Validated @RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO){
        updateMoreAdjustDTO.setAcceptStatus(2);//人工触发 改为已接受
        updateMoreAdjustDTO.setOperatorType(Constant.OPERATOR_TYPE_ARTIFICIAL);
        updateMoreAdjustDTO.setOperatorCode(QueryIds.userNo.get());
        updateMoreAdjustDTO.setOperator(QueryIds.userName.get());
        return workOrderClient.updateMoreAdjust(updateMoreAdjustDTO);
    }

    @ApiOperation(value = "回访工单单条分配工单", notes = "回访工单单条分配工单")
    @PostMapping(value = "v1/adjustment")
    public ComResponse<Void> adjustment(@RequestBody UpdateWorkOrderVisitDTO updateWorkOrderVisitDTO) {
        updateWorkOrderVisitDTO.setCreateId(QueryIds.userNo.get());
        updateWorkOrderVisitDTO.setCreateName(QueryIds.userName.get());
        return workOrderClient.adjustment(updateWorkOrderVisitDTO);
    }

    @ApiOperation(value = "回访工单多条分配工单", notes = "回访工单多条分配工单")
    @PostMapping(value = "v1/batchAdjustment")
    public ComResponse<Void> batchAdjustment(@RequestBody UpdateBatchDTO updateBatchDTO) {
        updateBatchDTO.setCreateId(QueryIds.userNo.get());
        updateBatchDTO.setCreateName(QueryIds.userName.get());
        List<UpdateBatchWorkOrderDTO> updateBatchWorkOrderDTOS = updateBatchDTO.getUpdateBatchWorkOrderDTOS();
        int count = 0;
        for(UpdateBatchWorkOrderDTO updateBatchWorkOrderDTO : updateBatchWorkOrderDTOS){
            count +=updateBatchWorkOrderDTO.getCount();
        }

        if(count != updateBatchDTO.getCount()){
            return ComResponse.nodata("总线数必须和分配线数相等!");
        }
        return workOrderClient.batchAdjustment(updateBatchDTO);
    }
}
