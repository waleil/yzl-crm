package cn.net.yzl.crm.client.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import cn.net.yzl.score.model.dto.ScoreManageDTO;
import cn.net.yzl.score.model.vo.DisableScoreVO;
import cn.net.yzl.score.model.vo.ManageSelectVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "scoreDetailClient", url = "${api.gateway.url}/scoreServer/detail/v1")
//@FeignClient(name = "scoreDetailClient", url = "127.0.0.1:8765/detail/v1")
public interface ScoreDetailClient {

    /**
     * @description 根据员工编号查询员工积分明细
     * @author Majinbao
     * @date 2021/2/24 15:53
     */
    @GetMapping("queryPage")
    ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(@RequestParam("staffNo") String staffNo,
                                                                    @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam("pageNo") Integer pageNo);

    /**
     * @description 分页查询员工积分信息
     * @author Majinbao
     * @date 2021/3/2 10:35
     */
    @PostMapping("scoreManagePage")
    ComResponse<Page<ScoreManageDTO>> scoreManagePage(@RequestBody ManageSelectVO vo);

    @GetMapping("exchangeRecord")
    ComResponse<Page<MyExchangeRecordDTO>> exchangeRecords(@RequestParam("staffNo") String staffNo,
                                                           @RequestParam("pageSize") Integer pageSize,
                                                           @RequestParam("pageNo") Integer pageNo);

    @GetMapping("changeScoreStaffStatus")
    ComResponse<Void> changeScoreStaffStatus(DisableScoreVO vo);
}
