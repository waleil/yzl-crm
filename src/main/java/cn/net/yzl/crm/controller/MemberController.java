package cn.net.yzl.crm.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.MemberSerchDTO;
import cn.net.yzl.crm.dto.order.ListParamsDTO;
import cn.net.yzl.crm.model.Member;
import cn.net.yzl.crm.model.OrderMember;
import cn.net.yzl.crm.service.MemberService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "顾客管理")
@Slf4j
@RestController
@RequestMapping(value = MemberController.PATH)
public class MemberController {
    public static final String PATH = "member/product";
    @Autowired
    private MemberService memberService;

    @ApiOperation(value="分页查询顾客列表")
    @PostMapping("listPage")
    public GeneralResult<Page<Member>> listPage(@RequestBody MemberSerchDTO dto) {
        GeneralResult<Page<Member>> result = memberService.listPage(dto);
        return result;
    }

    @ApiOperation(value="顾客列表查询病症分类")
    @GetMapping("productClassi")
    public GeneralResult<List<Map<Integer,Object>>> productClassi() {
        List<Map<Integer, Object>> maps = memberService.productClassiService("0");
        return GeneralResult.success(maps);
    }
    @ApiOperation(value="顾客列表查询病症分类")
    @GetMapping("specific")
    public GeneralResult<List<Map<Integer,Object>>> specific(@RequestParam("pid") String pid) {
        List<Map<Integer, Object>> maps = memberService.productClassiService(pid);
        return GeneralResult.success(maps);
    }
}
