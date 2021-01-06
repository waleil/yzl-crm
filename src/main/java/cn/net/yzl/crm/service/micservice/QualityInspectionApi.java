package cn.net.yzl.crm.service.micservice;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.ProductMarketingQuality;
import cn.net.yzl.crm.model.StaffTalkQuality;

import cn.net.yzl.inspection.common.model.WordQuality;
import cn.net.yzl.inspection.common.model.vo.ProductMarketingQualityVo;
import cn.net.yzl.inspection.common.model.vo.StaffTalkQualityVo;
import cn.net.yzl.inspection.common.model.vo.WordQualityVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


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
     * @Param: null
     * @return
     */
    @PostMapping("/importStaffTalkQuality")
    ComResponse<Integer> importStaffTalkQuality(@RequestParam("file") MultipartFile file);


    /**
     * author: liufaguan
     * description: 新增员工话术质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQuality
     * @return
     */
    @PostMapping("/saveStaffTalkQuality")
    ComResponse<Integer> saveStaffTalkQuality(@RequestBody StaffTalkQualityVo staffTalkQualityVo);


    /**
     * author: liufaguan
     * description: 员工话术质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @PostMapping("/queryStaffTalkQualityList")
    ComResponse<Page<StaffTalkQuality>> queryStaffTalkQualityList(@RequestBody StaffTalkQualityVo staffTalkQualityVo);

    /**
     * author: liufaguan
     * description: 查看员工话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @GetMapping("/queryStaffTalkQualityByCode")
    ComResponse<StaffTalkQuality> queryStaffTalkQualityByCode(@RequestParam("staffTalkCode") String staffTalkCode);


    /**
     * author: liufaguan
     * description: 更新员工话术质检
     * create time: 2020/12/28 21:54
     * @Param: null
     * @return
     */

    @PostMapping("/updateStaffTalkQualityByCode")
    ComResponse<Integer> updateStaffTalkQualityByCode(@RequestBody StaffTalkQualityVo staffTalkQualityVo);

    /**
     * author: liufaguan
     * description: 使用某个员工话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @PostMapping("/updateStaffTalkQualityUsing")
    ComResponse<Boolean> updateStaffTalkQualityUsing(@RequestParam("staffTalkCode") String staffTalkCode);

    /**
     * author: liufaguan
     * description: 停用某个员工话术
     * create time: 2020/12/29 15:41
     * @Param: null
     * @return
     */
    @PostMapping("/updateStaffTalkQualityDisabled")
    ComResponse<Boolean> updateStaffTalkQualityDisabled(@RequestParam("staffTalkCode") String staffTalkCode);

    /**
     * author: liufaguan
     * description: 产品营销话术质检导入
     * create time: 2020/12/25 15:54
     * @Param: null
     * @return
     */
    @PostMapping("importProductMarketingQuality")
    ComResponse<Integer> importProductMarketingQuality(@RequestParam("file") MultipartFile file);

    /**
     * author: liufaguan
     * description: 新增产品营销话术质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQuality
     * @return
     */
    @PostMapping("saveProductMarketingQuality")
    ComResponse<Boolean> saveProductMarketingQuality(@RequestBody ProductMarketingQualityVo productMarketingQualityVo);

    /**
     * author: liufaguan
     * description: 产品营销话术质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @PostMapping("queryProductMarketingQualityList")
    ComResponse<Page<ProductMarketingQuality>>  queryProductMarketingQualityList(@RequestBody ProductMarketingQualityVo productMarketingQualityVo);

    /**
     * author: liufaguan
     * description: 更新产品营销话术质检
     * create time: 2020/12/28 21:54
     * @Param: null
     * @return
     */
    @PostMapping("updateProductMarketingQualityByCode")
    ComResponse<Integer> updateProductMarketingQualityByCode(@RequestBody ProductMarketingQualityVo productMarketingQualityVo);

    /**
     * author: liufaguan
     * description: 使用某个产品营销话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @PostMapping("updateProductMarketingQualityUsing")
    ComResponse<Boolean> updateProductMarketingQualityUsing(@RequestParam("productMarketingCode") String productMarketingCode);

    /**
     * author: liufaguan
     * description: 停用某个产品营销话术质检
     * create time: 2020/12/29 15:41
     * @Param: null
     * @return
     */
    @PostMapping("updateProductMarketingQualityDisabled")
    ComResponse<Boolean> updateProductMarketingQualityDisabled(@RequestParam("productMarketingCode") String productMarketingCode);

    /**
     * author: liufaguan
     * description: 违禁词质检导入
     * create time: 2020/12/25 15:54
     * @Param: null
     * @return
     */
    @PostMapping("/importWordQuality")
    ComResponse<Integer> importWordQuality(@RequestParam("file") MultipartFile file);


    /**
     * author: liufaguan
     * description: 新增违禁词质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQuality
     * @return
     */
    @PostMapping("/saveWordQuality")
    ComResponse<Boolean> saveWordQuality(@RequestBody WordQualityVo wordQualityVo);


    /**
     * author: liufaguan
     * description: 违禁词质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @PostMapping("/queryWordQualityList")
    ComResponse<Page<WordQuality>> queryWordQualityList(@RequestBody WordQualityVo wordQualityVo);


    /**
     * author: liufaguan
     * description: 更新违禁词质检
     * create time: 2020/12/28 21:54
     * @Param: null
     * @return
     */

    @PostMapping("/updateWordQualityByCode")
    ComResponse<Integer> updateWordQualityByCode(@RequestBody WordQualityVo wordQualityVo);

    /**
     * author: liufaguan
     * description: 使用某个违禁词质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @PostMapping("/updateWordQualityUsing")
    ComResponse<Boolean> updateWordQualityUsing(@RequestParam("wordCode") String wordCode);

    /**
     * author: liufaguan
     * description: 停用违禁词质检
     * create time: 2020/12/29 15:41
     * @Param: null
     * @return
     */
    @PostMapping("/updateWordQualityDisabled")
    ComResponse<Boolean> updateWordQualityDisabled(@RequestParam("wordCode") String wordCode);


}
