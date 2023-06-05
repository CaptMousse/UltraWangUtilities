package wang.ultra.my_utilities.idiompedia.mapper;



import org.apache.ibatis.annotations.Param;
import wang.ultra.my_utilities.idiompedia.entity.IdiomEntity;

import java.util.List;
import java.util.Map;

public interface IdiomsMapper {
    void idiomsAdd(List<IdiomEntity> i);

    void idiomsDelete(String id);

    void idiomsAmountCount(@Param("amount") long amount, @Param("id") String id);
    
    List<Map<String, Object>> idiomsSearchByWord(String word);

    Integer idiomsCountByWord(IdiomEntity idiomEntity);

    List<Map<String, Object>> idiomsSearchByPinyinFirst(String pinyin_first);

    List<Map<String, Object>> idiomsSearchByFirst(String key);
    List<Map<String, Object>> idiomsSearchBySecond(String key);
    List<Map<String, Object>> idiomsSearchByThird(String key);
    List<Map<String, Object>> idiomsSearchByLast(String key);

    List<Map<String, Object>> idiomsSearchByQuestionMark(IdiomEntity idiomEntity);
}
