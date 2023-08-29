package wang.ultra.my_utilities.blog.mapper;

import wang.ultra.my_utilities.blog.entity.ContextEntity;

import java.util.List;
import java.util.Map;

public interface ContextMapper {

    void contextAdd(List<ContextEntity> list);

    void contextDelete(String uuid);

    // void contextAmountCount(String amount, String uuid);

    void contextTitleUpdate(String title, String update_time, String uuid);

    void contextUpdate(String update_time, String uuid);
    void contextAmount(String uuid);

    List<Map<String, Object>> contextList();

    List<Map<String, Object>> contextSearchByUser(String username);

    List<Map<String, Object>> contextSearchByUuid(String uuid);

    /**
     * 获得三个推荐置顶(优先级最高)
     * @return
     */
    List<Map<String, Object>> contextListRecommendIn3();
}
