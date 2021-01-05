package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.ProductMarketingQuality;
import cn.net.yzl.crm.model.StaffTalkQuality;
import cn.net.yzl.crm.service.micservice.QualityInspectionApi;
import cn.net.yzl.crm.vo.ProductMarketingQualityVo;
import cn.net.yzl.crm.vo.StaffTalkQualityVo;
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
@RequestMapping("/qualityInspection")
public class QualityInspectionController {

    @Autowired
    private QualityInspectionApi qualityInspectionApi;

    /**
     * author: liufaguan
     * description: 产品营销话术质检导入
     * create time: 2020/12/25 15:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="产品营销话术质检导入",httpMethod = "POST")
    @PostMapping("/importProductMarketingQuality")
    ComResponse<Integer> importProductMarketingQuality(){
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
    @GetMapping("/queryProductMarketingQualityList")
    ComResponse<Page<ProductMarketingQuality>> queryProductMarketingQualityList(@RequestParam("current") Integer current, @RequestParam("size") Integer size){
       return qualityInspectionApi.queryProductMarketingQualityList( current, size);
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
     * description: 员工话术质检导入
     * create time: 2020/12/25 15:54
     * @Param: null
     * @return
     */
    @ApiOperation(value="员工话术质检导入",httpMethod = "POST")
    @PostMapping("/importStaffTalkQuality")
    public ComResponse<Integer> importStaffTalkQuality(){
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
    @GetMapping("/queryStaffTalkQualityList")
    public ComResponse<Page<StaffTalkQuality>> queryStaffTalkQualityList(@RequestParam("current") Integer current, @RequestParam("size") Integer size){
        return qualityInspectionApi.queryStaffTalkQualityList(current,size);
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
}
