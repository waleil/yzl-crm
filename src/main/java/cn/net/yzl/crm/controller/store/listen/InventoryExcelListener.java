package cn.net.yzl.crm.controller.store.listen;

import cn.net.yzl.model.vo.InventoryProductVo;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

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
