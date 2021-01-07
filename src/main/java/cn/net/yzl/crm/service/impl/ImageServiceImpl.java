package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.ImageClient;
import cn.net.yzl.crm.service.ImageService;
import cn.net.yzl.product.model.db.Image;
import cn.net.yzl.product.model.db.ImageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageClient imageClient;


    @Override
    public ComResponse insert(Image img) {
        return imageClient.insert(img);
    }

    @Override
    public ComResponse createAlbum(ImageStore is) {
        return imageClient.createAlbum(is);
    }

    @Override
    public ComResponse<Map<Integer, String>> selectByStoreId(Integer storeId) {
        return imageClient.selectByStoreId(storeId);
    }
}
