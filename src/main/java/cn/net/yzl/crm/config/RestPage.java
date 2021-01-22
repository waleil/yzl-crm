package cn.net.yzl.crm.config;


import cn.net.yzl.crm.staff.dto.SpringDataPageAble;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestPage<T>{

    private Integer total;

    private List<T> content;

    private SpringDataPageAble pageable ;


}