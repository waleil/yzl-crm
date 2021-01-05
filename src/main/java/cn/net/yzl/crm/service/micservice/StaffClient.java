package cn.net.yzl.crm.service.micservice;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dto.ehr.EhrRobedQueryDto;
import cn.net.yzl.crm.dto.ehr.StaffScheduleQueryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ehr-staff-api",url = "${api.gateway.url}/staffDB")
public interface StaffClient {

//    @GetMapping("getAllTypesByGroup")
//    String  getAllTypesByGroup();

    @PostMapping("/attend/schedule/getListByParams")
    ComResponse getStaffScheduleInfo(StaffScheduleQueryDto queryDto);

    @GetMapping("/attend/schedule/robedClass")
    ComResponse robedClass(EhrRobedQueryDto queryDto);
}
