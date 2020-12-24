package cn.net.yzl.crm.dao;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBaseDAO<T ,PK extends Serializable>{
	T selectById(PK id);

	List<T> selectAll(@Param("params") Map<String,Object> params);

	Long selectCount(@Param("params") Map<String,Object> params);

	Integer save(T t);

	Integer update(T t);

	Integer deleteById(@Param("id") PK id);




	
}