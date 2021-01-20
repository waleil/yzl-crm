package cn.net.yzl.crm.controller.logistics;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.service.micservice.LogisticsFien;
import cn.net.yzl.logistics.model.vo.logistics.ExpressCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("logistics")
@Api(tags = "物流信息")
public class LogisticsController {

    @Autowired
    LogisticsFien logisticsFien;
    @ApiOperation("物流快递追踪轨迹信息")
    @GetMapping ("v1/selectExpressComponyCode")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyCode() {
        return logisticsFien.selectExpressComponyCode();
    }

    @ApiOperation("物流快递公司信息")
    @GetMapping("v1/selectExpressComponyDetail")
    public ComResponse<List<ExpressCodeVo>> selectExpressComponyDetail() {
        return logisticsFien.selectExpressComponyDetail();
    }
}
