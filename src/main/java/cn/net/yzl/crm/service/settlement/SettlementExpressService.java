package cn.net.yzl.crm.service.settlement;

import cn.net.yzl.logistics.settleexpresscharge.SettleBillSearchVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SettlementExpressService {

    void exportSettleExcel(SettleBillSearchVo searchVo, HttpServletResponse response) throws IOException;

}
