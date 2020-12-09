package cn.net.yzl.crm.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProvinceT {
    private Integer id;

    private String name;

    private Integer code;

    private Integer countryId;

    private String zname;

    private String aname;

    private String pname;

    private String abbr;

    private Date createTime;

    private Date updateTime;


}