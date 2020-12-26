package cn.net.yzl.crm.model;

import lombok.Data;

import java.util.List;

@Data
/*
 * @description 提供给前台的树状map封装类
 * @author Majinbao
 * @date 2020/12/25 10:04
 */
public class CategoryTreeNode {

    private Integer id;//分类id

    private String name;//分类名称

    private List<CategoryTreeNode> childNodes;//子类

}
