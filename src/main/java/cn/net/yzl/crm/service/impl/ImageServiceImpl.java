package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.ImageClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.ImageService;
import cn.net.yzl.product.model.db.Image;
import cn.net.yzl.product.model.db.ImageStore;
import cn.net.yzl.product.model.vo.image.ImageDTO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageClient imageClient;

    @Autowired
    private FastDFSConfig fastDFSConfig;


    @Override
    public ComResponse insert(Image img) {
        return imageClient.insert(img);
    }

    @Override
    public ComResponse createAlbum(ImageStore is) {
        return imageClient.createAlbum(is);
    }

    @Override
    public ComResponse<List<ImageDTO>> selectByStoreId(Integer storeId) {
        List<ImageDTO> list = imageClient.selectByStoreId(storeId).getData();
        return ComResponse.success(list).setMessage(fastDFSConfig.getUrl());
    }

    @Override
    public ComResponse selectTypeById(Integer storeId) {
        return imageClient.selectTypeById(storeId);
    }

    @Override
    public ComResponse<List<ImageStoreDTO>> selectStores(Integer type) {
        return imageClient.selectStores(type);
    }
}
