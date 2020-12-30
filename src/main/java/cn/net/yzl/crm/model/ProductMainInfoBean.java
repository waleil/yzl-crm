package cn.net.yzl.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//过滤用
public class ProductMainInfoBean implements Serializable {

    private Integer id;

    private String name;

    private Boolean type;

}
