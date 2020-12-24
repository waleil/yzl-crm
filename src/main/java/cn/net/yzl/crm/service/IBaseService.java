package cn.net.yzl.crm.service;

import cn.net.yzl.crm.dao.IBaseDAO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.Assert;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *Service基类
 */
public interface IBaseService<T,PK extends Serializable> {
    IBaseDAO<T, PK> getDao();

    default T selectById(PK id){
        return getDao().selectById(id);
    }

    default List<T> selectForList(Map<String,Object> params){
        return getDao().selectAll(params);
    }

    default Optional<T> selectOne(Map<String,Object> params){
       List<T> list = getDao().selectAll(params);
       if(!list.isEmpty() && list.size()>1){
           Assert.isTrue(list.size() <= 1,"selectOne 必须返回一行，现在返回"+list.size()+"行");
       }
       return Optional.ofNullable(list.get(0));
    }

    default PageInfo<T> selectPage(Map<String, Object> params){//int page, int pageSize
        params.putIfAbsent("currentPage", 1);
        params.putIfAbsent("pageSize", 10);
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageHelper.startPage(currentPage,pageSize);
        List<T> cityList = getDao().selectAll(params);
        return  new PageInfo<T>(cityList);
    }

    default Long selectCount(Map<String, Object> params){
        return  getDao().selectCount(params);
    }
    default Integer save(T t){
        return  getDao().save(t);
    }
    default Integer update(T t){
        return  getDao().update(t);
    }
    default Integer deleteById(PK id){
        return  getDao().deleteById(id);
    }

}
