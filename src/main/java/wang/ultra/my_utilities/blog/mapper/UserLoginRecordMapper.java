package wang.ultra.my_utilities.blog.mapper;

import wang.ultra.my_utilities.blog.entity.UserLoginRecordEntity;

import java.util.List;
import java.util.Map;

public interface UserLoginRecordMapper {
    void userLoginRecordAdd(List<UserLoginRecordEntity> list);

    List<Map<String, Object>> recordSearchByUsername(String username);


}
