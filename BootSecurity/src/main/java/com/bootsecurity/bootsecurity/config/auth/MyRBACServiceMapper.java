package com.bootsecurity.bootsecurity.config.auth;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MyRBACServiceMapper {
    @Select("select url from sys_menu m left join sys_role_menu rm on m.id = rm.menu_id left join sys_role r on r.id = rm.role_id left join sys_user_role ur on r.id = ur.role_id left join sys_user u on u.id = ur.user_id where u.username = #{userId} or u.phone = #{userId}")
    List<String> findUrlByUserName(@Param("userId") String userId);
}
