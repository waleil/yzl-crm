package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.ExpressCompanyListDTO;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("newOrdder")
@Api(tags = "订单管理")
public class NewOrderController {

    @ApiOperation(value = "新建订单（会刊）")
    @GetMapping("v1/newOrder")
    public ComResponse<Boolean> findAllNonCheckSettings(@RequestBody NewOrderDTO dto) {
        Boolean result = false;
       //todo
        return ComResponse.success(result);
    }

    @ApiOperation(value = "新建订单页导出模板")
    @GetMapping("v1/dowloadnewOrderTemplet")
    public void dowloadTemplet(HttpServletResponse response) throws FileNotFoundException {

    }

    @ApiOperation(value = "新建订单页导入",httpMethod = "POST")
    @PostMapping(value = "v1/importNewOrderTemplet",headers = "content-type=multipart/form-data")
    public ComResponse<Integer> importNewOrderTemplet( MultipartFile file) {
        Boolean result = false;
        //todo
        return ComResponse.success(1);
    }

    @ApiOperation(value = "查询物流公司",httpMethod = "Get")
    @PostMapping(value = "v1/selectExpressCompany",headers = "content-type=multipart/form-data")
    public ComResponse<List<ExpressCompanyListDTO>> selectExpressCompany(ExpressCompanyListDTO e) {
        //todo 此处待实现
        List<ExpressCompanyListDTO> list = new ArrayList<ExpressCompanyListDTO>();
        ExpressCompanyListDTO dto = new ExpressCompanyListDTO();
        dto.setCode("SF");
        dto.setName("顺丰");
        list.add(dto);
        return ComResponse.success(list);
    }

}
