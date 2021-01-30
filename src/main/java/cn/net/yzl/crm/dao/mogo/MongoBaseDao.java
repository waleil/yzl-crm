//package cn.net.yzl.crm.dao.mogo;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//public abstract class MongoBaseDao<T> {
//	/** 泛型参数类型 */
//	protected  Class<T> entityClass;
//
//	@SuppressWarnings("unchecked")
//	public MongoBaseDao() {
//		super();
//		// 返回该类表示的实体的直接超类的Type（类，接口，原始类型或void）。
//		Type type = this.getClass().getGenericSuperclass();
//		// 如果该类的实例是泛型参数类型
//		if (type instanceof ParameterizedType) {
//			ParameterizedType paramType = (ParameterizedType) type;
//			// 返回一个Type对象数组，该数组表示此类型的实际类型参数。
//			Type[] args = paramType.getActualTypeArguments();
//			this.entityClass = (Class<T>) args[0];
//		}
//		// 如果没有获取到泛型参数类型(出现这种情况，主要是子类继承父类的时候，没有指定泛型参数类型)
//		if (this.entityClass == null) {
//			this.entityClass = this.getEntityClass();
//		}
//		log.debug("当前对象的泛型参数类型：{}", this.entityClass);
//	}
//
//
//	/**
//     * 反射获取泛型类型
//     */
//    protected abstract Class<T> getEntityClass();
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//
//    /**
//     * @Description 保存一个对象
//     * @Author jingweitao
//     * @Date 13:28 2020/12/10
//     * @Param [t]
//     * @return void
//     **/
//    public void save(T t) {
//        this.mongoTemplate.save(t);
//    }
//
//
//    /**
//     * @Description 根据id从集合中查询对象
//     * @Author jingweitao
//     * @Date 13:28 2020/12/10
//     * @Param [id]
//     * @return T
//     **/
//    public T queryById(String id) {
//        Query query = new Query(Criteria.where("_id").is(id));
//        return this.mongoTemplate.findOne(query, this.getEntityClass());
//    }
//
//
//    /**
//     * @Description 根据条件查询集合
//     * @Author jingweitao
//     * @Date 13:28 2020/12/10
//     * @Param [object]
//     * @return java.util.List<T>
//     **/
//    public List<T> queryList(T object) {
//        Query query = getQueryByObject(object);
//        return mongoTemplate.find(query, this.getEntityClass());
//    }
//
//
//    /**
//     * @Description 根据条件查询只返回一个文档
//     * @Author jingweitao
//     * @Date 13:28 2020/12/10
//     * @Param [object]
//     * @return java.util.List<T>
//     **/
//    public T queryOne(T object) {
//        Query query = getQueryByObject(object);
//        return mongoTemplate.findOne(query, this.getEntityClass());
//    }
//
//    /**
//     * @Description 根据条件分页
//     * @Author jingweitao
//     * @Date 13:28 2020/12/10
//     * @param object
//     * @param start 查询起始值
//     * @param size  查询大小
//     **/
//    public List<T> getPage(T object, int start, int size) {
//        Query query = getQueryByObject(object);
//        query.skip(start);
//        query.limit(size);
//        return this.mongoTemplate.find(query, this.getEntityClass());
//    }
//
//
//    /**
//     * @Description 根据条件查询库中符合条件的记录数量
//     * @Author jingweitao
//     * @Date 13:29 2020/12/10
//     * @Param [object]
//     * @return java.lang.Long
//     **/
//    public Long getCount(T object) {
//        Query query = getQueryByObject(object);
//        return this.mongoTemplate.count(query, this.getEntityClass());
//    }
//
//
//    /**
//     * @Description 删除对象
//     * @Author jingweitao
//     * @Date 13:29 2020/12/10
//     * @Param [t]
//     * @return int
//     **/
//    public int delete(T t) {
//        return (int) this.mongoTemplate.remove(t).getDeletedCount();
//    }
//
//
//    /**
//     * @Description 根据id删除
//     * @Author jingweitao
//     * @Date 13:30 2020/12/10
//     * @Param [id]
//     * @return void
//     **/
//    public void deleteById(String id) {
//        Criteria criteria = Criteria.where("_id").is(id);
//        Query query = new Query(criteria);
//        T obj = this.mongoTemplate.findOne(query, this.getEntityClass());
//        if (obj != null) {
//            this.delete(obj);
//        }
//    }
//
//    /*MongoDB中更新操作分为三种
//    * 1：updateFirst     修改第一条
//    * 2：updateMulti     修改所有匹配的记录
//    * 3：upsert  修改时如果不存在则进行添加操作
//    * */
//
//
//    /**
//     * @Description 修改匹配到的第一条记录
//     * @Author jingweitao
//     * @Date 13:30 2020/12/10
//     * @Param [srcObj, targetObj]
//     * @return void
//     **/
//    public void updateFirst(T srcObj, T targetObj){
//        Query query = getQueryByObject(srcObj);
//        Update update = getUpdateByObject(targetObj);
//        this.mongoTemplate.updateFirst(query,update,this.getEntityClass());
//    }
//
//
//    /**
//     * @Description 修改匹配到的所有记录
//     * @Author jingweitao
//     * @Date 13:31 2020/12/10
//     * @Param [srcObj, targetObj]
//     * @return void
//     **/
//    public void updateMulti(T srcObj, T targetObj){
//        Query query = getQueryByObject(srcObj);
//        Update update = getUpdateByObject(targetObj);
//        this.mongoTemplate.updateMulti(query,update,this.getEntityClass());
//    }
//
//
//    /**
//     * @Description 修改匹配到的记录，若不存在该记录则进行添加
//     * @Author jingweitao
//     * @Date 13:31 2020/12/10
//     * @Param [srcObj, targetObj]
//     * @return void
//     **/
//    public void updateInsert(T srcObj, T targetObj){
//        Query query = getQueryByObject(srcObj);
//        Update update = getUpdateByObject(targetObj);
//        this.mongoTemplate.upsert(query,update,this.getEntityClass());
//    }
//
//
//    /**
//     * @Description 将查询条件对象转换为query
//     * @Author jingweitao
//     * @Date 13:31 2020/12/10
//     * @Param [object]
//     * @return org.springframework.data.mongodb.core.query.Query
//     **/
//    private Query getQueryByObject(T object) {
//        Query query = new Query();
//        List<String> filedNameList = getFiledName(object);
//
//        Criteria criteria = new Criteria();
//        for (String filed : filedNameList) {
//            Object filedValue = getFieldValueByName(filed, object);
//            if (filedValue != null) {
//                criteria.and(filed).is(filedValue);
//            }
//        }
//        query.addCriteria(criteria);
//        return query;
//    }
//
//    /**
//     * 将查询条件对象转换为update
//     */
//    private Update getUpdateByObject(T object) {
//        Update update = new Update();
//        List<String> filedNameList = getFiledName(object);
//        for (String filedName : filedNameList) {
//            Object filedValue = getFieldValueByName(filedName, object);
//            if (filedValue != null) {
//                update.set(filedName, filedValue);
//            }
//        }
//        return update;
//    }
//
//    /***
//     * 获取对象属性返回字符串数组
//     */
//    private static List<String> getFiledName(Object o) {
//        Field[] fields = o.getClass().getDeclaredFields();
//        return Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
//    }
//    /***
//     * 根据属性获取对象属性值
//     */
//    private static Object getFieldValueByName(String fieldName, Object o) {
//        try {
//            String e = fieldName.substring(0, 1).toUpperCase();
//            String getter = "get" + e + fieldName.substring(1);
//            Method method = o.getClass().getMethod(getter);
//            return method.invoke(o);
//        } catch (Exception var6) {
//            return null;
//        }
//    }
//    /***
//     * mongo 批量插入
//     */
//    public List<T> bathSave(List<T> list) {
//        Collection<T> ts = mongoTemplate.insertAll(list);
//        return ts.stream().collect(Collectors.toList());
//    }
//}