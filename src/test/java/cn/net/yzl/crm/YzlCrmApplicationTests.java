//package cn.net.yzl.crm;
//
//import cn.net.yzl.crm.dao.mogo.CityTMogoDAO;
//import cn.net.yzl.crm.model.mogo.CityDemo;
//import cn.net.yzl.crm.service.mogo.CityTMogoService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class YzlCrmApplicationTests {
//
//    @Autowired
//    private CityTMogoService service;
//
//
//   // @Test
//    void contextLoads() {
//        //service.testTrani();
//        CityDemo c = new CityDemo();
//       // c.setName("name2");
//        service.saveObj2(c);
//    }
//    //@Test
//    void mongoUpdateById() {
//        CityDemo c = new CityDemo();
//      //  c.setName("name33");
//    }
//
//    @Test
//    void mongoQueryById() {
//        CityDemo cityDemo = service.queryById("5fd1c21bbc6ce7149050094f");
//        System.out.println(">>>>>>>>>>>>>>>>>>"+cityDemo);
//    }
//
//   // @Test
//    void mongoSAVE() {
//        CityDemo c = new CityDemo();
//     //   c.setName("1111");
//        service.save(c);
//    }
//
//
//
////    public  void testPage(CityDemo c,int current,int size){
////        service.page(c,current,size);
////    }
//
//}
