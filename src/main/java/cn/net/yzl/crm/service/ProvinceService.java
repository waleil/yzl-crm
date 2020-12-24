package cn.net.yzl.crm.service;

import cn.net.yzl.crm.bean.DataSourceSelector;
import cn.net.yzl.crm.bean.DynamicDataSourceEnum;
import cn.net.yzl.crm.model.Province;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 19:04
 * @description:
 */
public interface ProvinceService {
    void saveProvince(Province province);

    Province getProvince();
}
