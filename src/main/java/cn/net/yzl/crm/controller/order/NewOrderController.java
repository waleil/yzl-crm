package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.service.impl.order.listener.NewOrderListioner;
import cn.net.yzl.crm.service.micservice.MemberFien;
import cn.net.yzl.crm.service.order.INewOrderService;
import cn.net.yzl.crm.sys.BizException;
import cn.net.yzl.order.model.vo.order.NewOrderDTO;
//import cn.net.yzl.order.model.vo.order.NewOrderExcelInDTO;
import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("newOrdder")
@Api(tags = "订单管理")
public class NewOrderController {

    @Autowired
    private MemberFien memberFien;

    @Autowired
    private INewOrderService newOrderService;
    @ApiOperation(value = "新建订单（会刊）")
    @PostMapping("v1/newOrder")
    public ComResponse<Boolean> newOrder(@RequestBody NewOrderDTO dto) {
        ComResponse<Boolean> response = null;
        try{
            response = newOrderService.newOrder(dto);
        }catch (BizException e){
            throw new BizException(e.getCode(),e.getMessage());
        }
        return  response;
    }

    @ApiOperation(value = "新建订单页导出模板")
    @GetMapping("v1/dowloadnewOrderTemplet")
    public void dowloadTemplet(HttpServletResponse response) throws FileNotFoundException {

    }

    @ApiOperation(value = "新建订单页导入",httpMethod = "POST")
    @PostMapping(value = "v1/importNewOrderTemplet",headers = "content-type=multipart/form-data")
    public ComResponse<Boolean> importNewOrderTemplet( MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
//        EasyExcel.read(inputStream, NewOrderExcelInDTO.class, new NewOrderListioner(newOrderService)).sheet()
//                .headRowNumber(2).doRead();
        return ComResponse.success(true);
    }



    @ApiOperation(value = "查询群组列表",httpMethod = "POST")
    @PostMapping(value = "v1/getCrowdGroupByPaget")
    public ComResponse<Integer> getCrowdGroupByPaget(@RequestBody CrowdGroupDTO crowdGroupDTO) {

        return memberFien.getCrowdGroupByPage(crowdGroupDTO);
    }

}
