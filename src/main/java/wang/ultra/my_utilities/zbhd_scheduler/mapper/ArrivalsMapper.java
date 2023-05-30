package wang.ultra.my_utilities.zbhd_scheduler.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Arrivals;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.ScheduleVO;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArrivalsMapper {
    
    List<Map<String, Object>> arrivalsSelectAll();

    List<Map<String, Object>> arrivalsSearch(SearchVO arrivalsSearchVO);

    List<Map<String, Object>> arrivalsToday(ScheduleVO arrivalsScheduleVO);

    int arrivalsSearchCount(Arrivals arrivals);
}
