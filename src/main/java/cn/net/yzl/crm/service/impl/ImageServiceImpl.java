package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.client.ImageClient;
import cn.net.yzl.crm.config.FastDFSConfig;
import cn.net.yzl.crm.service.ImageService;
import cn.net.yzl.product.model.vo.image.ImageDTO;
import cn.net.yzl.product.model.vo.image.ImageVO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreDTO;
import cn.net.yzl.product.model.vo.imageStore.ImageStoreVO;
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
    public ComResponse insert(ImageVO img) {
        return imageClient.insert(img);
    }

    @Override
    public ComResponse createAlbum(ImageStoreVO is) {
        return imageClient.createAlbum(is);
    }

    @Override
    public ComResponse<Page<ImageDTO>> selectByStoreId(Integer storeId, Integer pageNo, Integer pageSize) {
        Page<ImageDTO> list = imageClient.selectByStoreId(storeId,pageNo,pageSize).getData();
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

    @Override
    public ComResponse deleteById(Integer id, String userId) {
        return imageClient.deleteById(id,userId);
    }

    @Override
    public ComResponse deleteStoreById(Integer id, String userId) {
        return imageClient.deleteStoreById(id, userId);
    }
}
