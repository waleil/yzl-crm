package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.ProductMarketingQuality;
import cn.net.yzl.crm.model.StaffTalkQuality;

import cn.net.yzl.inspection.common.model.CallDurationQuality;
import cn.net.yzl.inspection.common.model.WordQuality;
import cn.net.yzl.inspection.common.model.vo.CallDurationQualityVo;
import cn.net.yzl.inspection.common.model.vo.ProductMarketingQualityVo;
import cn.net.yzl.inspection.common.model.vo.StaffTalkQualityVo;
import cn.net.yzl.inspection.common.model.vo.WordQualityVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/queryCallDurations")
    ComResponse<List<CallDurationQuality>> queryCallDurations();
}
