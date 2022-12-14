package cn.net.yzl.crm.service.micservice;


import cn.hutool.json.JSONObject;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.ehr.*;
import cn.net.yzl.crm.dto.staff.StaffChangeRecordDto;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.model.StaffDetail;
import cn.net.yzl.crm.staff.dto.lasso.Base;
import cn.net.yzl.crm.staff.dto.lasso.ScheduleDto;
import cn.net.yzl.crm.staff.dto.lasso.TrainProductDto;
import cn.net.yzl.crm.staff.dto.lasso.WorkOrderTypeDto;
import cn.net.yzl.model.dto.DepartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FeignClient(name = "ehr-staff-api", url = "${api.gateway.url}/staffDB")
//@FeignClient(name = "ehr-staff-api", url = "localhost:38080/")
public interface EhrStaffClient {

    /**
     * 获取员工排班记录
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/attend/schedule/getListByParams")
    ComResponse getStaffBaseInfo(StaffScheduleQueryDto queryDto);

    /**
     * 员工抢班
     *
     * @param queryDto
     * @return
     */
    @GetMapping("/attend/schedule/robedClass")
    ComResponse robedClass(EhrRobedQueryDto queryDto);

    /**
     * 获取排班详情
     *
     * @param staffNo
     * @param time
     * @return
     */
    @GetMapping("/attend/schedule/getDetailByStaffNoAndTime")
    ComResponse getDetailByStaffNoAndTime(@RequestParam("staffNo") String staffNo, @RequestParam("time") String time);

    /**
     * 通过员工编号获取员工基本信息
     *
     * @param staffNo
     * @return
     */
    @GetMapping("/staff/getDetailsByNo")
    ComResponse<StaffImageBaseInfoDto> getDetailsByNo(@RequestParam("staffNo") String staffNo);

    /**
     * 根据staffno数组批量查询用户详情
     *
     * @param list
     * @return
     */
    @PostMapping(value = "/staff/getDetailsListByNo")
    ComResponse<List<StaffDetail>> getDetailsListByNo(@RequestBody List<String> list);

    /**
     * 获取ehr字典表  员工在职状态
     *
     * @param dictType
     * @return
     */
    @GetMapping("/sysDic/getByType")
    ComResponse<List<SysDictDto>> getAllStuffStatus(@RequestParam("dictType") String dictType);

    /**
     * 获取岗位级别通过岗位
     *
     * @param postId
     * @return
     */
    @GetMapping("/postLevel/getListByPostId")
    ComResponse<EhrPostLevelDto> getPostLevelListByPostId(@RequestParam("postId") Integer postId);

    @GetMapping(value = "/depart/getChildTreeById")
    ComResponse<List<DepartDto>> getChildTreeById(@RequestParam("departId")Integer departId);

    /**
     * 获取岗位列表通过部门id
     *
     * @param departId
     * @return
     */
    @GetMapping("/departPost/getListByDepartId")
    ComResponse<List<EhrPostDto>> getPostListByDepartId(@RequestParam("departId") Integer departId);

    /**
     * 分页查询员工列表
     *
     * @param query
     * @return
     */
    @PostMapping("/staff/getListByParams")
    ComResponse<Page<EhrStaff>> getStaffListByPage(@RequestBody StaffQueryDto query);

    /**
     * 根据员工id获取当前员工部门以及下属部门
     *
     * @param staffNo
     * @return
     */
    @GetMapping("/depart/getListByStaffNo")
    ComResponse<List<EhrDepartDto>> getListByStaffNo(@RequestParam("staffNo") String staffNo);

    /**
     * 根据业务属性和岗位id-获取对应岗位级别列表
     */
    @GetMapping(value = "/businessPost/getBusinessPostList")
    ComResponse<List<BusinessPostDto>> getBusiPostListByAttr(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode, @RequestParam("postId") Integer postId);

    /**
     * 根据业务属性获取岗位列表
     */
    @GetMapping(value = "/businessPost/getPostByBussinessAttrCode")
    ComResponse<List<PostDto>> getPostByBussinessAttrCode(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode);

    /**
     * 根据业务属性获取部门 list
     * @param bussinessAttrId
     * @return
     */
    @RequestMapping(value = "/depart/getListByBusinessAttrId", method = RequestMethod.GET)
    ComResponse<List<DepartDto>> getListByBusinessAttrId(@RequestParam("bussinessAttrId") String bussinessAttrId);

    /**
     * 根据多个业务属性获取部门列表 list
     * @param bussinessAttrIds
     * @return
     */
    @RequestMapping(value = "/depart/getListByBusinessAttrIds", method = RequestMethod.GET)
    ComResponse<List<DepartDto>> getListByBusinessAttrIds(@RequestParam("bussinessAttrIds") String bussinessAttrIds);

