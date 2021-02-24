package cn.net.yzl.crm.service.score.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.client.score.ScoreDetailClient;
import cn.net.yzl.crm.client.score.ScoreProductClient;
import cn.net.yzl.crm.service.score.ScoreService;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreDetailClient client;

    @Override
    public ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(String staffNo, Integer pageSize, Integer pageNo) {
        return StringUtils.isEmpty(staffNo)?
                ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"用户id不能为空！")
                :client.myExchangeRecords(staffNo, pageSize==null?10:pageSize, pageNo==null?1:pageNo);
    }

}
