package cn.net.yzl.crm.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhouchangsong
 */
@Mapper
@Repository
public interface OutStoreWarningMapper {

    /**
     * 根据菜单标识查询角色ID
     * @param menuPerms
     * @return
     */
    List<Integer> getRoleIdsByMenuPerms(@Param("menuPerms") List<String> menuPerms);

    /**
     * 根据角色ID查询用户code
     * @param roleIdList
     * @return
     */
    List<String> getUserCodesByRoleIds(@Param("roleIdList") List<Integer> roleIdList);
}