    /**
     * 多条件获取 员工list
     * @param staffParamsVO
     * @return
     */ @RequestMapping(value = "/staff/getListsByParams", method = RequestMethod.POST)
    ComResponse<List<EhrStaff>> getListsByParams(@RequestBody  StaffQueryDto staffParamsVO);
    /**
     * 获取培训过的商品
     *
     * @return
     */
    @GetMapping(value = "/trainCourse/selectProduct")
    ComResponse<List<String>> selectProduct();

    /**
     * 根据员工编号获取培训过的商品以及培训结果
     *
     * @param size
     * @param staffNo
     * @return
     */
    @GetMapping(value = "/trainCourse/selectStaffTrainProduct")
    ComResponse<List<JSONObject>> selectStaffTrainProduct(@RequestParam("staffNo") String staffNo, @RequestParam("size") Integer size);

    /**
     * 根据员工编号获取员工旅程
     *
     * @param staffNo
     * @return
     */
    @GetMapping(value = "/abnor/getStaffTrain")
    ComResponse<List<StaffTrainDto>> getStaffTrain(@RequestParam("staffNo") String staffNo);

    /**
     * 获取组织架构 部门树形列表
     *
     * @return
     */
    @GetMapping(value = "/depart/getTreeList")
    ComResponse<EhrDepartDto> getDepartTree();


    @PostMapping("/staff/getStaffByBaseVOParams")
    ComResponse<List<String>> getStaffBaseInfo(@RequestBody Base baseVOParams);

    @PostMapping("/staff/getStaffByTrainProductParams")
    ComResponse<List<String>> getStaffTrainProduct(@RequestBody TrainProductDto trainProduct);

    @PostMapping("/staff/getStaffListByBussinessAttr")
    ComResponse<List<String>> getStaffWorkOrderType(@RequestBody List<WorkOrderTypeDto> workOrderType);

    @PostMapping("/attend/schedule/getStaffListByTime")
    ComResponse<List<String>> getStaffScheduleRemote(@RequestBody ScheduleDto schedule);

    @PostMapping("/staff/getByStaffNos")
    ComResponse<List<StaffDetailDto>> getByStaffNos(@RequestBody List<String> staffNos);

    @GetMapping(value = "/post/getPostList")
    ComResponse<List<CommonPostDto>> getPostList();

    @GetMapping(value = "/staff/getStaffByPostIds")
    ComResponse<List<String>> getPostIdListRemote(@RequestBody List<String> postIdList);

    /**
     * 根据id获取部门信息
     *
     * @param departId 部门id
     * @return 部门信息
     * @author zhangweiwei
     * @date 2021年1月25日, 下午1:04:20
     */
    @GetMapping("/depart/getById")
    ComResponse<DepartDto> getDepartById(@RequestParam @NotNull Integer departId);

    /**
     * 员工变动-查询员工最新变动后状态
     * @param staffNo
     * @return
     */
    @GetMapping(value = "/staffChange/getStaffLastChangeRecord")
    ComResponse<StaffChangeRecordDto> getStaffLastChangeRecord(@RequestParam("staffNo") String staffNo);

    /**
     * 员工变动-根据变动编号查询员工变动状态
     * @param id
     * @return
     */
    @GetMapping(value = "/staffChange/getStaffChangeRecordById")
    ComResponse<StaffChangeRecordDto> getStaffChangeRecordById(@RequestParam("id") Integer id);


    default List<String> getStaffBaseInfoList(Base base) {
        ComResponse<List<String>> staffScheduleInfo = getStaffBaseInfo(base);
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
        workOrderType.forEach(workOrderTypeDto -> {
            if (0 == workOrderTypeDto.getWorkOrderType()) {
                workOrderTypeDto.setWorkOrderType(41);
            } else {
                workOrderTypeDto.setWorkOrderType(42);
            }
        });
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

    default List<String> getPostIdList(List<String> postIdList) {
        try{
            ComResponse<List<String>> postId = this.getPostIdListRemote(postIdList);
            if (null != postId && postId.getCode() == 200) {
                return postId.getData();
            }
            return Collections.emptyList();
        }catch (Exception e){
            return Collections.emptyList();
        }
    }


    default Map<String, StaffDetailDto> getMapByStaffNos(List<String> staffNos) {
        ComResponse<List<StaffDetailDto>> byStaffNos;
        try {
            byStaffNos = getByStaffNos(staffNos);
        } catch (Exception e) {
            return null;
        }
        if (null == byStaffNos) {
            return null;
        }
        List<StaffDetailDto> data = byStaffNos.getData();
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        return data.stream().collect(Collectors.toMap(StaffDetailDto::getUserNo, staff -> staff, (oldValue, newValue) -> newValue));
    }


    default List<StaffDetail> getDetailsListByNoDefault(List<String> userNos) {
        ComResponse<List<StaffDetail>> listComResponse;
        try {
            listComResponse =  this.getDetailsListByNo(userNos);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        if (null == listComResponse) {
            return Collections.emptyList();
        }
        return listComResponse.getData();
    }
}
