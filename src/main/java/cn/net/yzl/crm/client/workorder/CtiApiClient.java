package cn.net.yzl.crm.client.workorder;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.workorder.model.db.CallInfoBean;
import cn.net.yzl.workorder.model.dto.CticheckDTO;
import cn.net.yzl.workorder.model.vo.CallInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ctiApiClient", url = "${api.gateway.url}/workorderServer/api/call")
//@FeignClient(name = "ctiApiClient", url = "127.0.0.1:4602/api/call")
public interface CtiApiClient {

    /**
     * 场景：座席呼出电话，验证某座席是否可以给某个号码打电话
     * @param cticheckDTO
     * @return
     */
    @PostMapping("/v1/check")
    ComResponse<Boolean> check(CticheckDTO cticheckDTO);

    /**
     * 座席呼出回调接口
     * @param callInfoBean
     * @return
     */
    @PostMapping("/v1/callOutSuccess")
    ComResponse callOutSuccess(CallInfoBean callInfoBean);

    /**
     * 挂断修改通话记录
     * @param callInfoVo
     * @return
     */
    @PostMapping("/v1/hangUp")
    ComResponse hangUp(CallInfoVo callInfoVo);

    /**
     * 挂断修改通话记录
     * @param staffNo
     * @return
     */
    @GetMapping("/v1/checkCallCount")
    ComResponse checkCallCount(@RequestParam("staffNo") String staffNo,@RequestParam("workOrderType") Integer workOrderType);
}
