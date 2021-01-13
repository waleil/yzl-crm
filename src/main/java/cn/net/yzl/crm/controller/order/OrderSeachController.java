package cn.net.yzl.crm.controller.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.order.OrderSearchClient;
import cn.net.yzl.crm.model.Advisor;
import cn.net.yzl.crm.model.Media;
import cn.net.yzl.order.model.vo.order.OderListReqDTO;
import cn.net.yzl.order.model.vo.order.OderListResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("orderSearch")
@Api(tags = "订单管理")
public class OrderSeachController {

    @Autowired
    private OrderSearchClient orderSearchClient;

    @ApiOperation(value = "查询订单列表")
    @PostMapping("v1/selectOrderList")
    public ComResponse<Page<OderListResDTO>> selectOrderList(@RequestBody OderListReqDTO dto) {
        ComResponse<Page<OderListResDTO>> list = orderSearchClient.selectOrderList(dto);
        return list;
    }
    @ApiOperation(value = "查询媒介名称下拉类表")
    @GetMapping("v1/selectMedias")
    public ComResponse<List<Media>> selectMedias() {
        List<Media> list = new ArrayList<>();
        //todo  查询媒介名称
        Media media = new Media();
        media.setBusNo("1001");
        media.setBusNo("陕西卫视");
        Media media1 = new Media();
        media1.setBusNo("5001");
        media1.setBusNo("御芝林");
        list.add(media);
        list.add(media1);
        return ComResponse.success(list);
    }

    @ApiOperation(value = "查询广告列表")
    @GetMapping("v1/selectAdvisors")
    public ComResponse<List<Advisor>> selectAdvisors() {
        List<Advisor> list = new ArrayList<>();
        //todo  查询媒介名称
        Advisor advisor = new Advisor();
        advisor.setAdvisorNo("1");
        advisor.setAdvisorName("真情剧场二集剧后");
        list.add(advisor);
        Advisor advisor2 = new Advisor();
        advisor2.setAdvisorNo("4");
        advisor2.setAdvisorName("真情剧场二集剧后");
        list.add(advisor2);
        return ComResponse.success(list);
    }

}
