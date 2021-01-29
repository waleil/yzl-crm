//package cn.net.yzl.crm.test;
//
//import org.junit.jupiter.api.Test;
//
//import cn.net.yzl.crm.dao.mogo.DemoMogoDAO;
//import cn.net.yzl.crm.dao.mogo.MongoBaseDao;
//
///**
// * 单元测试类
// * 
// * @author zhangweiwei
// * @date 2021年1月18日,下午7:10:24
// */
//public class MongoBaseDaoTests {
//	@Test
//	public void testGetParamType() {
//		new DemoMogoDAO();
//		new IntegerMogoDao();
//		new StringMogoDao();
//	}
//
//	static class IntegerMogoDao extends MongoBaseDao<Integer> {
//
//		@Override
//		protected Class<Integer> getEntityClass() {
//			return null;
//		}
//	}
//
//	static class StringMogoDao extends MongoBaseDao<String> {
//
//		@Override
//		protected Class<String> getEntityClass() {
//			return null;
//		}
//	}
//
//	// 不建议使用这种写法，既然用了泛型，就需要指定泛型参数类型
//	@SuppressWarnings("rawtypes")
//	static class ObjectMogoDao extends MongoBaseDao {
//
//		@Override
//		protected Class getEntityClass() {
//			return Object.class;
//		}
//	}
//}
