package cn.net.yzl.crm.controller.score;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.service.score.ScoreService;
import cn.net.yzl.score.model.dto.MyExchangeRecordDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("score")
@Api(tags = "积分服务")
public class ScoreController {

    @Autowired
    private ScoreService service;

    @GetMapping("pageDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffNo",value = "员工编号",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query"),
            @ApiImplicitParam(name = "pageNo",value = "页码",paramType = "query")
    })
    @ApiOperation("根据员工编号分页查询员工积分明细")
    public ComResponse<Page<MyExchangeRecordDTO>> myExchangeRecords(@RequestParam("staffNo") String staffNo,
                                                                    @RequestParam("pageSize") Integer pageSize,
                                                                    @RequestParam("pageNo") Integer pageNo){
        return service.myExchangeRecords(staffNo, pageSize, pageNo);
    }


}
