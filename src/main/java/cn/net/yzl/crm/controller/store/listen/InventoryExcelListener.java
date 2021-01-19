package cn.net.yzl.crm.controller.store.listen;

import cn.net.yzl.model.dto.InventoryProductDto;
import cn.net.yzl.model.vo.InventoryProductExcelVo;
import cn.net.yzl.model.vo.InventoryProductVo;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/1/18 21:42
 */
public class InventoryExcelListener extends AnalysisEventListener {

    private List<InventoryProductVo> inventoryProductExcelVoList = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext analysisContext) {
        InventoryProductVo inventoryProductDto = (InventoryProductVo) object;
        inventoryProductExcelVoList.add(inventoryProductDto);


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<InventoryProductVo> getInventoryProductExcelVoList(){
        return inventoryProductExcelVoList;
    }

}
