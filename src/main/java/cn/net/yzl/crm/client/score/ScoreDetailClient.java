package cn.net.yzl.crm.client.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.*;
import cn.net.yzl.score.model.vo.DisableScoreVO;
import cn.net.yzl.score.model.vo.GrantVO;
import cn.net.yzl.score.model.vo.ManageSelectVO;
import cn.net.yzl.score.model.vo.StaffExchangeSelectVO;
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
    ComResponse<Page<MyScoreDetailDTO>> myExchangeRecords(@RequestParam("staffNo") String staffNo,
                                                          @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam("pageNo") Integer pageNo);

    /**
     * @description 分页查询员工积分信息
     * @author Majinbao
     * @date 2021/3/2 10:35
     */
    @PostMapping("scoreManagePage")
    ComResponse<Page<ScoreManageDTO>> scoreManagePage(@RequestBody ManageSelectVO vo);

    /**
     * @description 兑换记录
     * @author Majinbao
     * @date 2021/3/3 20:53
     */
    @GetMapping("exchangeRecord")
    ComResponse<Page<MyExchangeRecordDTO>> exchangeRecords(@RequestParam("staffNo") String staffNo,
                                                           @RequestParam("pageSize") Integer pageSize,
                                                           @RequestParam("pageNo") Integer pageNo);

    /**
     * @description 冻结积分/解冻积分
     * @author Majinbao
     * @date 2021/3/3 20:53
     */
    @GetMapping("changeStaffScoreStatus")
    ComResponse<Void> changeStaffScoreStatus(DisableScoreVO vo);

    /**
     * @description 分页查询员工积分信息
     * @author Majinbao
     * @date 2021/3/3 20:53
     */
    @PostMapping("pageStaffScore")
    ComResponse<Page<ScoreStaffRecordDTO>> pageStaffScore(@RequestBody StaffExchangeSelectVO vo);

    /**
     * @description 发放
     * @author Majinbao
     * @date 2021/3/3 20:53
     */
    @PostMapping("grant")
    ComResponse<Void> grant(@RequestBody GrantVO vo);

    @GetMapping("mainInfo")
    @ApiOperation("查询发放统计信息")
    ComResponse<MainInfo> mainInfo();

}
