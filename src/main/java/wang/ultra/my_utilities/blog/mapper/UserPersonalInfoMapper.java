package wang.ultra.my_utilities.blog.mapper;

import java.util.List;
import java.util.Map;

public interface UserPersonalInfoMapper {
    List<Map<String, Object>> getUserInfoByUsername(String username);
}
