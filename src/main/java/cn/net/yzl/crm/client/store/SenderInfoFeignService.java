package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.SenderInfoDTO;
import cn.net.yzl.model.vo.SenderInfoEditVO;
import cn.net.yzl.model.vo.SenderInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wujianing
 * @version 1.0
 * @date: 2021/1/29 19:43
 * @title: SenderInfoFeignService
 * @description:
 */
@FeignClient(name = "senderInfoClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface SenderInfoFeignService {

    @PostMapping(value = "senderInfo/v1/selectSenderInfo")
    @ApiOperation("查询发货方信息")
    public ComResponse<List<SenderInfoDTO>> selectSenderInfo();

    @PostMapping(value = "senderInfo/v1/selectSenderInfoList")
    @ApiOperation("发货方列表查询")
    public ComResponse<Page<SenderInfoDTO>> selectSenderInfoList(@RequestBody SenderInfoVO vo);

    @GetMapping(value = "senderInfo/v1/selectSenderInfoDetail")
    @ApiOperation("发货方信息详情")
    public ComResponse<SenderInfoDTO> selectSenderInfoDetail(@RequestParam("id") Integer id);

    @PostMapping(value = "senderInfo/v1/insertSenderInfo")
    @ApiOperation("发货方信息新增")
    public ComResponse insertSenderInfo(@RequestBody SenderInfoEditVO vo);

    @PostMapping(value = "senderInfo/v1/updateSenderInfo")
    @ApiOperation("发货方信息修改")
    public ComResponse updateSenderInfo(@RequestBody SenderInfoEditVO vo);

    @GetMapping(value = "senderInfo/v1/updateSenderInfoStatus")
    @ApiOperation("修改状态")
    public ComResponse updateSenderInfoStatus(@RequestParam("id") Integer id);

}
