package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/22 11:09
 */
public interface ProductStockFeign {

    /**
     * 查询库存
     * @param codeAndName
     * @param storeNo
     * @return
     */
    @GetMapping("v1/selectProductStockExcel")
    ComResponse<List<ProductStockExcelVo>> selectProductStockExcel(String codeAndName, String storeNo);
}
