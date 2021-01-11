package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 调用crmStaffClient 服务
 */
@FeignClient(value = "yzl-crm-staff-server",url = "${api.gateway.url}/staffServer")
public interface CrmStaffClient {

    /**
     * 获取员工画像  商品旅程列表
     * @param staffNo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/staff/v1/getStaffProductTravelList")
     ComResponse<Page<StaffProdcutTravelDto>> getStaffProductTravelList(@RequestParam("staffNo")  Integer staffNo,@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize")  Integer pageSize);
    /**
     * 获取员工画像  顾客列表
     * @param staffNo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/staff/v1/getCustomerList")
    ComResponse<Page<CustomerDto>> getCustomerList(@RequestParam("staffNo")  Integer staffNo,@RequestParam("pageNumber") Integer pageNumber,@RequestParam("pageSize") Integer pageSize);


    /**
     * 获取员工画像  顾客列表
     * @param staffNo
     * @return
     */
    @GetMapping("/staff/v1/getBasicProductAdvance")
    ComResponse<List<String>> getBasicProductAdvance(@RequestParam("staffNo")Integer staffNo);

    /**
     * 获取员工画像  病症优势
     * @param staffNo
     * @return
     */
    @GetMapping("/staff/v1/getBasicDiseaseAdvance")
    ComResponse<List<String>> getBasicDiseaseAdvance(@RequestParam("staffNo")Integer staffNo);
}
