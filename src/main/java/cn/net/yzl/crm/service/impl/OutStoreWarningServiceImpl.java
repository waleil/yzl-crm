package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.dao.OutStoreWarningMapper;
import cn.net.yzl.crm.service.order.OutStoreWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhouchangsong
 */
@Service
public class OutStoreWarningServiceImpl implements OutStoreWarningService {

    @Autowired
    private OutStoreWarningMapper outStoreWarningMapper;

    @Override
    public ComResponse<Boolean> sendOutStoreWarningMsg() {
        return null;
    }
}
