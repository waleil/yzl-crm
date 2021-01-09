package cn.net.yzl.crm.controller.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.service.product.AttributeService;
import cn.net.yzl.product.model.db.AttributeBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/product/attribute")
@Api(tags = "属性管理", description = "包含：增删改查")
public class AttributeController {


    @Autowired
    private AttributeService attributeService;

    @ApiOperation(value = "添加属性")
    @PostMapping("insertAttribute")
    public ComResponse insertProductAttribute(@NotNull(message = "数据不能为空！")@RequestBody AttributeBean attributeBean) {
        if (attributeBean.getId() == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), ResponseCodeEnums.SYSTEM_ERROR_CODE.getMessage());
        }else {
            return attributeService.insertProductAttribute(attributeBean);
        }
    }

    @ApiOperation(value = "通过页码和每页条数查询属性")
    @GetMapping("selectPageAttribute")
    public ComResponse selectPageAttribute(@RequestParam("pageNo") @NotNull(message = "页码信息不能为空！")  int pageNo, @RequestParam("pageSize") @NotNull(message = "每页条数不能为空！") int pageSize) {
        if (pageSize>50) {
            pageSize=50;
        }
        return attributeService.selectPageAttribute(pageNo,pageSize);
    }
    @ApiOperation(value = "通过id查询属性")
    @GetMapping("selectById")
    public ComResponse selectById(@RequestParam("id") @NotNull(message = "id不能为空！") Integer id) {
        ComResponse comResponse = attributeService.selectById(id);
        if (comResponse.getData() == null) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "通过分类查询属性")
    @GetMapping("selectByclassifyIdAttribute")
    public ComResponse selectByclassifyIdAttribute(@RequestParam("id") @ApiParam("分类id") @NotNull(message = "id不能为空！") Integer id) {
        ComResponse comResponse = attributeService.selectByclassifyIdAttribute(id);
        List list = new ArrayList<>();
        if (comResponse.getData() == null||(list = (List) comResponse.getData()).size() == 0) {
            return ComResponse.nodata();
        }
        return comResponse;
    }

    @ApiOperation(value = "修改属性")
    @PostMapping("updateAttribute")
    public ComResponse updateAttribute(@NotNull(message = "数据不能为空！")@RequestBody AttributeBean attributeBean) {
        return attributeService.updateAttribute(attributeBean);
    }

}
