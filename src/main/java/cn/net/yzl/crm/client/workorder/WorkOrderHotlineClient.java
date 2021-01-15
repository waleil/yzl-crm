package cn.net.yzl.crm.client.workorder;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.workorder.model.db.WorkOrderHotlineBean;
import cn.net.yzl.workorder.model.dto.FindWorkOrderHotlinePageListDTO;
import cn.net.yzl.workorder.model.dto.UpdateMoreAdjustDTO;
import cn.net.yzl.workorder.model.dto.UpdateRecyclingDTO;
import cn.net.yzl.workorder.model.dto.UpdateSingleAdjustDTO;
import cn.net.yzl.workorder.model.vo.FindWorkOrderHotlinePageListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *  工单规则配置相关功能
 */
@Service
@FeignClient(name = "WorkOrderHotline",url = "${api.gateway.url}/workorderServer/hotline")
//@FeignClient(name = "WorkOrderHotline",url = "127.0.0.1:4602/hotline")
public interface WorkOrderHotlineClient {

    /**
     *  热线工单：回收
     * @param updateRecyclingDTO
     * @return
     */
    @RequestMapping(value = "v1/updateRecycling",method = RequestMethod.POST)
    ComResponse<Void> updateRecycling(@RequestBody UpdateRecyclingDTO updateRecyclingDTO);


    /**
     * 热线工单：单数据调整
     * @param updateSingleAdjustDTO
     * @return
     */
    @RequestMapping(value = "v1/updateSingleAdjust",method = RequestMethod.POST)
    ComResponse<Void> updateSingleAdjust(@RequestBody UpdateSingleAdjustDTO updateSingleAdjustDTO);

    /**
     * 热线工单：多数据调整
     * @param updateMoreAdjustDTO
     * @return
     */
    @RequestMapping(value = "v1/updateMoreAdjust",method = RequestMethod.POST)
    ComResponse<Void> updateMoreAdjust(@RequestBody UpdateMoreAdjustDTO updateMoreAdjustDTO);

    /**
     * 热线工单：查询热线工单列表
     * @param findWorkOrderHotlinePageListDTO
     * @return
     */
    @RequestMapping(value = "v1/findWorkOrderHotlinePageList",method = RequestMethod.GET)
    ComResponse<Page<FindWorkOrderHotlinePageListVO>> findWorkOrderHotlinePageList(@SpringQueryMap FindWorkOrderHotlinePageListDTO findWorkOrderHotlinePageListDTO);
}
