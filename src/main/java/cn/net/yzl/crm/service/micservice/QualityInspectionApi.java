package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.ehr.BusinessPostDto;
import cn.net.yzl.crm.model.ProductMarketingQuality;
import cn.net.yzl.crm.model.StaffTalkQuality;

import cn.net.yzl.inspection.common.model.CallDurationQuality;
import cn.net.yzl.inspection.common.model.Quality;
import cn.net.yzl.inspection.common.model.WordQuality;
import cn.net.yzl.inspection.common.model.enums.QualityinspectionEnum;
import cn.net.yzl.inspection.common.model.vo.*;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * @ Author     ：liufaguan
 * @ Date       ：2020/12/30 10:56
 * @ Description：质检中心api
 * @Version: 1
 */
@FeignClient(value = "inspectionCenter",url = "${api.gateway.url}/qualityInspection")
public interface QualityInspectionApi {

    /**
     * author: liufaguan
     * description: 员工话术质检导入
     * create time: 2020/12/25 15:54
     * @Param: file
     * @return ComResponse<Integer>
     */
    @PostMapping(value = "/importStaffTalkQuality",headers = "content-type=multipart/form-data")
    ComResponse<Integer> importStaffTalkQuality(MultipartFile file);


    /**
     * author: liufaguan
     * description: 新增员工话术质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQualityVo
     * @return ComResponse<Integer>
     */
    @PostMapping("/saveStaffTalkQuality")
    ComResponse<Integer> saveStaffTalkQuality(@RequestBody StaffTalkQualityVo staffTalkQualityVo);


    /**
     * author: liufaguan
     * description: 员工话术质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: staffTalkQualityVo
     * @return ComResponse<Page<StaffTalkQuality>>
     */
    @PostMapping("/queryStaffTalkQualityList")
    ComResponse<Page<StaffTalkQuality>> queryStaffTalkQualityList(@RequestBody StaffTalkQualityVo staffTalkQualityVo);

    /**
     * author: liufaguan
     * description: 查看员工话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: staffTalkCode
     * @return ComResponse<StaffTalkQuality>
     */
    @GetMapping("/queryStaffTalkQualityByCode")
    ComResponse<StaffTalkQuality> queryStaffTalkQualityByCode(@RequestParam("staffTalkCode") String staffTalkCode);


    /**
     * author: liufaguan
     * description: 更新员工话术质检
     * create time: 2020/12/28 21:54
     * @Param: staffTalkQualityVo
     * @return ComResponse<Integer>
     */

    @PostMapping("/updateStaffTalkQualityByCode")
    ComResponse<Integer> updateStaffTalkQualityByCode(@RequestBody StaffTalkQualityVo staffTalkQualityVo);

    /**
     * author: liufaguan
     * description: 使用某个员工话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: staffTalkCode
     * @return ComResponse<Boolean>
     */
    @PostMapping("/updateStaffTalkQualityUsing")
    ComResponse<Boolean> updateStaffTalkQualityUsing(@RequestParam("staffTalkCode") String staffTalkCode);

    /**
     * author: liufaguan
     * description: 停用某个员工话术
     * create time: 2020/12/29 15:41
     * @Param: staffTalkCode
     * @return ComResponse<Boolean>
     */
    @PostMapping("/updateStaffTalkQualityDisabled")
    ComResponse<Boolean> updateStaffTalkQualityDisabled(@RequestParam("staffTalkCode") String staffTalkCode);

    /**
     * author: liufaguan
     * description: 产品营销话术质检导入
     * create time: 2020/12/25 15:54
     * @Param: file
     * @return ComResponse<Integer>
     */
    @PostMapping(value = "/importProductMarketingQuality",headers = "content-type=multipart/form-data")
    ComResponse<Integer> importProductMarketingQuality(MultipartFile file);

    /**
     * author: liufaguan
     * description: 新增产品营销话术质检
     * create time: 2020/12/28 10:41
     * @Param: productMarketingQualityVo
     * @return ComResponse<Boolean>
     */
    @PostMapping("saveProductMarketingQuality")
    ComResponse<Boolean> saveProductMarketingQuality(@RequestBody ProductMarketingQualityVo productMarketingQualityVo);

