package cn.net.yzl.crm.client.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.product.ProductClient;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import cn.net.yzl.score.model.dto.ScoreProductDetailDTO;
import cn.net.yzl.score.model.dto.ScoreProductMainInfoDTO;
import cn.net.yzl.score.model.vo.ExchangeVO;
import cn.net.yzl.score.model.vo.ScoreProductVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@FeignClient(name = "scoreProductClient", url = "${api.gateway.url}/scoreServer/scoreProduct/v1")
public interface ScoreProductClient {

//    @RequestMapping(value = "uploadScoreProductFile", method = RequestMethod.POST)
//    ComResponse<String> uploadScoreProductFile(@RequestParam(value = "file") MultipartFile file);

    @GetMapping("queryPage")
    ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(@RequestParam("pageSize") Integer pageSize,
                                                         @RequestParam("pageNo") Integer pageNo,
                                                         @RequestParam("hide") Boolean hide);

    @GetMapping("queryDetail")
    ComResponse<ScoreProductDetailDTO> queryDetail(@RequestParam("id")Integer id);

    @PostMapping("edit")
    ComResponse<Void> edit(@RequestBody ScoreProductVO vo);

    @PostMapping("delete")
    ComResponse<Void> delete(@RequestParam("id") Integer id, @RequestParam("userNo") String userNo);

    @PostMapping("exchange")
    ComResponse<Void> exchange(@RequestBody ExchangeVO vo);

    @GetMapping
    @ApiOperation("根据员工编号查询积分")
    ComResponse<Integer> myScore(@RequestParam("staffNo") String staffNo);

}
