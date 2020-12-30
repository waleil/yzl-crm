package cn.net.yzl.crm.dao;

import cn.net.yzl.crm.model.LabelType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LabelMapper {
    List<LabelType> getLabelTypes();
}
