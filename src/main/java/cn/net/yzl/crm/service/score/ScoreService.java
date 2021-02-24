package cn.net.yzl.crm.service.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import cn.net.yzl.score.model.dto.ScoreProductDetailDTO;
import cn.net.yzl.score.model.dto.ScoreProductMainInfoDTO;
import cn.net.yzl.score.model.vo.ScoreProductVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ScoreService {
    ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(String staffNo, Integer pageSize, Integer pageNo);

//    ComResponse<String> uploadScoreProductFile(MultipartFile file);

    ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(Integer pageSize, Integer pageNo);

    ComResponse<ScoreProductDetailDTO> queryDetail(Integer id);

    ComResponse<Void> edit(ScoreProductVO vo);

    ComResponse<Void> delete(Integer id, HttpServletRequest request);
}
