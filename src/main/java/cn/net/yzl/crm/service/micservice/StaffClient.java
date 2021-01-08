package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.ehr.*;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ehr-staff-api",url = "${api.gateway.url}/staffDB")
public interface StaffClient {

//    @GetMapping("getAllTypesByGroup")
//    String  getAllTypesByGroup();

    @PostMapping("/attend/schedule/getListByParams")
    ComResponse getStaffScheduleInfo(StaffScheduleQueryDto queryDto);

    @GetMapping("/attend/schedule/robedClass")
    ComResponse robedClass(EhrRobedQueryDto queryDto);

    @GetMapping("/attend/schedule/getDetailByStaffNoAndTime")
    ComResponse getDetailByStaffNoAndTime(@RequestParam("staffNo") String staffNo, @RequestParam("time")String time);

    @GetMapping("/staff/getDetailsByNo")
    ComResponse<StaffImageBaseInfoDto> getDetailsByNo(@RequestParam("staffNo") String staffNo);

    @GetMapping("/sysDic/getByType")
    ComResponse<List<StaffStatusDto>> getAllStuffStatus(@RequestParam("dictType") String dictType);

    @GetMapping("/postLevel/getListByPostId")
    ComResponse<EhrPostLevelDto> getPostLevelListByPostId(@RequestParam("postId")Integer postId);

    @GetMapping("/departPost/getListByDepartId")
    ComResponse<List<EhrPostDto>> getPostListByDepartId(@RequestParam("departId")Integer departId);

    @PostMapping("/staff/getListByParams")
    ComResponse<Page<EhrStaff>> getStaffListByPage(StaffQueryDto query);

    @GetMapping("/depart/getListByStaffNo")
    ComResponse<List<EhrDepartDto>> getListByStaffNo(@RequestParam("staffNo") String staffNo);
}
