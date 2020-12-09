package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.dto.region.ProvinceTResDTO;
import cn.net.yzl.crm.model.ProvinceT;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProvinceTMapper {
    Integer deleteById(Integer id);

    Integer insert(ProvinceT record);

    ProvinceTResDTO selectById(Integer id);

    Integer update(ProvinceT record);

    List<ProvinceTResDTO> selectList(@Param("params") Map<String,Object> params);
}