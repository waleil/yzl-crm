package cn.net.yzl.crm.service.impl.order.listener;

import cn.hutool.json.JSONUtil;
import cn.net.yzl.crm.service.order.IExcelService;
import cn.net.yzl.crm.service.order.INewOrderService;
//import cn.net.yzl.order.model.vo.order.NewOrderExcelInDTO;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.extension.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @ Description：新建会刊订单监听
 * @Version: 1
 */
public class NewOrderListioner  {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewOrderListioner.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<Object> list = new ArrayList<Object>();
    /**
     *
     */
    private IExcelService defaultService;

    /**
     * 传入处理类
     *
     * @param service
     */
    public NewOrderListioner(IExcelService service) {
        this.defaultService = service;
    }


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext}
     * @param context
     */
//    @Override
//    public void invoke(NewOrderExcelInDTO data, AnalysisContext context) {
//        LOGGER.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
//
//        list.add(data);
//        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (list.size() >= BATCH_COUNT) {
//            saveData();
//            // 存储完成清理 list
//            list.clear();
//        }
//    }



    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext context) {
//        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//        saveData();
//        LOGGER.info("所有数据解析完成！");
//    }
    /**
     *  存储数据
     */
//    private void saveData() {
//        LOGGER.info("{}条数据，开始存储数据库！", list.size());
//        ArrayList<NewOrderExcelInDTO> newOrderExcelInDTOS = new ArrayList<>();
//
//        try {
//            defaultService.oprData(list);
//        } catch (Exception e) {
//            LOGGER.error("解析失败---->>>>>" + e.toString(), e );
//        }
//        LOGGER.info("存储数据库成功！");
//    }
}

