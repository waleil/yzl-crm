package cn.net.yzl.crm.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryTO implements Serializable {

    public CategoryBean categoryBean;

    public Integer productCount;

    public List<AttributeBean> attributeBeans;

}
