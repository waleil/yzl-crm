package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.model.ProductMarketingQuality;
import cn.net.yzl.crm.model.StaffTalkQuality;
import cn.net.yzl.crm.service.micservice.QualityInspectionApi;

import cn.net.yzl.crm.utils.GetParamsValue;
import cn.net.yzl.crm.utils.ValidateUtils;
import cn.net.yzl.inspection.common.model.CallDurationQuality;
import cn.net.yzl.inspection.common.model.WordQuality;
import cn.net.yzl.inspection.common.model.vo.CallDurationQualityVo;
import cn.net.yzl.inspection.common.model.vo.ProductMarketingQualityVo;
import cn.net.yzl.inspection.common.model.vo.StaffTalkQualityVo;
import cn.net.yzl.inspection.common.model.vo.WordQualityVo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * @Param: file
     * @return ComResponse<Integer>
     */
    @ApiOperation(value="员工话术质检导入",httpMethod = "POST")
    @PostMapping(value = "/importStaffTalkQuality",headers = "content-type=multipart/form-data")
    public ComResponse<Integer> importStaffTalkQuality(MultipartFile file){
        return qualityInspectionApi.importStaffTalkQuality(file);
    }


    /**
     * author: liufaguan
     * description: 新增员工话术质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQualityVo
     * @return ComResponse<Integer>
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
     * @Param: staffTalkQualityVo
     * @return ComResponse<Page<StaffTalkQuality>>
     */
    @ApiOperation(value="员工话术质检列表",httpMethod = "POST")
    @PostMapping("/queryStaffTalkQualityList")
    public ComResponse<Page<StaffTalkQuality>> queryStaffTalkQualityList(@RequestBody StaffTalkQualityVo staffTalkQualityVo){
        return qualityInspectionApi.queryStaffTalkQualityList(staffTalkQualityVo);
    }

    /**
     * author: liufaguan
     * description: 查看员工话术质检
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: staffTalkCode
     * @return ComResponse<StaffTalkQuality>
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
     * @Param: staffTalkQualityVo
     * @return ComResponse<Integer>
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
     * @Param: staffTalkCode
     * @return ComResponse<Boolean>
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
     * @Param: staffTalkCode
     * @return ComResponse<Boolean>
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
     * @Param: file
     * @return ComResponse<Integer>
     */
    @ApiOperation(value="产品营销话术质检导入",httpMethod = "POST")
    @PostMapping(value = "/importProductMarketingQuality",headers = "content-type=multipart/form-data")
    ComResponse<Integer> importProductMarketingQuality(MultipartFile file){
        return qualityInspectionApi.importProductMarketingQuality(file);
    }

    /**
     * author: liufaguan
     * description: 新增产品营销话术质检
     * create time: 2020/12/28 10:41
     * @Param: productMarketingQualityVo
     * @return ComResponse<Boolean>
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
     * @Param: productMarketingQualityVo
     * @return ComResponse<Page<ProductMarketingQuality>>
     */
    @ApiOperation(value="产品营销话术质检列表",httpMethod = "POST")
    @PostMapping("/queryProductMarketingQualityList")
    ComResponse<Page<ProductMarketingQuality>> queryProductMarketingQualityList(@RequestBody ProductMarketingQualityVo productMarketingQualityVo){
       return qualityInspectionApi.queryProductMarketingQualityList(productMarketingQualityVo);
    }



    /**
     * author: liufaguan
     * description: 更新产品营销话术质检
     * create time: 2020/12/28 21:54
     * @Param: productMarketingQualityVo
     * @return ComResponse<Integer>
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
     * @Param: productMarketingCode
     * @return ComResponse<Boolean>
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
     * @Param: productMarketingCode
     * @return ComResponse<Boolean>
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
     * @Param: file
     * @return ComResponse<Integer>
     */
    @ApiOperation(value="违禁词质检导入",httpMethod = "POST")
    @PostMapping(value = "/importWordQuality",headers = "content-type=multipart/form-data")
    ComResponse<Integer> importWordQuality(MultipartFile file){
        return qualityInspectionApi.importWordQuality(file);
    }


    /**
     * author: liufaguan
     * description: 新增违禁词质检
     * create time: 2020/12/28 10:41
     * @Param: staffTalkQuality
     * @return ComResponse<Boolean>
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
     * @Param: wordQualityVo
     * @return ComResponse<Page<WordQuality>>
     */
    @ApiOperation(value="违禁词质检列表",httpMethod = "POST")
    @PostMapping("/queryWordQualityList")
    ComResponse<Page<WordQuality>> queryWordQualityList(@RequestBody WordQualityVo wordQualityVo){
        return qualityInspectionApi.queryWordQualityList(wordQualityVo);
    }


    /**
     * author: liufaguan
     * description: 更新违禁词质检
     * create time: 2020/12/28 21:54
     * @Param: wordQualityVo
     * @return ComResponse<Integer>
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
     * @Param: wordCode
     * @return ComResponse<Boolean>
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
     * @Param: wordCode
     * @return ComResponse<Boolean>
     */
    @ApiOperation(value="停用违禁词质检",httpMethod = "POST")
    @PostMapping("/updateWordQualityDisabled")
    ComResponse<Boolean> updateWordQualityDisabled(@RequestParam("wordCode") String wordCode){
        return qualityInspectionApi.updateWordQualityDisabled(wordCode);
    }

    /**
     * author: liufaguan
     * description: 新增通话时长质检设置
     * create time: 2021/1/4 10:55
     * @Param: callDurationQualityVos
     * @return ComResponse<Boolean>
     */
    @ApiOperation(value="新增通话时长质检设置",httpMethod = "POST")
    @PostMapping("/saveCallDuration")
    ComResponse<Boolean> saveCallDuration(@RequestBody ArrayList<CallDurationQualityVo> callDurationQualityVos){
        System.out.println(callDurationQualityVos.toString());
        return qualityInspectionApi.saveCallDuration(callDurationQualityVos);
    }

    /**
     * author: liufaguan
     * description: 查询通话时长质检设置
     * create time: 2021/1/7 19:50
     * @Param: null
     * @return ComResponse<List<CallDurationQuality>>
     */
    @ApiOperation(value="查询通话时长质检设置",httpMethod = "POST")
    @GetMapping("/queryCallDurations")
    ComResponse<List<CallDurationQuality>> queryCallDurations(){
        return qualityInspectionApi.queryCallDurations();
    }

}
