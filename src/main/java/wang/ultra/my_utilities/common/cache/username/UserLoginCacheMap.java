package wang.ultra.my_utilities.common.cache.username;

import wang.ultra.my_utilities.blog.entity.UserLoginRecordEntity;
import wang.ultra.my_utilities.blog.service.UserLoginRecordService;
import wang.ultra.my_utilities.common.constant.ConstantFromFile;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.SpringBeanUtils;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.*;

/**
 * 存储验证码的Map
 */
public class UserLoginCacheMap {

    private static final Map<String, UserLoginRecordEntity> cacheMap = new HashMap<>();

    private final UserLoginRecordService userLoginRecordService = SpringBeanUtils.getBean(UserLoginRecordService.class);

    public UserLoginCacheMap() {
        // 清理过期的验证码
        long cleanMillis = System.currentTimeMillis() - (ConstantFromFile.getSessionInactiveInterval() * 1000L);
        // 不能用for遍历删除过期的用户
        Iterator<Map.Entry<String, UserLoginRecordEntity>> iterator = cacheMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, UserLoginRecordEntity> entry = iterator.next();
            long recordTime = DateConverter.getMillis(entry.getValue().getRecord_time());
            if (recordTime < cleanMillis) {

                String username = entry.getValue().getUsername();
                String uuid = entry.getValue().getUsername();

                System.out.println("用户 " + entry.getValue().getUsername() + " 登录已过期. ");

                UserLoginRecordEntity entity = new UserLoginRecordEntity(username, "登录过期", "localhost");
                entity.setUuid(StringUtils.getMyUUID());
                entity.setRecord_time(DateConverter.getTime(recordTime + (ConstantFromFile.getSessionInactiveInterval() * 1000L)));
                entity.setRecord_type(2);
                userLoginRecordService.loginRecordAdd(entity);

                iterator.remove();
            }
        }
    }

    public boolean hasUsername(String loginToken) {
        return cacheMap.containsKey(loginToken);
    }

    public String userLogin(String username, String ipAddress) {
        String loginToken = StringUtils.getMyUUID();
        UserLoginRecordEntity entity = new UserLoginRecordEntity(username, "用户登入", ipAddress);
        entity.setUuid(loginToken);
        entity.setRecord_time(DateConverter.getTime(System.currentTimeMillis()));
        entity.setRecord_type(1);

        userLoginRecordService.loginRecordAdd(entity);

        cacheMap.put(loginToken, entity);

        return loginToken;
    }


    /**
     * 续期
     *
     * @param loginToken
     */
    public void userLoginUpdate(String loginToken) {

        UserLoginRecordEntity entityOld = cacheMap.get(loginToken);
        if (entityOld == null) {
            return;
        }
        UserLoginRecordEntity entityNew;
        entityNew = entityOld;
        entityNew.setRecord_time(DateConverter.getTime(System.currentTimeMillis()));
        cacheMap.put(entityNew.getUuid(), entityNew);
    }

    public List<String> getLoginUserList() {
        List<String> resultList = new ArrayList<>();
        int i = 1;
        for (String key : cacheMap.keySet()) {
            String username = cacheMap.get(key).getUsername();
            String recordTime = cacheMap.get(key).getRecord_time();
            String result = "序号: " + i++ + "\t在线用户: " + username + "\t最后时间: " + recordTime;
            System.out.println(result);
            resultList.add(result);
        }
        return resultList;
    }

    public String getLoginUser(String loginToken) {
        if (!cacheMap.containsKey(loginToken)) {
            return null;
        }
        return cacheMap.get(loginToken).getUsername();
    }
}
