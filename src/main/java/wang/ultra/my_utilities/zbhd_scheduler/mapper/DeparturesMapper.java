package wang.ultra.my_utilities.zbhd_scheduler.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Departures;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.ScheduleVO;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeparturesMapper {
    
    List<Map<String, Object>> departuresSelectAll();

    List<Map<String, Object>> departuresSearch(SearchVO searchVO);

    List<Map<String, Object>> departuresToday(ScheduleVO scheduleVO);

    int departuresSearchCount(Departures departures);

    String getLastUpdateDate();
}
