package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.vo.ProductPurchaseWarnExcelVO;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/20 23:14
 */
public interface DownImageInService {


    void exportProductStockExcel(String codeAndName, String storeNo, HttpServletResponse httpServletResponse) throws IOException;

    void exportExcelOfProductPurchaseWarn(ProductPurchaseWarnExcelVO productPurchaseWarnExcelVO,HttpServletResponse httpServletResponse);
}
