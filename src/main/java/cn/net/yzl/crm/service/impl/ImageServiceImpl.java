package cn.net.yzl.crm.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.client.ImageClient;
import cn.net.yzl.crm.service.ImageService;
import cn.net.yzl.product.model.db.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageClient imageClient;


    @Override
    public ComResponse insert(Image img) {
        return imageClient.insert(img);
    }
}
