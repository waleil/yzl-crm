package cn.net.yzl.crm.client.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.model.vo.ProductStockExcelVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/22 11:09
 */
@FeignClient(name = "inventoryClient",url = "${api.gateway.url}/storeServer")
//@FeignClient("yzl-store-server")
public interface ProductStockFeignService {

    /**
     * 查询库存
     * @param codeAndName
     * @param storeNo
     * @return
     */
    @GetMapping("productStock/v1/selectProductStockExcel")
    ComResponse<List<ProductStockExcelVo>> selectProductStockExcel(@RequestParam("codeAndName") String codeAndName,@RequestParam("storeNo") String storeNo);
}
