package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.Image;

public interface ImageService {

    ComResponse insert(Image img);
}
