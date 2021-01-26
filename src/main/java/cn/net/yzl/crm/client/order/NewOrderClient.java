package cn.net.yzl.crm.client.order;

import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;

@FeignClient(name = "newOrder",url = "${api.gateway.url}/orderService/newOrdder")
public interface NewOrderClient {


    /**
     * 新建订单（会刊）
     */
    @PostMapping("v1/newOrder")
    public ComResponse<Boolean> newOrder(@RequestBody NewOrderDTO dto);


    /**
     * 新建订单页导出模板
     */
    @GetMapping("v1/dowloadnewOrderTemplet")
    public ComResponse<ResponseEntity<byte[]>> dowloadTemplet(HttpServletResponse response) ;


    /**
     * 新建订单页导入
     */
    @PostMapping(value = "v1/importNewOrderTemplet",headers = "content-type=multipart/form-data")
    public ComResponse<Boolean> importNewOrderTemplet( MultipartFile file);


    /**
     * 定时插入会刊订单
     */
    @GetMapping(value = "v1/oprOrderTemp")
    public ComResponse<Boolean> oprOrderTemp(  ) ;


    /**
     * 查询物流公司
     */
//    @GetMapping(value = "v1/selectExpressCompany")
//    public ComResponse<List<ExpressCompanyListDTO>> selectExpressCompany() ;


}
