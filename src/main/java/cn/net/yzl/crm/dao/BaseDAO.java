package cn.net.yzl.crm.dao;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDAO<T ,PK extends Serializable>{
	Integer deleteById(PK id);

	Integer insert(T t);

	T selectById(PK id);

	Integer update(T t);

	List<T> selectList(@Param("params") Map<String,Object> params);
	
}