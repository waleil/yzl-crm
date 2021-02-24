package cn.net.yzl.crm.controller.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.service.score.ScoreService;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import cn.net.yzl.score.model.dto.ScoreProductDetailDTO;
import cn.net.yzl.score.model.dto.ScoreProductMainInfoDTO;
import cn.net.yzl.score.model.vo.ScoreProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("score")
@Api(tags = "积分服务")
public class ScoreController {

    @Autowired
    private ScoreService service;

    @GetMapping("pageDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query"),
            @ApiImplicitParam(name = "pageNo",value = "页码",paramType = "query")
    })
    @ApiOperation("根据员工编号分页查询员工积分明细")
    public ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(@RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam("pageNo") Integer pageNo, HttpServletRequest request){
        if(StringUtils.isBlank(request.getHeader("userNo"))) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        return service.myExchangeRecords(request.getHeader("userNo"), pageSize, pageNo);
    }


    @ApiOperation(value = "积分商品文件上传", notes = "积分商品文件上传",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping(value = "uploadScoreProductFile", method = RequestMethod.POST)
    public ComResponse<String> uploadScoreProductFile(@RequestParam(value = "file") MultipartFile file) {
        return service.uploadScoreProductFile(file);
    }


    @GetMapping("queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query"),
            @ApiImplicitParam(name = "pageNo",value = "页码",paramType = "query")
    })
    @ApiOperation("分页查询积分兑换商品总览")
    public ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo){
        return service.queryPage(pageSize, pageNo);
    }

    @GetMapping("queryDetail")
    @ApiImplicitParam(name = "id",value = "id",paramType = "query")
    @ApiOperation("根据id查询积分兑换指定商品明细")
    public ComResponse<ScoreProductDetailDTO> queryDetail(@RequestParam("id")Integer id){
        return service.queryDetail(id);
    }

    @PostMapping("edit")
    @ApiOperation("编辑积分兑换商品信息")
    public ComResponse<Void> edit(@RequestBody @Valid ScoreProductVO vo,HttpServletRequest request){
        if(StringUtils.isBlank(request.getHeader("userNo"))) {
            return ComResponse.fail(ResponseCodeEnums.LOGIN_ERROR_CODE.getCode(),"验证用户信息失败，请尝试重新登陆！");
        }
        vo.setStaffNo(request.getHeader("userNo"));
        return service.edit(vo);
    }

}
