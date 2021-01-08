package cn.net.yzl.crm.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.db.Image;
import cn.net.yzl.product.model.db.ImageStore;
import cn.net.yzl.product.model.vo.image.ImageDTO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreDTO;

import java.util.List;

public interface ImageService {

    ComResponse insert(Image img);

    ComResponse createAlbum(ImageStore is);

    ComResponse<List<ImageDTO>> selectByStoreId(Integer storeId);

    ComResponse<Integer> selectTypeById(Integer storeId);

    ComResponse<List<ImageStoreDTO>> selectStores(Integer type);
}
