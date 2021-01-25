package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.workorder.model.db.WorkOrderVisitBean;
import cn.net.yzl.workorder.model.dto.IsListPageDTO;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitCriteriaTO;
import cn.net.yzl.workorder.model.vo.WorkOrderVisitVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sound.midi.SoundbankResource;

@FeignClient(name = "workOrderVisitClient", url = "localhost:4206/visit")
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
     * @param code
     * @return
     */
    @GetMapping("v1/getByCode")
    @ApiOperation(value = "根据Code查询回访工单", notes = "根据Code查询回访工单")
    ComResponse<WorkOrderVisitBean> getByCode(@RequestParam("code") @ApiParam(value = "回访编码标识",name = "code") Integer code);

    /**
     *  分页查询回访工单列表
     * @param workOrderVisitVO
     * @return
     */
    @PostMapping("v1/listPage")
    @ApiOperation(value = "查询回访工单列表", notes = "查询回访工单列表")
    ComResponse<Page<WorkOrderVisitBean>> listPage(@RequestBody WorkOrderVisitVO workOrderVisitVO);

    @PostMapping("v1/isListPage")
    @ApiOperation(value = "查询我的回访工单列表", notes = "查询我的回访工单列表")
    ComResponse<Page<WorkOrderVisitBean>> isListPage(@RequestBody IsListPageDTO isListPageDTO);

    @GetMapping("v1/queryFirstProduct")
    @ApiOperation(value = "查询所有用户首次购买商品", notes = "查询所有用户首次购买商品")
    ComResponse<String> queryFirstProduct();

    @GetMapping("v1/查询所有用户最后一次购买商品")
    @ApiOperation(value = "查询所有用户最后一次购买商品", notes = "查询所有用户最后一次购买商品")
    ComResponse<String> queryLastProduct();
}
