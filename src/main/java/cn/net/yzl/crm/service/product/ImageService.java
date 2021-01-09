package cn.net.yzl.crm.service.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.product.model.vo.image.ImageDTO;
import cn.net.yzl.product.model.vo.image.ImageVO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreDTO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreVO;

import java.util.List;

public interface ImageService {

    ComResponse insert(ImageVO img);

    ComResponse createAlbum(ImageStoreVO is);

    ComResponse<Page<ImageDTO>> selectByStoreId(Integer storeId, Integer pageNo, Integer pageSize);

    ComResponse<Integer> selectTypeById(Integer storeId);

    ComResponse<List<ImageStoreDTO>> selectStores(Integer type);

    ComResponse deleteById(Integer id, String userId);

    ComResponse deleteStoreById(Integer id, String userId);
}
