package cn.net.yzl.crm.service.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;

public interface ScoreService {
    ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(String staffNo, Integer pageSize, Integer pageNo);
}
