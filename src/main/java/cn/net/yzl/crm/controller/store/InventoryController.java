package cn.net.yzl.crm.controller.store;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.controller.store.listen.InventoryExcelListener;
import cn.net.yzl.crm.service.InventoryService;
import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.vo.InventoryProductExcelVo;
import cn.net.yzl.model.vo.ReadExcelInventoryProductVo;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/18 23:11
 */
@RestController
@Api
@RequestMapping("inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @ApiOperation(value = "导入查看盘点商品",notes = "导入查看盘点商品信息",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping("v1/readExcelnventoryProduct")
    public ComResponse<Page<InventoryProductDto>> readExcelnventoryProduct(ReadExcelInventoryProductVo readExcelInventoryProductVo) throws IOException {
      return inventoryService.readExcelnventoryProduct(readExcelInventoryProductVo);
    }

}
