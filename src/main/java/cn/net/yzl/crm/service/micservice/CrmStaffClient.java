package cn.net.yzl.crm.service.micservice;

import cn.hutool.json.JSONObject;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.staff.dto.CustomerDto;
import cn.net.yzl.crm.staff.dto.StaffProdcutTravelDto;
import cn.net.yzl.crm.staff.dto.lasso.CalculationDto;
import cn.net.yzl.crm.staff.dto.lasso.StaffCrowdGroup;
import cn.net.yzl.crm.staff.dto.lasso.StaffCrowdGroupListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 调用crmStaffClient 服务
 */
@FeignClient(value = "yzl-crm-staff-server", url = "${api.gateway.url}/staffServer")
public interface CrmStaffClient {


    @PostMapping("staff/v1/calculationDto")
    Integer calculationDto(@RequestBody CalculationDto calculationDto);

    /**
     * 获取员工画像  商品旅程列表
     *
     * @param staffNo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/staff/v1/getStaffProductTravelList")
    ComResponse<Page<StaffProdcutTravelDto>> getStaffProductTravelList(@RequestParam("staffNo") String staffNo, @RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize);

    /**
     * 获取员工画像  顾客列表
     *
     * @param staffNo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/staff/v1/getCustomerList")
    ComResponse<Page<CustomerDto>> getCustomerList(@RequestParam("staffNo") String staffNo, @RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize);


    /**
     * 获取员工画像  顾客列表
     *
     * @param staffNo
     * @return
     */
    @GetMapping("/staff/v1/getBasicProductAdvance")
    ComResponse<List<String>> getBasicProductAdvance(@RequestParam("staffNo") String staffNo);

    /**
     * 获取员工画像  病症优势
     *
     * @param staffNo
     * @return
     */
    @GetMapping("/staff/v1/getBasicDiseaseAdvance")
    ComResponse<List<JSONObject>> getBasicDiseaseAdvance(@RequestParam("staffNo") String staffNo);

    /**
     * 保存 员工圈选接口
     *
     * @param staffCrowdGroupDTO
     * @return
     */
    @PostMapping("/staff/v1/saveStaffCrowdGroup")
    ComResponse<Boolean> saveStaffCrowdGroupDTO(@RequestBody StaffCrowdGroup staffCrowdGroupDTO);


    @GetMapping("/staff/v1/getGroupListByPage")
    ComResponse<Page<StaffCrowdGroupListDTO>> getGroupListByPage(@RequestParam("crowdGroupName") String crowdGroupName,
                                                                 @RequestParam("status") Integer status,
                                                                 @RequestParam("startTime") Date startTime,
                                                                 @RequestParam("endTime") Date endTime,
                                                                 @RequestParam("pageNo") Integer pageNo,
                                                                 @RequestParam("pageSize") Integer pageSize);

    /**
     * 员工圈选 启用 失效
     *
     * @param enable  1:启用, -1:失效
     * @param groupId 圈选组id
     * @param s
     * @return
     */
    @GetMapping("/staff/v1/updateEnable")
    ComResponse<Integer> updateEnable(@RequestParam("enable") int enable, @RequestParam("groupId") long groupId, @RequestParam("userNo") String userNo);

    /**
     * 获取圈选组 详情
     *
     * @param groupId 群组id
     * @return
     */
    @GetMapping("/staff/v1/getStaffCrowdGroup")
    ComResponse<StaffCrowdGroup> getStaffCrowdGroup(@RequestParam("groupId") long groupId);

    @GetMapping("/staff/v1/getStaffCrowdGroupList")
    ComResponse<List<StaffCrowdGroup>> getStaffCrowdGroupList(@RequestParam("id") Long id);

    default List<StaffCrowdGroup> getStaffCrowdGroupDefault(Long id) {
        ComResponse<List<StaffCrowdGroup>> staffCrowdGroupList = getStaffCrowdGroupList(id);
        if (null == staffCrowdGroupList || 200 != staffCrowdGroupList.getCode()) {
            return Collections.emptyList();
        }
        return staffCrowdGroupList.getData();
    }

    @PostMapping("/staff/v1/updateResult")
    void updateResult(@RequestBody StaffCrowdGroup staffGroup);
}
