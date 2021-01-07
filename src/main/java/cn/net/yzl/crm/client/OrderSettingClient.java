package cn.net.yzl.crm.client;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingDTO;
import cn.net.yzl.order.model.vo.order.OrderCheckSettingProduct;
import cn.net.yzl.order.model.vo.order.UpdateOrderCheckSettingDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
@Service
@FeignClient(name = "orderSettingClient",url = "http://api.staff.yuzhilin.net.cn/orderService/orderCheckSetting")
public interface OrderSettingClient {

    /**
     * 查询所有免审配置
     * @param enableFlag 0通用 1启用
     * @return
     */
    @GetMapping("v1/findAllNonCheckSettings")
    public ComResponse<List<OrderCheckSettingDTO>> findAllNonCheckSettings(Integer enableFlag);


    /**
     * 删除免审配置关联产品
     * @return
     */
    @RequestMapping(path="v1/deleteNonCheckSettings",method = RequestMethod.POST)
    public ComResponse<Boolean> deleteNonCheckSettings(@RequestBody @Valid UpdateOrderCheckSettingDTO dto);


    /**
     * 新增规则关联商品
     * @param dto
     * @return
     */
    @RequestMapping(path="v1/createNonCheckSettings",method = RequestMethod.POST)
    public ComResponse<Boolean> createNonCheckSettings(@RequestBody @Valid UpdateOrderCheckSettingDTO dto);

    /**
     * 更新免审配置
     * @param dto
     * @return
     */
    @RequestMapping(path="v1/updateNonCheckSettings",method = RequestMethod.POST)
    public ComResponse<Boolean> updateNonCheckSettings(@RequestBody @Valid OrderCheckSettingDTO dto) ;

    /**
     * 查询免审规则已选商品
     * @param settingType 配置类型
     * @return
     */
    @RequestMapping(path="v1/selectSettingedProducts",method = RequestMethod.POST)
    public ComResponse<Page<OrderCheckSettingProduct>> selectSettingedProducts(@RequestParam(required = false,defaultValue = "1",value = "pageNo")Integer pageNo,
                                                                               @RequestParam(required = false,defaultValue = "15",value = "pageSize")  Integer pageSize,
                                                                               @NotBlank(message="免审类型不能为空")   Integer settingType) ;



}
