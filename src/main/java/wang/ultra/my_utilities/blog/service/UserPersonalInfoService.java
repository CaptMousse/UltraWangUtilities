package wang.ultra.my_utilities.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.blog.mapper.ContextMapper;
import wang.ultra.my_utilities.blog.mapper.UserPersonalInfoMapper;
import wang.ultra.my_utilities.common.cache.username.UserLoginCacheMap;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.ListConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userPersonalInfoService")
public class UserPersonalInfoService {

    @Autowired
    UserPersonalInfoMapper userPersonalInfoMapper;

    @Autowired
    ContextMapper contextMapper;

    public Map<String, String> getUserInfo(String username) {
        List<Map<String, Object>> userInfoList = userPersonalInfoMapper.getUserInfoByUsername(username);

        Map<String, String> userInfoMap = ListConverter.mapValueToString(userInfoList).get(0);

        UserLoginCacheMap cacheMap = new UserLoginCacheMap();
        if (cacheMap.hasLogon(username)) {
            userInfoMap.put("record_time", "当前在线");
        } else {
            String timeAgo = DateConverter.getTimeAgo(userInfoMap.get("record_time"));
            userInfoMap.put("record_time", timeAgo);
        }

        String userContextsCount = contextMapper.getUserContexts(username);
        userInfoMap.put("contexts_count", userContextsCount);
        return userInfoMap;
    }
}
