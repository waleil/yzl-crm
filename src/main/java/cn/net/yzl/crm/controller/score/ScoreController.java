package cn.net.yzl.crm.controller.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.config.QueryIds;
import cn.net.yzl.crm.dto.staff.StaffImageBaseInfoDto;
import cn.net.yzl.crm.service.micservice.EhrStaffClient;
import cn.net.yzl.crm.service.score.ScoreService;
import cn.net.yzl.score.model.dto.*;
import cn.net.yzl.score.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("score/v1")
@Api(tags = "积分服务")
public class ScoreController {

    @Autowired
    private ScoreService service;

    @Autowired
    private FastDFSConfig fastDFSConfig;

    @Autowired
    private EhrStaffClient ehrClient;

    @GetMapping("myScore")
    @ApiOperation("【商城首页】查询我的积分")
    public ComResponse<Integer> myScore(){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        return service.myScore(userNo);
    }

    @GetMapping("myInfo")
    @ApiOperation("【商城首页】查询我的基本信息")
    public ComResponse<StaffImageBaseInfoDto> myInfo() {
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        return ehrClient.getDetailsByNo(userNo);
    }

    @GetMapping("pageDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query"),
            @ApiImplicitParam(name = "pageNo",value = "页码",paramType = "query"),

    })
    @ApiOperation("【商城首页】查询我的积分明细")
    public ComResponse<Page<MyScoreDetailDTO>> myExchangeRecords(@RequestParam("pageSize") Integer pageSize,
                                                                 @RequestParam("pageNo") Integer pageNo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        return service.myExchangeRecords(userNo, pageSize, pageNo);
    }

    @GetMapping("exchangeRecord")
    @ApiOperation("【商城首页】分页查询我的兑换明细")
    public ComResponse<Page<MyExchangeRecordDTO>> exchangeRecords(@RequestParam("pageSize")Integer pageSize,
                                                                  @RequestParam("pageNo")Integer pageNo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        return service.exchangeRecords(userNo,pageSize, pageNo);
    }

    @GetMapping("queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query"),
            @ApiImplicitParam(name = "pageNo",value = "页码",paramType = "query"),
            @ApiImplicitParam(name = "hide",value = "是否显示禁用信息",paramType = "query")
    })
    @ApiOperation("【商城首页】查询积分兑换商品列表")
    public ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(@RequestParam("pageSize")Integer pageSize,
                                                                @RequestParam("pageNo")Integer pageNo,
                                                                @RequestParam("hide") Boolean hide){
        return service.queryPage(pageSize, pageNo,hide);
    }

    @PostMapping("exchange")
    @ApiOperation("【商城首页】兑换商品")
    public ComResponse<Void> exchange(@RequestBody @Valid ExchangeVO vo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        vo.setStaffNo(userNo);
        return service.exchange(vo);
    }

    @PostMapping("changeStatus")
    @ApiOperation("【兑换商品管理】修改积分商品启用禁用状态")
    public ComResponse<Void> changeStatus(@RequestBody @Valid ChangeProductStatusVO vo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        vo.setStaffNo(userNo);
        return service.changeStatus(vo);
    }

    @GetMapping("queryDetail")
    @ApiImplicitParam(name = "id",value = "id",paramType = "query",required = true)
    @ApiOperation("【兑换商品管理】根据id查询积分兑换指定商品信息")
    public ComResponse<ScoreProductDetailDTO> queryDetail(@RequestParam("id")Integer id){
        if (id == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE, "id不能为空！");
        }
        ComResponse<ScoreProductDetailDTO> response = service.queryDetail(id);
        return response;
    }


    @PostMapping("edit")
    @ApiOperation("【兑换商品管理】编辑积分兑换商品信息")
    public ComResponse<Void> edit(@RequestBody @Valid ScoreProductVO vo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        vo.setStaffNo(userNo);
        return service.edit(vo);
    }

    @PostMapping("delete")
    @ApiOperation("【兑换商品管理】删除积分商品信息")
    public ComResponse<Void> delete(@RequestBody ProductDelVO vo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        vo.setStaffNo(userNo);
        return service.delete(vo);
    }

    @GetMapping("mainInfo")
    @ApiOperation("【员工积分管理】查询发放统计信息")
    public ComResponse<MainInfo> mainInfo(){
        return service.mainInfo();
    }

    @PostMapping("scoreManagePage")
    @ApiOperation("【员工积分管理】分页查询员工积分信息")
    ComResponse<Page<ScoreManageDTO>> scoreManagePage(@RequestBody ManageSelectVO vo){
        return service.scoreManagePage(vo);
    }

    @PostMapping("changeStaffScoreStatus")
    @ApiOperation("【员工积分管理】修改员工账户可用状态")
    public ComResponse<Void> changeScoreStaffStatus(@RequestBody @Valid DisableScoreVO vo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        vo.setUpdateCode(userNo);
        return service.changeScoreStaffStatus(vo);
    }


//    @ApiOperation(value = "积分商品文件上传", notes = "积分商品文件上传",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @RequestMapping(value = "uploadScoreProductFile", method = RequestMethod.POST)
//    public ComResponse<String> uploadScoreProductFile(@RequestParam(value = "file") MultipartFile file) {
//        return service.uploadScoreProductFile(file).setMessage(fastDFSConfig.getUrl());
//    }


//    @PostMapping
//    @ApiOperation("删除积分兑换商品信息")
//    public ComResponse<Void> delete(@RequestParam("id")Integer id,HttpServletRequest request){
//        return service.delete(id, request);
//    }


    @PostMapping("pageStaffScore")
    @ApiOperation("【员工兑换管理】分页查询员工兑换列表")
    public ComResponse<Page<ScoreStaffRecordDTO>> pageStaffScore(@RequestBody StaffExchangeSelectVO vo){
        return service.pageStaffScore(vo);
    }

    @PostMapping("grant")
    @ApiOperation("【员工兑换管理】发放")
    public ComResponse<Void> grant(@RequestBody @Valid GrantVO vo){
    	String userNo = QueryIds.userNo.get();
        if(StringUtils.isBlank(userNo)) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        vo.setStaffNo(userNo);
        return service.grant(vo);
    }

}
