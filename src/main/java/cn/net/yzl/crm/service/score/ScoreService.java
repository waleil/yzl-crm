package cn.net.yzl.crm.service.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import cn.net.yzl.score.model.dto.ScoreManageDTO;
import cn.net.yzl.score.model.dto.ScoreProductDetailDTO;
import cn.net.yzl.score.model.dto.ScoreProductMainInfoDTO;
import cn.net.yzl.score.model.vo.*;

import javax.servlet.http.HttpServletRequest;

public interface ScoreService {
    ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(String staffNo, Integer pageSize, Integer pageNo);

//    ComResponse<String> uploadScoreProductFile(MultipartFile file);

    ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(Integer pageSize, Integer pageNo, Boolean hide);

    ComResponse<ScoreProductDetailDTO> queryDetail(Integer id);

    ComResponse<Void> edit(ScoreProductVO vo);

    ComResponse<Void> delete(ProductDelVO vo);

    ComResponse<Void> exchange(ExchangeVO vo);

    ComResponse<Integer> myScore(String staffNo);

    ComResponse<Void> changeStatus(ChangeProductStatusVO vo);

    ComResponse<Page<ScoreManageDTO>> scoreManagePage(ManageSelectVO vo);
}
