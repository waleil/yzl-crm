package cn.net.yzl.crm.client.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "scoreDetailClient", url = "localhost:8765/detail/v1")
public interface ScoreDetailClient {

    /**
     * @description 根据员工编号查询员工积分明细
     * @author Majinbao
     * @date 2021/2/24 15:53
     */
    @GetMapping
    ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(@RequestParam("staffNo") String staffNo,
                                                                    @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam("pageNo") Integer pageNo);
}
