package cn.net.yzl.crm;


import cn.net.yzl.crm.model.mogo.DemoMongo;
import cn.net.yzl.crm.service.mogo.DemoMogoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YzlCrmApplicationTests {

    @Autowired
    private DemoMogoService service;


   // @Test
    void contextLoads() {
        //service.testTrani();
        DemoMongo c = new DemoMongo();
        c.setName("name2");
        service.saveObj2(c);
    }
    //@Test
    void mongoUpdateById() {
        DemoMongo c = new DemoMongo();
      //  c.setName("name33");
    }

    @Test
    void mongoQueryById() {
        DemoMongo cityDemo = service.queryById("5fd1c21bbc6ce7149050094f");
        System.out.println(">>>>>>>>>>>>>>>>>>"+cityDemo);
    }

   // @Test
    void mongoSAVE() {
        DemoMongo c = new DemoMongo();
     //   c.setName("1111");
        service.save(c);
    }



//    public  void testPage(CityDemo c,int current,int size){
//        service.page(c,current,size);
//    }

}
