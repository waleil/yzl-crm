package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.Image;
import cn.net.yzl.product.model.db.ImageStore;

import java.util.Map;

public interface ImageService {

    ComResponse insert(Image img);

    ComResponse createAlbum(ImageStore is);

    ComResponse<Map<Integer, String>> selectByStoreId(Integer storeId);
}
