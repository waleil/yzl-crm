package cn.net.yzl.crm.service.mogo;

import cn.net.yzl.crm.dao.mogo.DemoMogoDAO;
import cn.net.yzl.crm.model.mogo.DemoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author jingweitao
 **/
@Service
public class DemoMogoService {

    @Autowired
    private DemoMogoDAO dao;

    public String saveObj2(DemoMongo c) {
       c.setCreateTime(new Date());
       c.setUpdateTime(new Date());
        //调用bookMongoDbDao父类中的添加方法
        dao.save(c);
        return "添加成功";
    }

    public String page(DemoMongo c, int start, int size) {
        List<DemoMongo> page = dao.getPage(c, start, size);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+page.toString()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return "添加成功";
    }

    /**
     * @Description 测试事务
     * @Author jingweitao
     * @Date 14:17 2020/12/10
     **/
    @Transactional
    public void testTrani() {
        DemoMongo c = new DemoMongo();
        c.setName("7");
        dao.save(c);
        c.setName("8");
        int i = 1/0;
        dao.save(c);
    }

    public DemoMongo queryById(String id) {
        DemoMongo cityDemo = dao.queryById(id);
        return cityDemo;
    }

    public void save(DemoMongo cityDemo) {
        dao.save(cityDemo);
    }
}
