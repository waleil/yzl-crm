package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.ProductMarketingQuality;
import cn.net.yzl.crm.model.StaffTalkQuality;
import cn.net.yzl.crm.service.micservice.QualityInspectionApi;

import cn.net.yzl.inspection.model.vo.ProductMarketingQualityVo;
import cn.net.yzl.inspection.model.vo.StaffTalkQualityVo;
import cn.net.yzl.inspection.model.WordQuality;
import cn.net.yzl.inspection.model.vo.WordQualityVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ Author     ：liufaguan
 * @ Date       ：2020/12/30 11:12
 * @ Description：质检中心
 * @Version: 1
 */
@Api(tags = "质检中心")
@RestController
@RequestMapping("inspectionCenter")
public class QualityInspectionController {

    @Autowired
    private QualityInspectionApi qualityInspectionApi;

    /**
     * author: liufaguan
     * description: 员工话术质检导入
     * create time: 2020/12/25 15:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="员工话术质检导入",httpMethod = "POST")
    @PostMapping("/importStaffTalkQuality")
    public ComResponse<Integer> importStaffTalkQuality(@RequestParam("name") String name){
        return qualityInspectionApi.importStaffTalkQuality();
    }


    /**
     * author: liufaguan
     * description: 新增员工话术质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQuality
     * @return
     */
    @ApiOperation(value="新增员工话术质检",httpMethod = "POST")
    @PostMapping("/saveStaffTalkQuality")
    public ComResponse<Integer> saveStaffTalkQuality(@RequestBody StaffTalkQualityVo staffTalkQualityVo){
        return qualityInspectionApi.saveStaffTalkQuality(staffTalkQualityVo);
    }


    /**
     * author: liufaguan
     * description: 员工话术质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @ApiOperation(value="员工话术质检列表",httpMethod = "GET")
    @PostMapping("/queryStaffTalkQualityList")
    public ComResponse<Page<StaffTalkQuality>> queryStaffTalkQualityList(@RequestBody StaffTalkQualityVo staffTalkQualityVo){
        return qualityInspectionApi.queryStaffTalkQualityList(staffTalkQualityVo);
    }

    /**
     * author: liufaguan
     * description: 查看员工话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @ApiOperation(value="查看员工话术质检",httpMethod = "GET")
    @GetMapping("/queryStaffTalkQualityByCode")
    public ComResponse<StaffTalkQuality> queryStaffTalkQualityByCode(@RequestParam("staffTalkCode") String staffTalkCode){
        return qualityInspectionApi.queryStaffTalkQualityByCode(staffTalkCode);
    }


    /**
     * author: liufaguan
     * description: 更新员工话术质检
     * create time: 2020/12/28 21:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="更新员工话术质检",httpMethod = "POST")
    @PostMapping("/updateStaffTalkQualityByCode")
    public ComResponse<Integer> updateStaffTalkQualityByCode(@RequestBody StaffTalkQualityVo staffTalkQualityVo){
        return qualityInspectionApi.updateStaffTalkQualityByCode(staffTalkQualityVo);
    }

    /**
     * author: liufaguan
     * description: 使用某个员工话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @ApiOperation(value="使用某个员工话术质检",httpMethod = "POST")
    @PostMapping("/updateStaffTalkQualityUsing")
    public ComResponse<Boolean> updateStaffTalkQualityUsing(@RequestParam("staffTalkCode") String staffTalkCode){
        return qualityInspectionApi.updateStaffTalkQualityUsing(staffTalkCode);
    }

    /**
     * author: liufaguan
     * description: 停用某个员工话术
     * create time: 2020/12/29 15:41
     * @Param: null
     * @return
     */
    @ApiOperation(value="停用某个员工话术质检",httpMethod = "POST")
    @PostMapping("/updateStaffTalkQualityDisabled")
    public ComResponse<Boolean> updateStaffTalkQualityDisabled(@RequestParam("staffTalkCode") String staffTalkCode){
        return qualityInspectionApi.updateStaffTalkQualityDisabled(staffTalkCode);
    }

    /**
     * author: liufaguan
     * description: 产品营销话术质检导入
     * create time: 2020/12/25 15:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="产品营销话术质检导入",httpMethod = "POST")
    @PostMapping("/importProductMarketingQuality")
    ComResponse<Integer> importProductMarketingQuality(@RequestParam("name") String name){
        return qualityInspectionApi.importProductMarketingQuality();
    }

    /**
     * author: liufaguan
     * description: 新增产品营销话术质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQuality
     * @return
     */
    @ApiOperation(value="新增产品营销话术质检",httpMethod = "POST")
    @PostMapping("/saveProductMarketingQuality")
    ComResponse<Boolean> saveProductMarketingQuality(@RequestBody ProductMarketingQualityVo productMarketingQualityVo){
        return qualityInspectionApi.saveProductMarketingQuality(productMarketingQualityVo);
    }

