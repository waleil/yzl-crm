package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/20 23:14
 */
public interface DownImageInService {


    ComResponse<List<ProductStockExcelVo>> exportProductStockExcel(String codeAndName, String storeNo);
}
