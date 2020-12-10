package cn.net.yzl.crm.service.impl;

import cn.net.yzl.crm.bean.DataSourceSelector;
import cn.net.yzl.crm.bean.DynamicDataSourceEnum;
import cn.net.yzl.crm.dao.ProvinceMapper;
import cn.net.yzl.crm.dao.ProvinceTMapper;
import cn.net.yzl.crm.dto.region.ProvinceTResDTO;
import cn.net.yzl.crm.model.Province;
import cn.net.yzl.crm.model.ProvinceT;
import cn.net.yzl.crm.service.ProvinceService;
import cn.net.yzl.crm.service.ProvinceTService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class ProvinceTServiceImpl implements ProvinceTService {


    @Autowired
    private ProvinceTMapper provinceMapper;

    @Override
    public PageInfo<ProvinceT> findPage(Map<String, Object> params) {
        params.computeIfAbsent("currentPage", k -> 1);
        params.computeIfAbsent("pageSize", k -> 10);
        int currentPage = Integer.parseInt(params.get("currentPage").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());
        PageHelper.startPage(currentPage,pageSize );
        List<ProvinceT> provinceList = provinceMapper.selectList(params);
        return  new PageInfo<>(provinceList);
    }

    @Override
    public Optional<ProvinceT> getById(Integer id) {
        return Optional.ofNullable(provinceMapper.selectById(id));
    }

    @Override
    public Integer insert(ProvinceT province) {
        return provinceMapper.insert(province);
    }

}