    /**
     * author: liufaguan
     * description: 产品营销话术质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @ApiOperation(value="产品营销话术质检列表",httpMethod = "GET")
    @PostMapping("/queryProductMarketingQualityList")
    ComResponse<Page<ProductMarketingQuality>> queryProductMarketingQualityList(@RequestBody ProductMarketingQualityVo productMarketingQualityVo){
       return qualityInspectionApi.queryProductMarketingQualityList(productMarketingQualityVo);
    }



    /**
     * author: liufaguan
     * description: 更新产品营销话术质检
     * create time: 2020/12/28 21:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="更新产品营销话术质检",httpMethod = "POST")
    @PostMapping("/updateProductMarketingQualityByCode")
    ComResponse<Integer> updateProductMarketingQualityByCode(@RequestBody ProductMarketingQualityVo productMarketingQualityVo){
        return qualityInspectionApi.updateProductMarketingQualityByCode(productMarketingQualityVo);
    }

    /**
     * author: liufaguan
     * description: 使用某个产品营销话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @ApiOperation(value="使用某个产品营销话术质检",httpMethod = "POST")
    @PostMapping("/updateProductMarketingQualityUsing")
    ComResponse<Boolean> updateProductMarketingQualityUsing(@RequestParam("productMarketingCode") String productMarketingCode){
        return qualityInspectionApi.updateProductMarketingQualityUsing(productMarketingCode);
    }

    /**
     * author: liufaguan
     * description: 停用某个产品营销话术质检
     * create time: 2020/12/29 15:41
     * @Param: null
     * @return
     */
    @ApiOperation(value="停用某个产品营销话术质检",httpMethod = "POST")
    @PostMapping("/updateProductMarketingQualityDisabled")
    ComResponse<Boolean> updateProductMarketingQualityDisabled(@RequestParam("productMarketingCode") String productMarketingCode){
        return qualityInspectionApi.updateProductMarketingQualityDisabled(productMarketingCode);
    }

    /**
     * author: liufaguan
     * description: 违禁词质检导入
     * create time: 2020/12/25 15:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="违禁词质检导入",httpMethod = "POST")
    @PostMapping("/importWordQuality")
    ComResponse<Integer> importWordQuality(@RequestParam("name") String name){
        return qualityInspectionApi.importWordQuality(name);
    }


    /**
     * author: liufaguan
     * description: 新增违禁词质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQuality
     * @return
     */
    @ApiOperation(value="新增违禁词质检",httpMethod = "POST")
    @PostMapping("/saveWordQuality")
    ComResponse<Boolean> saveWordQuality(@RequestBody WordQualityVo wordQualityVo){
        return qualityInspectionApi.saveWordQuality(wordQualityVo);
    }


    /**
     * author: liufaguan
     * description: 违禁词质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @ApiOperation(value="违禁词质检列表",httpMethod = "GET")
    @PostMapping("/queryWordQualityList")
    ComResponse<Page<WordQuality>> queryWordQualityList(@RequestBody WordQualityVo wordQualityVo){
        return qualityInspectionApi.queryWordQualityList(wordQualityVo);
    }


    /**
     * author: liufaguan
     * description: 更新违禁词质检
     * create time: 2020/12/28 21:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="更新违禁词质检",httpMethod = "POST")
    @PostMapping("/updateWordQualityByCode")
    ComResponse<Integer> updateWordQualityByCode(@RequestBody WordQualityVo wordQualityVo){
        return qualityInspectionApi.updateWordQualityByCode(wordQualityVo);
    }

    /**
     * author: liufaguan
     * description: 使用某个违禁词质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return
     */
    @ApiOperation(value="使用某个违禁词质检",httpMethod = "POST")
    @PostMapping("/updateWordQualityUsing")
    ComResponse<Boolean> updateWordQualityUsing(@RequestParam("wordCode") String wordCode){
        return qualityInspectionApi.updateWordQualityUsing(wordCode);
    }

    /**
     * author: liufaguan
     * description: 停用违禁词质检
     * create time: 2020/12/29 15:41
     * @Param: null
     * @return
     */
    @ApiOperation(value="停用违禁词质检",httpMethod = "POST")
    @PostMapping("/updateWordQualityDisabled")
    ComResponse<Boolean> updateWordQualityDisabled(@RequestParam("wordCode") String wordCode){
        return qualityInspectionApi.updateWordQualityDisabled(wordCode);
    }


}
