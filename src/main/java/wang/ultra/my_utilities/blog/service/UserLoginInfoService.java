package wang.ultra.my_utilities.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.blog.entity.UserLoginEntity;
import wang.ultra.my_utilities.blog.mapper.UserLoginInfoMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("userLoginInfoService")
public class UserLoginInfoService {

    @Autowired
    UserLoginInfoMapper userLoginInfoMapper;

    public void userLoginInfoAdd(UserLoginEntity userLoginEntity) {
        List<UserLoginEntity> userLoginEntityList = new ArrayList<>();

        String salt = StringUtils.getMyUUID();
        String saltPasswordMD5 = StringUtils.makeMD5(userLoginEntity.getPassword() + salt);
        userLoginEntity.setPassword(saltPasswordMD5);
        userLoginEntity.setSalt(salt);

        String time = DateConverter.getTime();
        userLoginEntity.setCreate_time(time);
        userLoginEntity.setUpdate_time(time);

        userLoginEntityList.add(userLoginEntity);
        userLoginInfoMapper.userLoginAdd(userLoginEntityList);
    }

    public Map<String, String> userLoginSearchByUsername(String username) {
        List<Map<String, Object>> userList = userLoginInfoMapper.userLoginSearchByUsername(username);

        if (userList.isEmpty()) {
            return null;
        }

        List<Map<String, String>> resultList = ListConverter.mapValueToString(userList);
        return resultList.get(0);
    }
}
