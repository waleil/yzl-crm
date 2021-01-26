package cn.net.yzl.crm.controller.member;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateHelper;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.dto.member.MemberCrowdGroupDTO;
import cn.net.yzl.crm.model.customer.UpdateCrowdStatus;
import cn.net.yzl.crm.service.micservice.MemberGroupFeign;
import cn.net.yzl.crm.service.micservice.member.MemberPhoneFien;
import cn.net.yzl.crm.sys.BizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 顾客手机号服务
 * wangzhe
 * 2021-01-26
 */
@Api(tags = "顾客手机号服务")
@RestController(value = "memberPhone")
public class MemberPhoneController {
    @Autowired
    private MemberPhoneFien memberPhoneFien;

    @ApiOperation("顾客手机号服务")
    @PostMapping("/v1/getMemberCard")
    public ComResponse<String> getMemberCard(
            @RequestParam("phoneNumber")
            @NotBlank(message = "phoneNumber不能为空")
            @ApiParam(name = "phoneNumber", value = "电话号码", required = true)
                    String phoneNumber
    ) {
        if (StringUtil.isNullOrEmpty(phoneNumber)) throw new BizException(ResponseCodeEnums.PARAMS_EMPTY_ERROR_CODE);
        ComResponse<String> result = memberPhoneFien.getMemberCard(phoneNumber);
        return result;
    }

}