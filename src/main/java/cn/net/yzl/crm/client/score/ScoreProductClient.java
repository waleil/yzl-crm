package cn.net.yzl.crm.client.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.ScoreProductDetailDTO;
import cn.net.yzl.score.model.dto.ScoreProductMainInfoDTO;
import cn.net.yzl.score.model.vo.ChangeProductStatusVO;
import cn.net.yzl.score.model.vo.ExchangeVO;
import cn.net.yzl.score.model.vo.ProductDelVO;
import cn.net.yzl.score.model.vo.ScoreProductVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "scoreProductClient", url = "${api.gateway.url}/scoreServer/scoreProduct/v1")
//@FeignClient(name = "scoreProductClient", url = "127.0.0.1:8765/scoreProduct/v1")
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
    ComResponse<Void> delete(@RequestBody ProductDelVO vo);

    @PostMapping("exchange")
    ComResponse<Void> exchange(@RequestBody ExchangeVO vo);

    @GetMapping("myScore")
    ComResponse<Integer> myScore(@RequestParam("staffNo") String staffNo);

    @PostMapping("changeStatus")
    ComResponse<Void> changeStatus(@RequestBody ChangeProductStatusVO vo);

}
