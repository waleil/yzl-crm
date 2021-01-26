package cn.net.yzl.crm.controller.order;

import cn.net.yzl.order.enums.BusinessType;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OutStoreWarningClient;
import cn.net.yzl.order.model.vo.order.OrderWarningOutStorePageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhouchangsong
 */
@RestController
@RequestMapping("orderWarningOutStore")
@Api(tags = "出库预警管理")
public class OrderWarningOutStoreController {
    @Autowired
    private OutStoreWarningClient outStoreWarningClient;

    @GetMapping("v1/getPageList")
    @ApiOperation("出库预警分页")
    public ComResponse<Page<OrderWarningOutStorePageDTO>> getPageList(
            @RequestParam @ApiParam(value = "当前页码", required = true) Integer pageNo,
            @RequestParam @ApiParam(value = "每页总数量", required = true) Integer pageSize,
            @RequestParam @ApiParam(value = "订单编号") String orderNo) {
        return this.outStoreWarningClient.getPageList(pageNo, pageSize, orderNo);
    }


    @GetMapping("v1/getByBusinessType")
    @ApiOperation(value = "查询预警配置", notes = "格式：{'delay':1,'noticeType':1}，delay预警时间，noticeType预警方式：1短信，2邮件，3短信+邮件")
    public ComResponse<JSONObject> getByBusinessType() {
        return outStoreWarningClient.getByBusinessType(BusinessType.ORDER_WARNING_OUT_STORE.getCode());
    }
	@PostMapping("v1/saveSysConfig")
	@ApiOperation(value = "添加出库预警配置", notes = "格式：{'delay':1,'noticeType':1}，delay预警时间，noticeType预警方式：1短信，2邮件，3短信+邮件")
	public ComResponse<Integer> saveSysConfig(@RequestParam @ApiParam(name = "配置信息", required = true) String businessValue){
    	return outStoreWarningClient.saveSysConfig(businessValue);
	}
}
