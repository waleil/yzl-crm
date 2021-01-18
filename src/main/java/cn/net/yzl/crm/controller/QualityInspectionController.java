package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.dto.ehr.BusinessPostDto;
import cn.net.yzl.crm.dto.ehr.PostDto;
import cn.net.yzl.crm.model.ProductMarketingQuality;
import cn.net.yzl.crm.model.StaffTalkQuality;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.micservice.QualityInspectionApi;

import cn.net.yzl.crm.utils.GetParamsValue;
import cn.net.yzl.crm.utils.ValidateUtils;
import cn.net.yzl.inspection.common.model.CallDurationQuality;
import cn.net.yzl.inspection.common.model.Quality;
import cn.net.yzl.inspection.common.model.WordQuality;
import cn.net.yzl.inspection.common.model.vo.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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

    @Autowired
    private EhrStaffClient ehrStaffClient;

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
        // 非空校验
        StringBuffer staffTalk = new StringBuffer("qualityDepartmentCode,qualityDepartmentName," +
                "qualityName,keyword,punishDescription" );
        Map<String, Object> vmap = ValidateUtils.allField(staffTalkQualityVo,staffTalk.toString());
        if (vmap.get("code") != null && (Integer.parseInt(vmap.get("code")+"")) == 0) {
            vmap = GetParamsValue.changeAllFieldResult(vmap,GetParamsValue.getStaffTalkMap());
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,vmap.toString());
        }
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
        // 非空校验
        if (StringUtils.isBlank(staffTalkCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "员工话术质检编号");
        }
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
        // 非空校验
        StringBuffer staffTalk = new StringBuffer("staffTalkCode,qualityDepartmentCode,qualityDepartmentName," +
                "qualityName,keyword,punishDescription" );
        Map<String, Object> vmap = ValidateUtils.allField(staffTalkQualityVo,staffTalk.toString());
        if (vmap.get("code") != null && (Integer.parseInt(vmap.get("code")+"")) == 0) {
            vmap = GetParamsValue.changeAllFieldResult(vmap,GetParamsValue.getStaffTalkMap());
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,vmap.toString());
        }
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
        // 非空校验
        if (StringUtils.isBlank(staffTalkCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "员工话术质检编号");
        }
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
        // 非空校验
        if (StringUtils.isBlank(staffTalkCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "员工话术质检编号");
        }
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
        // 非空校验
        StringBuffer productMarket = new StringBuffer("productCode,productName," +
                "marketingContent,keyword,punishDescription" );
        Map<String, Object> vmap = ValidateUtils.allField(productMarketingQualityVo,productMarket.toString());
        if (vmap.get("code") != null && (Integer.parseInt(vmap.get("code")+"")) == 0) {
            vmap = GetParamsValue.changeAllFieldResult(vmap,GetParamsValue.getProductMarketingMap());
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,vmap.toString());
        }
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

        // 非空校验
        StringBuffer productMarket = new StringBuffer("productMarketingCode,productCode,productName," +
                "marketingContent,keyword,punishDescription" );
        Map<String, Object> vmap = ValidateUtils.allField(productMarketingQualityVo,productMarket.toString());
        if (vmap.get("code") != null && (Integer.parseInt(vmap.get("code")+"")) == 0) {
            vmap = GetParamsValue.changeAllFieldResult(vmap,GetParamsValue.getProductMarketingMap());
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,vmap.toString());
        }
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
        // 非空校验
        if (StringUtils.isBlank(productMarketingCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "产品营销话术质检编号");
        }
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
        // 非空校验
        if (StringUtils.isBlank(productMarketingCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "产品营销话术质检编号");
        }
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
        // 非空校验
        StringBuffer productMarket = new StringBuffer("qualityDepartmentCode,qualityDepartmentName,prohibitedLevel," +
                "keyword,punishDescription" );
        Map<String, Object> vmap = ValidateUtils.allField(wordQualityVo,productMarket.toString());
        if (vmap.get("code") != null && (Integer.parseInt(vmap.get("code")+"")) == 0) {
            vmap = GetParamsValue.changeAllFieldResult(vmap,GetParamsValue.getWordQualityMap());
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,vmap.toString());
        }
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
        // 非空校验
        StringBuffer productMarket = new StringBuffer("wordCode,qualityDepartmentCode,qualityDepartmentName,prohibitedLevel," +
                "keyword,punishDescription" );
        Map<String, Object> vmap = ValidateUtils.allField(wordQualityVo,productMarket.toString());
        if (vmap.get("code") != null && (Integer.parseInt(vmap.get("code")+"")) == 0) {
            vmap = GetParamsValue.changeAllFieldResult(vmap,GetParamsValue.getWordQualityMap());
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,vmap.toString());
        }
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
        // 非空校验
        if (StringUtils.isBlank(wordCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "某个违禁词质检编号");
        }
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
        // 非空校验
        if (StringUtils.isBlank(wordCode)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "某个违禁词质检编号");
        }
        return qualityInspectionApi.updateWordQualityDisabled(wordCode);
    }

    /**
     * author: liufaguan
     * description: 质检列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return ComResponse<Page<Quality>>
     */
    @ApiOperation(value="质检列表",httpMethod = "POST")
    @PostMapping("/queryInspectionQualityList")
    ComResponse<Page<Quality>> queryInspectionQualityList(@RequestBody QualityVo qualityVo){return qualityInspectionApi.queryInspectionQualityList(qualityVo);}

    /**
     * author: liufaguan
     * description: 质检审核列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return ComResponse<Page<Quality>>
     */
    @ApiOperation(value="质检审核列表",httpMethod = "POST")
    @PostMapping("/queryInspectioncheckList")
    ComResponse<Page<Quality>> queryInspectioncheckList(@RequestBody QualityVo qualityVo){
        return qualityInspectionApi.queryInspectioncheckList(qualityVo);
    }

    /**
     * author: liufaguan
     * description: 质检申诉列表
     * create time: 2020/12/28 17:03
     * @Param: staffTalkQuality
     * @Param: pageParam
     * @return ComResponse<Page<Quality>>
     */
    @ApiOperation(value="质检申诉列表",httpMethod = "GET")
    @GetMapping("/queryInspectionAppealList")
    ComResponse<Page<Quality>> queryInspectionAppealList(@RequestParam("staffCode") String staffCode,@RequestParam("current") int current,@RequestParam("size") int size){
        return qualityInspectionApi.queryInspectionAppealList(staffCode,current,size);
    }




    /**
     * author: liufaguan
     * description: 下载质检列表
     * create time: 2021/1/12 20:13
     * @Param: info...
     * @return
     */
    @ApiOperation(value="下载质检列表",httpMethod = "GET")
    @GetMapping("/exportInspection")
    void exportInspection(@RequestParam("info") String info,@RequestParam("infoType") String infoType,
                     @RequestParam("departmentCode") String departmentCode,
                     @RequestParam("ehrDepartmentCode") String ehrDepartmentCode,
                     @RequestParam("qualityType") String qualityType,
                     @RequestParam("prohibitedLevel") String prohibitedLevel,
                     @RequestParam("qualityStatus") String qualityStatus,
                     HttpServletResponse response){
        qualityInspectionApi.exportInspection(info,infoType,departmentCode, ehrDepartmentCode, qualityType,
                prohibitedLevel,qualityStatus,response);
    }

    /**
     * author: liufaguan
     * description: 下载审核列表
     * create time: 2021/1/12 20:13
     * @Param: info...
     * @return
     */
    @ApiOperation(value="下载审核列表",httpMethod = "GET")
    @GetMapping("/exportCheck")
    public void exportCheckInspection(@RequestParam("info") String info,@RequestParam("infoType") String infoType,
                                      @RequestParam("departmentCode") String departmentCode,
                                      @RequestParam("ehrDepartmentCode") String ehrDepartmentCode,
                                      @RequestParam("qualityType") String qualityType,
                                      @RequestParam("prohibitedLevel") String prohibitedLevel,
                                      @RequestParam("qualityStatus") String checkStatus,
                                      HttpServletResponse response) {
        qualityInspectionApi.exportCheckInspection(info,infoType,departmentCode, ehrDepartmentCode, qualityType,
                prohibitedLevel,checkStatus,response);
    }

    /**
     * 查询通话时长质检设置
     */
    @ApiOperation(value="查询通话时长质检设置",httpMethod = "GET")
    @GetMapping(value = "/queryCallDurations")
    ComResponse<List<CallDurationQuality>> getBusiPostListByAttr(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode, @RequestParam("postId") Integer postId){
        if (bussinessAtrrCode!=null && postId!=null){
            ComResponse<List<BusinessPostDto>> posts = ehrStaffClient.getBusiPostListByAttr(bussinessAtrrCode, postId);
            if (posts!=null){
                List<Integer> leves = new ArrayList<>();
                for (BusinessPostDto ehrLeve: posts.getData()) {
                    leves.add(ehrLeve.getPostLevelId());

                }
                return qualityInspectionApi.queryCallDurations(bussinessAtrrCode,postId,leves);
            }

        }
        return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE,"缺少必填参数或参数值为null:" + "业务属性编号热线，回访或者岗位编号");
    }

    /**
     * 根据业务属性获取岗位列表
     */
    @ApiOperation(value="根据业务属性获取岗位列表",httpMethod = "GET")
    @GetMapping(value = "/getPostByBussinessAttrCode")
    ComResponse<List<PostDto>> getPostByBussinessAttrCode(@RequestParam("bussinessAtrrCode") Integer bussinessAtrrCode){
        return ehrStaffClient.getPostByBussinessAttrCode(bussinessAtrrCode);
    }

    /**
     * author: liufaguan
     * description: 新增通话时长质检设置
     * create time: 2021/1/4 10:55
     * @Param: callDurationQualityVos
     * @return ComResponse<Boolean>
     */
    @ApiOperation(value="更新通话时长质检设置",httpMethod = "POST")
    @PostMapping("/saveCallDuration")
    ComResponse<Boolean> saveCallDuration(@RequestBody ArrayList<CallDurationQualityVo> callDurationQualityVos){
        return qualityInspectionApi.saveCallDuration(callDurationQualityVos);
    }
}
