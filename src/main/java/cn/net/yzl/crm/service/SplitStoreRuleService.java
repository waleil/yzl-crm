package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.dto.order.SplitStoreRulePageDTO;

/**
 * @author zhouchangsong
 */
public interface SplitStoreRuleService {

    /**
     * 订单分仓规则
     * @param pageSize
     * @param pageNo
     * @return
     */
    ComResponse<Page<SplitStoreRulePageDTO>> getSplitStoreRuleList(Integer pageSize,Integer pageNo);
}