    /**
     * author: liufaguan
     * description: 产品营销话术质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: productMarketingQualityVo
     * @return ComResponse<Page<ProductMarketingQuality>>
     */
    @PostMapping("queryProductMarketingQualityList")
    ComResponse<Page<ProductMarketingQuality>>  queryProductMarketingQualityList(@RequestBody ProductMarketingQualityVo productMarketingQualityVo);

    /**
     * author: liufaguan
     * description: 更新产品营销话术质检
     * create time: 2020/12/28 21:54
     * @Param: productMarketingQualityVo
     * @return ComResponse<Integer>
     */
    @PostMapping("updateProductMarketingQualityByCode")
    ComResponse<Integer> updateProductMarketingQualityByCode(@RequestBody ProductMarketingQualityVo productMarketingQualityVo);

    /**
     * author: liufaguan
     * description: 使用某个产品营销话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: productMarketingCode
     * @return ComResponse<Boolean>
     */
    @PostMapping("updateProductMarketingQualityUsing")
    ComResponse<Boolean> updateProductMarketingQualityUsing(@RequestParam("productMarketingCode") String productMarketingCode);

    /**
     * author: liufaguan
     * description: 停用某个产品营销话术质检
     * create time: 2020/12/29 15:41
     * @Param: productMarketingCode
     * @return ComResponse<Boolean>
     */
    @PostMapping("updateProductMarketingQualityDisabled")
    ComResponse<Boolean> updateProductMarketingQualityDisabled(@RequestParam("productMarketingCode") String productMarketingCode);

    /**
     * author: liufaguan
     * description: 违禁词质检导入
     * create time: 2020/12/25 15:54
     * @Param: file
     * @return ComResponse<Integer>
     */
    @PostMapping(value = "/importWordQuality",headers = "content-type=multipart/form-data")
    ComResponse<Integer> importWordQuality(MultipartFile file);


    /**
     * author: liufaguan
     * description: 新增违禁词质检
     * create time: 2020/12/28 10:41
     * @Param: wordQualityVo
     * @return ComResponse<Boolean>
     */
    @PostMapping("/saveWordQuality")
    ComResponse<Boolean> saveWordQuality(@RequestBody WordQualityVo wordQualityVo);


    /**
     * author: liufaguan
     * description: 违禁词质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: wordQualityVo
     * @return ComResponse<Page<WordQuality>>
     */
    @PostMapping("/queryWordQualityList")
    ComResponse<Page<WordQuality>> queryWordQualityList(@RequestBody WordQualityVo wordQualityVo);


    /**
     * author: liufaguan
     * description: 更新违禁词质检
     * create time: 2020/12/28 21:54
     * @Param: wordQualityVo
     * @return ComResponse<Integer>
     */

    @PostMapping("/updateWordQualityByCode")
    ComResponse<Integer> updateWordQualityByCode(@RequestBody WordQualityVo wordQualityVo);

    /**
     * author: liufaguan
     * description: 使用某个违禁词质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: wordCode
     * @return ComResponse<Boolean>
     */
    @PostMapping("/updateWordQualityUsing")
    ComResponse<Boolean> updateWordQualityUsing(@RequestParam("wordCode") String wordCode);

    /**
     * author: liufaguan
     * description: 停用违禁词质检
     * create time: 2020/12/29 15:41
     * @Param: wordCode
     * @return ComResponse<Boolean>
     */
    @PostMapping("/updateWordQualityDisabled")
    ComResponse<Boolean> updateWordQualityDisabled(@RequestParam("wordCode") String wordCode);

    /**
     * author: liufaguan
     * description: 新增通话时长质检设置
     * create time: 2021/1/4 10:55
     * @Param: callDurationQualityVos
     * @return saveCallDurationCallDuration
     */
    @PostMapping("/saveCallDuration")
    ComResponse<Boolean> saveCallDuration(@RequestBody ArrayList<CallDurationQualityVo> callDurationQualityVos);

