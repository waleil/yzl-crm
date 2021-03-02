package cn.net.yzl.crm.service.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.*;
import cn.net.yzl.score.model.vo.*;

import javax.servlet.http.HttpServletRequest;

public interface ScoreService {
    ComResponse<Page<MyScoreDetailDTO>> myExchangeRecords(String staffNo, Integer pageSize, Integer pageNo);

//    ComResponse<String> uploadScoreProductFile(MultipartFile file);

    ComResponse<Page<ScoreProductMainInfoDTO>> queryPage(Integer pageSize, Integer pageNo, Boolean hide);

    ComResponse<ScoreProductDetailDTO> queryDetail(Integer id);

    ComResponse<Void> edit(ScoreProductVO vo);

    ComResponse<Void> delete(ProductDelVO vo);

    ComResponse<Void> exchange(ExchangeVO vo);

    ComResponse<Integer> myScore(String staffNo);

    ComResponse<Void> changeStatus(ChangeProductStatusVO vo);

    ComResponse<Page<ScoreManageDTO>> scoreManagePage(ManageSelectVO vo);

    ComResponse<Page<MyExchangeRecordDTO>> exchangeRecords(String userNo, Integer pageSize, Integer pageNo);

    ComResponse<Void> changeScoreStaffStatus(DisableScoreVO vo);
}
