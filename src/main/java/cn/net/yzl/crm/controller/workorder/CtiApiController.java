package cn.net.yzl.crm.controller.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.workorder.CtiApiClient;
import cn.net.yzl.workorder.model.db.CallInfoBean;
import cn.net.yzl.workorder.model.dto.CticheckDTO;
import cn.net.yzl.workorder.model.vo.CallInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/call")
@Api(tags = "和CTI交互的相关接口")
public class CtiApiController {

    @Autowired
    CtiApiClient ctiApiClient;

    @PostMapping("/v1/check")
    @ApiOperation(value = "场景：座席呼出电话，验证某座席是否可以给某个号码打电话")
    public ComResponse<Boolean> check(@Validated @RequestBody CticheckDTO cticheckDTO){
        return ctiApiClient.check(cticheckDTO);
    }

    @PostMapping("/v1/callOutSuccess")
    @ApiOperation(value = "座席呼出回调-通话记录新增")
    public ComResponse callOutSuccess(@RequestBody CallInfoBean callInfoBean){
        return ctiApiClient.callOutSuccess(callInfoBean);
    }

    @PostMapping("/v1/hangUp")
    @ApiOperation(value = "挂断-通话记录修改")
    public ComResponse hangUp(@RequestBody CallInfoVo callInfoVo){
        return ctiApiClient.hangUp(callInfoVo);
    }

}
