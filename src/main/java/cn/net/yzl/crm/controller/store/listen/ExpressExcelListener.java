package cn.net.yzl.crm.controller.store.listen;

import cn.net.yzl.model.dto.express.ExpressImportModel;
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
public class ExpressExcelListener extends AnalysisEventListener {

    private List<ExpressImportModel> expressImportModels = new ArrayList<>();

    private Map<String,String> errorMessageMap = new HashMap<>();

    private  static Map<Integer,String> headInit = new HashMap<>();

    static {
        headInit.put(0,"发货码");
        headInit.put(1,"快递单号");
    }

    @Override
    public void invoke(Object object, AnalysisContext analysisContext) {
        ExpressImportModel expressImportModel = (ExpressImportModel) object;
        expressImportModels.add(expressImportModel);
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

    public List<ExpressImportModel> getExpressImportModels(){
        return expressImportModels;
    }


    public Map<String,String> getErrorMessageMap(){
        return errorMessageMap;
    }
}
