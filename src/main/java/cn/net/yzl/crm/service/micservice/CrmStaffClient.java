package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import cn.net.yzl.crm.staff.dto.lasso.StaffCrowdGroupDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
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

    /**
     * 保存 员工圈选接口
     * @param staffCrowdGroupDTO
     * @return
     */
    @PostMapping("/staff/v1/saveStaffCrowdGroupDTO")
    Integer saveStaffCrowdGroupDTO(@RequestBody StaffCrowdGroupDTO staffCrowdGroupDTO);
    @GetMapping("/staff/v1/getGroupListByPage")
    ComResponse<Page<StaffCrowdGroupDTO>> getGroupListByPage(@RequestParam("crowdGroupName")String crowdGroupName,
                                                             @RequestParam("status") Integer status,
                                                             @RequestParam("startTime") Date startTime,
                                                             @RequestParam("endTime") Date endTime,
                                                             @RequestParam("pageNo")Integer pageNo,
                                                             @RequestParam("pageSize") Integer pageSize);

    /**
     * 员工圈选 试算
     * @param groupId
     * @return
     */
    @GetMapping("/staff/v1/trialStaffNo")
    ComResponse<Integer> trialStaffNo(@RequestParam("groupId") long groupId);

    /**
     *  员工圈选 启用 失效
     * @param enable 1:启用, -1:失效
     * @param groupId 圈选组id
     * @return
     */
    @GetMapping("/staff/v1/updateEnable")
    ComResponse<Integer> updateEnable(@RequestParam("enable") int enable,@RequestParam("groupId") long groupId);

    /**
     *  获取圈选组 详情
     * @param groupId 群组id
     * @return
     */
    @GetMapping("/staff/v1/getStaffCrowdGroupDTO")
    ComResponse<StaffCrowdGroupDTO> getStaffCrowdGroupDTO(@RequestParam("groupId")long groupId);
}
