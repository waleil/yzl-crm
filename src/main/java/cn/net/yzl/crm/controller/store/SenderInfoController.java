package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.store.SenderInfoFeignService;
import cn.net.yzl.model.dto.SenderInfoDTO;
import cn.net.yzl.model.vo.SenderInfoEditVO;
import cn.net.yzl.model.vo.SenderInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wujianing
 * @version 1.0
 * @date: 2021/1/29 19:59
 * @title: SenderInfoController
 * @description:
 */
@RestController
@Slf4j
@Api(value = "仓储中心心心心心-发货人管理", tags = {"仓储中心心心心心-发货人管理"})
@RequestMapping("senderInfo")
public class SenderInfoController {

    @Autowired
    private SenderInfoFeignService senderInfoFeignService;

    @PostMapping(value = "v1/selectSenderInfo")
    @ApiOperation("查询发货方信息")
    public ComResponse<List<SenderInfoDTO>> selectSenderInfo(){
        return senderInfoFeignService.selectSenderInfo();
    }

    @PostMapping(value = "v1/selectSenderInfoList")
    @ApiOperation("发货方列表查询")
    public ComResponse<Page<SenderInfoDTO>> selectSenderInfoList(@RequestBody SenderInfoVO vo){
        return senderInfoFeignService.selectSenderInfoList(vo);
    }

    @GetMapping(value = "v1/selectSenderInfoDetail")
    @ApiOperation("发货方信息详情")
    public ComResponse<SenderInfoDTO> selectSenderInfoDetail(@RequestParam("id") Integer id){
        return senderInfoFeignService.selectSenderInfoDetail(id);
    }

    @PostMapping(value = "v1/insertSenderInfo")
    @ApiOperation("发货方信息新增")
    public ComResponse insertSenderInfo(@RequestBody SenderInfoEditVO vo){
        return senderInfoFeignService.insertSenderInfo(vo);
    }

    @PostMapping(value = "v1/updateSenderInfo")
    @ApiOperation("发货方信息修改")
    public ComResponse updateSenderInfo(@RequestBody SenderInfoEditVO vo){
        return senderInfoFeignService.updateSenderInfo(vo);
    }

    @GetMapping(value = "v1/updateSenderInfoStatus")
    @ApiOperation("修改状态")
    public ComResponse updateSenderInfoStatus(@RequestParam("id") Integer id){
        return senderInfoFeignService.updateSenderInfoStatus(id);
    }

}
