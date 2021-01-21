package cn.net.yzl.crm.service.micservice;


import cn.hutool.json.JSONObject;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.ehr.*;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.staff.dto.lasso.Base;
import cn.net.yzl.crm.staff.dto.lasso.ScheduleDto;
import cn.net.yzl.crm.staff.dto.lasso.TrainProductDto;
import cn.net.yzl.crm.staff.dto.lasso.WorkOrderTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@FeignClient(name = "ehr-staff-api",url = "${api.gateway.url}/staffDB")
public interface EhrStaffClient {

    /**
     * 获取员工排班记录
     * @param queryDto
     * @return
     */
    @PostMapping("/attend/schedule/getListByParams")
    ComResponse getStaffScheduleInfo(StaffScheduleQueryDto queryDto);

    /**
     * 员工抢班
     * @param queryDto
     * @return
     */
    @GetMapping("/attend/schedule/robedClass")
    ComResponse robedClass(EhrRobedQueryDto queryDto);

    /**
     * 获取排班详情
     * @param staffNo
     * @param time
     * @return
     */
    @GetMapping("/attend/schedule/getDetailByStaffNoAndTime")
    ComResponse getDetailByStaffNoAndTime(@RequestParam("staffNo") String staffNo, @RequestParam("time")String time);

    /**
     * 通过员工编号获取员工基本信息
     * @param staffNo
     * @return
     */
    @GetMapping("/staff/getDetailsByNo")
    ComResponse<StaffImageBaseInfoDto> getDetailsByNo(@RequestParam("staffNo") String staffNo);

    /**
     * 获取ehr字典表  员工在职状态
     * @param dictType
     * @return
     */
    @GetMapping("/sysDic/getByType")
    ComResponse<List<StaffStatusDto>> getAllStuffStatus(@RequestParam("dictType") String dictType);

    /**
     * 获取岗位级别通过岗位
     * @param postId
     * @return
     */
    @GetMapping("/postLevel/getListByPostId")
    ComResponse<EhrPostLevelDto> getPostLevelListByPostId(@RequestParam("postId")Integer postId);

    /**
     * 获取岗位列表通过部门id
     * @param departId
     * @return
     */
    @GetMapping("/departPost/getListByDepartId")
    ComResponse<List<EhrPostDto>> getPostListByDepartId(@RequestParam("departId")Integer departId);

    /**
     * 分页查询员工列表
     * @param query
     * @return
     */
    @PostMapping("/staff/getListByParams")
    ComResponse<Page<EhrStaff>> getStaffListByPage(@RequestBody StaffQueryDto query);

    /**
     * 根据员工id获取当前员工部门以及下属部门
     * @param staffNo
     * @return
     */
    @GetMapping("/depart/getListByStaffNo")
    ComResponse<List<EhrDepartDto>> getListByStaffNo(@RequestParam("staffNo") String staffNo);

    /**
     * 根据业务属性和岗位id-获取对应岗位级别列表
     */
    @GetMapping(value = "/businessPost/getBusinessPostList")
    ComResponse<List<BusinessPostDto>> getBusiPostListByAttr(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode,@RequestParam("postId") Integer postId);

    /**
     * 根据业务属性获取岗位列表
     */
    @GetMapping(value = "/businessPost/getPostByBussinessAttrCode")
    ComResponse<List<PostDto>> getPostByBussinessAttrCode(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode);

    /**
     *  获取培训过的商品
     * @return
     */
    @GetMapping(value = "/trainCourse/selectProduct")
    ComResponse<List<String>> selectProduct();

    /**
     *  根据员工编号获取培训过的商品以及培训结果
     * @return
     * @param size
     * @param staffNo
     */
    @GetMapping(value = "/trainCourse/selectStaffTrainProduct")
    ComResponse<List<JSONObject>> selectStaffTrainProduct(@RequestParam("staffNo")String staffNo,@RequestParam("size") Integer size);

    /**
     *  根据员工编号获取员工旅程
     * @return
     * @param staffNo
     */
    @GetMapping(value = "/abnor/getStaffTrain")
    ComResponse<List<StaffTrainDto>> getStaffTrain(@RequestParam("staffNo")String staffNo);

    /**
     * 获取组织架构 部门树形列表
     * @return
     */
    @GetMapping(value = "/depart/getTreeList")
    ComResponse<EhrDepartDto> getDepartTree();



    @PostMapping("/staff/getStaffByBaseVOParams")
    ComResponse<List<String>> getStaffScheduleInfo(@RequestBody Base base);

    @PostMapping("/staff/getStaffByTrainProductParams")
    ComResponse<List<String>> getStaffTrainProduct(@RequestBody TrainProductDto trainProduct);

    @PostMapping("/staff/getStaffListByBussinessAttr")
    ComResponse<List<String>> getStaffWorkOrderType(@RequestBody List<WorkOrderTypeDto> workOrderType);

    @PostMapping("/attend/schedule/getStaffListByTime")
    ComResponse<List<String>> getStaffScheduleRemote(@RequestBody ScheduleDto schedule);


    default List<String> getStaffScheduleInfoList(Base base) {
        ComResponse<List<String>> staffScheduleInfo = this.getStaffScheduleInfo(base);
        if (null != staffScheduleInfo && staffScheduleInfo.getCode() == 200) {
            return staffScheduleInfo.getData();
        }
        return Collections.emptyList();
    }

    default List<String> getStaffTrainProductList(TrainProductDto trainProduct) {
        ComResponse<List<String>> staffTrainProduct = this.getStaffTrainProduct(trainProduct);
        if (null != staffTrainProduct && staffTrainProduct.getCode() == 200) {
            return staffTrainProduct.getData();
        }
        return Collections.emptyList();
    }

    default List<String> getStaffWorkOrderTypeList(List<WorkOrderTypeDto> workOrderType) {
        ComResponse<List<String>> staffWorkOrderType = this.getStaffWorkOrderType(workOrderType);
        if (null != staffWorkOrderType && staffWorkOrderType.getCode() == 200) {
            return staffWorkOrderType.getData();
        }
        return Collections.emptyList();
    }

    default List<String> getStaffSchedule(ScheduleDto schedule) {
        schedule.setTime(new Date());
        ComResponse<List<String>> staffSchedule = this.getStaffScheduleRemote(schedule);
        if (null != staffSchedule && staffSchedule.getCode() == 200) {
            return staffSchedule.getData();
        }
        return Collections.emptyList();
    }
}
