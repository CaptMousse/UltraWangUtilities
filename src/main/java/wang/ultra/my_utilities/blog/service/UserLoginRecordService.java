package wang.ultra.my_utilities.blog.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.blog.entity.UserLoginRecordEntity;
import wang.ultra.my_utilities.blog.mapper.UserLoginRecordMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.ListConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("userLoginRecordService")
public class UserLoginRecordService {

    @Autowired
    UserLoginRecordMapper userLoginRecordMapper;

    public void loginRecordAdd(UserLoginRecordEntity entity) {
        entity.setUuid(StringUtils.getMyUUID());
        String time = DateConverter.getTime();
        entity.setRecord_time(time);
        entity.setRecord_type(1);

        List<UserLoginRecordEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        userLoginRecordMapper.userLoginRecordAdd(entityList);
    }

    public void logoutRecordAdd(UserLoginRecordEntity entity) {
        entity.setUuid(StringUtils.getMyUUID());
        entity.setRecord_time(DateConverter.getTime());
        entity.setRecord_type(2);

        List<UserLoginRecordEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        userLoginRecordMapper.userLoginRecordAdd(entityList);
    }

    public List<Map<String, String>> recordSearchByUsername(String username) {
        List<Map<String, Object>> resultList = userLoginRecordMapper.recordSearchByUsername(username);
        return ListConverter.mapValueToString(resultList);
    }
}
