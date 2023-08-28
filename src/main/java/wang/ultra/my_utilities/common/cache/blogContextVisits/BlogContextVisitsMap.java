package wang.ultra.my_utilities.common.cache.blogContextVisits;


import java.util.*;

/**
 * 存储访问量
 * 根据访客ID
 */
public class BlogContextVisitsMap {
    private static final Map<String, List<BlogContextVisitsEntity>> cacheMap = new HashMap<>();

    // 清理超时的存储
    public BlogContextVisitsMap() {
        long cleanMillis = System.currentTimeMillis();
        for (Map.Entry<String, List<BlogContextVisitsEntity>> entry : cacheMap.entrySet()) {
            List<BlogContextVisitsEntity> entityList = entry.getValue();
            for (int i = entityList.size() - 1; i >= 0; i--) {
                long recordMillis = entityList.get(i).getExpireMillis();
                long diff = cleanMillis - recordMillis;
                if (diff >= 3600000) {
                    entityList.remove(i);
                }
            }
        }
    }

    public void setVisits(String contextId, String visitId) {
        BlogContextVisitsEntity entity = new BlogContextVisitsEntity(visitId, System.currentTimeMillis());
        List<BlogContextVisitsEntity> entityList = new ArrayList<>();
        if (cacheMap.containsKey(contextId)) {
            entityList = cacheMap.get(contextId);
            entityList.add(entity);
        } else {
            entityList.add(entity);
        }
        cacheMap.put(contextId, entityList);
    }

    public boolean availableVisits(String contextId, String visitId) {
        if (cacheMap.containsKey(contextId)) {
            List<BlogContextVisitsEntity> entityList = cacheMap.get(contextId);
            for (BlogContextVisitsEntity entity : entityList) {
                if (visitId.equals(entity.getVisitId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
