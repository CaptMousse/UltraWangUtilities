package wang.ultra.my_utilities.blog.mapper;

import wang.ultra.my_utilities.blog.entity.UserLoginEntity;

import java.util.List;
import java.util.Map;

public interface UserLoginInfoMapper {
    void userLoginAdd(List<UserLoginEntity> userLoginEntityList);

    void userLoginDelete(UserLoginEntity userLoginEntity);

    void userLoginUpdate(UserLoginEntity userLoginEntity);

    String userLoginAccountCount();

    List<Map<String, Object>> userLoginSearchByUsername(String username);


}
