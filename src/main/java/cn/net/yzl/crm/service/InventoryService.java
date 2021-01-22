package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.vo.ReadExcelInventoryProductVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/19 9:14
 */
public interface InventoryService {

    /**
     * 读取excel
     * @param readExcelInventoryProductVo
     * @return
     */
    ComResponse<Page<InventoryProductDto>> readExcelnventoryProduct(ReadExcelInventoryProductVo readExcelInventoryProductVo, MultipartFile file);
}