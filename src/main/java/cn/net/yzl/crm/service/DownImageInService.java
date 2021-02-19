package cn.net.yzl.crm.service;

import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/20 23:14
 */
public interface DownImageInService {


    void exportProductStockExcel(String codeAndName, String storeNo, HttpServletResponse httpServletResponse) throws IOException;

    void exportExcelOfProductPurchaseWarn(ProductPurchaseWarnExcelVO productPurchaseWarnExcelVO,HttpServletResponse httpServletResponse) throws IOException;
}
