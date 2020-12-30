package cn.net.yzl.crm.service.impl;

import cn.net.yzl.crm.dao.LabelMapper;
import cn.net.yzl.crm.model.LabelType;
import cn.net.yzl.crm.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    LabelMapper labelMapper;

    @Override
    public List<LabelType> getLabelTypes() {
        return labelMapper.getLabelTypes();
    }
}
