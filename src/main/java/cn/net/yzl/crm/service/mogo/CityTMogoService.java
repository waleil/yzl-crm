//package cn.net.yzl.crm.service.mogo;
//
//import cn.net.yzl.crm.dao.mogo.CityTMogoDAO;
//import cn.net.yzl.crm.model.mogo.CityDemo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @Author jingweitao
// **/
//@Service
//public class CityTMogoService {
//
//    @Autowired
//    private CityTMogoDAO dao;
//
//    public String saveObj2(CityDemo c) {
//        c.setCreateTime(new Date());
//        c.setUpdateTime(new Date());
//        //调用bookMongoDbDao父类中的添加方法
//        dao.save(c);
//        return "添加成功";
//    }
//
//    public String page(CityDemo c,int start,int size) {
//        List<CityDemo> page = dao.getPage(c, start, size);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+page.toString()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        return "添加成功";
//    }
//
//    /**
//     * @Description 测试事务
//     * @Author jingweitao
//     * @Date 14:17 2020/12/10
//     **/
//    @Transactional
//    public void testTrani() {
//        CityDemo c = new CityDemo();
//        c.setName("7");
//        dao.save(c);
//        c.setName("8");
//        int i = 1/0;
//        dao.save(c);
//    }
//
//    public CityDemo queryById(String id) {
//        CityDemo cityDemo = dao.queryById(id);
//        return cityDemo;
//    }
//
//    public void save(CityDemo cityDemo) {
//        dao.save(cityDemo);
//    }
//}
