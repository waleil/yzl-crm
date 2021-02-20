package cn.net.yzl.crm.controller.store.listen;

import cn.net.yzl.model.vo.InventoryProductVo;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.util.CollectionUtils;

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

    private Map<String,String> errorMessageMap = new HashMap<>();

    private  static Map<Integer,String> headInit = new HashMap<>();

    static {
        headInit.put(0,"商品id");
        headInit.put(1,"商品编码");
        headInit.put(2,"商品名称");
        headInit.put(3,"规格");
        headInit.put(4,"生产批次");
        headInit.put(5,"包装单位");
        headInit.put(6,"库存量");
        headInit.put(7,"实际数量");
    }

    @Override
    public void invoke(Object object, AnalysisContext analysisContext) {
        InventoryProductVo inventoryProductDto = (InventoryProductVo) object;
        inventoryProductExcelVoList.add(inventoryProductDto);
    }

    @Override
    public void invokeHeadMap(Map headMap, AnalysisContext context) {
        if (CollectionUtils.isEmpty(headMap)){
            errorMessageMap.put("msg","excel表头信息错误");
            return;
        }
        Map<Integer,String> headMap2= (Map<Integer,String>)headMap;

        for (Integer integer : headMap2.keySet()) {
            String headExcel = headMap2.get(integer);
            String head = headInit.get(integer);
            if (!head.equals(headExcel)){
                errorMessageMap.put("msg","第"+integer+1+"列表头应该为:"+head+",实际表头为:"+headExcel);
                return;
            }
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<InventoryProductVo> getInventoryProductExcelVoList(){
        return inventoryProductExcelVoList;
    }


    public Map<String,String> getErrorMessageMap(){
        return errorMessageMap;
    }
}
