package cn.net.yzl.crm.client.score;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.ScoreProductDetailDTO;
import cn.net.yzl.score.model.dto.ScoreProductMainInfoDTO;
import cn.net.yzl.score.model.vo.ScoreProductVO;

@FeignClient(name = "scoreProductClient", url = "${api.gateway.url}/scoreServer/scoreProduct/v1")
public interface ScoreProductClient {

//    @RequestMapping(value = "uploadScoreProductFile", method = RequestMethod.POST)
//    ComResponse<String> uploadScoreProductFile(@RequestParam(value = "file") MultipartFile file);

    @GetMapping("queryPage")
    ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(@RequestParam("pageSize")Integer pageSize, @RequestParam("pageNo")Integer pageNo);

    @GetMapping("queryDetail")
    ComResponse<ScoreProductDetailDTO> queryDetail(@RequestParam("id")Integer id);

    @PostMapping("edit")
    ComResponse<Void> edit(@RequestBody @Valid ScoreProductVO vo);

    @PostMapping("delete")
    ComResponse<Void> delete(@RequestParam Integer id,@RequestParam String userNo);
}
