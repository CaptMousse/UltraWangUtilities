package wang.ultra.my_utilities.common.cache.blogContextVisits;


import java.util.*;

/**
 * 存储一小时内每篇文章有哪些访客ID访问
 * 
 */
public class BlogContextVisitsMap {
    private static final Map<String, List<BlogContextVisitsEntity>> cacheMap = new HashMap<>();

    // 构造方法, 清理超时的存储
    public BlogContextVisitsMap() {
        long cleanMillis = System.currentTimeMillis();
        for (Map.Entry<String, List<BlogContextVisitsEntity>> entry : cacheMap.entrySet()) {
            List<BlogContextVisitsEntity> entityList = entry.getValue();
            for (int i = entityList.size() - 1; i >= 0; i--) {  // 因为是List, 所以要倒序删除
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
            // 遍历此文章的List, 删除老的放入新的, 以达到续期的目的
            for (BlogContextVisitsEntity e : entityList) {
                if (visitId.equals(e.getVisitId())) {
                    entityList.remove(e);
                }
            }
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
