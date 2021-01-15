package cn.net.yzl.crm.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.model.LabelType;
import cn.net.yzl.crm.service.LabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "标签管理")
@Slf4j
@RestController
@RequestMapping("/api/label")
public class LabelController {

    @Autowired
    LabelService labelService;

    @ApiOperation(value="获取标签类型接口")
    @GetMapping("getLabelTypes")
    public ComResponse getLabelTypes(){
        List<LabelType> labelTypeList=labelService.getLabelTypes();

        return new ComResponse().setData(labelTypeList).setCode(ComResponse.SUCCESS_STATUS);
    }

}
