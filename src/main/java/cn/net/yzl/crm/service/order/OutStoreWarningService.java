package cn.net.yzl.crm.service.order;

import cn.net.yzl.common.entity.ComResponse;

/**
 * @author zhouchangsong
 */
public interface OutStoreWarningService {

    /**
     * 发送出库预警
     * @return
     */
    ComResponse<Boolean> sendOutStoreWarningMsg();
}