    /**
     * author: liufaguan
     * description: 查询通话时长质检设置
     * create time: 2021/1/7 19:50
     * @Param: null
     * @return ComResponse<List<CallDurationQuality>>
     */
    /*@GetMapping("/queryCallDurations")
    ComResponse<List<CallDurationQuality>> queryCallDurations();*/

    /**
     * author: liufaguan
     * description: 质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return ComResponse<Page<Quality>>
     */
    @PostMapping("/queryInspectionQualityList")
    ComResponse<Page<Quality>> queryInspectionQualityList(@RequestBody QualityVo qualityVo);

    /**
     * author: liufaguan
     * description: 质检审核列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return ComResponse<Page<Quality>>
     */
    @PostMapping("/queryInspectioncheckList")
    ComResponse<Page<Quality>> queryInspectioncheckList(@RequestBody QualityVo qualityVo);

    /**
     * author: liufaguan
     * description: 质检申诉列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return ComResponse<Page<Quality>>
     */
    @GetMapping("/queryInspectionAppealList")
    ComResponse<Page<Quality>> queryInspectionAppealList(@RequestParam("staffCode") String staffCode,@RequestParam("current") int current,@RequestParam("size") int size);

    /**
     * author: liufaguan
     * description: 质检列表查询下载
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return List<Quality>
     */
    @GetMapping("/downloadInspectionList")
    List<Quality> downloadInspectionList(@RequestParam("info") String info, @RequestParam("infoType") String infoType,
                                                @RequestParam("departmentCode") String departmentCode,
                                                @RequestParam("ehrDepartmentCode") String ehrDepartmentCode,
                                                @RequestParam("qualityType") String qualityType,
                                                @RequestParam("prohibitedLevel") String prohibitedLevel,
                                                @RequestParam("qualityStatus") String qualityStatus);

    /**
     * author: liufaguan
     * description: 申诉
     * create time: 2020/12/28 21:54
     * @Param: qualityVo
     * @return ComResponse<Integer>
     */
    @PostMapping("/appeal")
    ComResponse<Integer> appeal(@RequestBody QualityVo qualityVo);

    /**
     * author: liufaguan
     * description: 审核
     * create time: 2020/12/28 21:54
     * @Param: qualityVo
     * @return ComResponse<Integer>
     */
    @PostMapping("/check")
    ComResponse<Integer> check(@RequestBody QualityVo qualityVo);


    /**
     * author: liufaguan
     * description: 下载质检列表
     * create time: 2021/1/12 20:13
     * @Param: info...
     * @return
     */
    @GetMapping("/export")
    void exportInspection(@RequestParam("info") String info,@RequestParam("infoType") String infoType,
                            @RequestParam("departmentCode") String departmentCode,
                            @RequestParam("ehrDepartmentCode") String ehrDepartmentCode,
                            @RequestParam("qualityType") String qualityType,
                            @RequestParam("prohibitedLevel") String prohibitedLevel,
                            @RequestParam("qualityStatus") String qualityStatus,
                            HttpServletResponse response);

    /**
     * author: liufaguan
     * description: 下载审核列表
     * create time: 2021/1/12 20:13
     * @Param: info...
     * @return
     */
    @GetMapping("/exportCheck")
    void exportCheckInspection(@RequestParam("info") String info,@RequestParam("infoType") String infoType,
                                      @RequestParam("departmentCode") String departmentCode,
                                      @RequestParam("ehrDepartmentCode") String ehrDepartmentCode,
                                      @RequestParam("qualityType") String qualityType,
                                      @RequestParam("prohibitedLevel") String prohibitedLevel,
                                      @RequestParam("qualityStatus") String checkStatus,
                                      HttpServletResponse response);

    /**
     * author: liufaguan
     * description: 查询通话时长质检设置
     * create time: 2021/1/7 19:50
     * @Param: null
     * @return ComResponse<List<CallDurationQuality>>
     */
    @GetMapping("/queryCallDurations")
    ComResponse<List<CallDurationQuality>> queryCallDurations(@RequestParam("bussinessAtrrCode")Integer bussinessAtrrCode,@RequestParam("postId")Integer postId,@RequestParam("leves")List<Integer> leves);
}
