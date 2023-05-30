package wang.ultra.my_utilities.zbhd_scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Departures;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.ScheduleVO;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.DeparturesMapper;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.InsertMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;

import java.util.List;
import java.util.Map;

@Service("departuresService")
public class DeparturesService {
    @Autowired
    InsertMapper insertMapper;

    @Autowired
    DeparturesMapper departuresMapper;

    public Integer departuresAdd(List<Departures> departuresList) {

        System.out.println("departuresList = " + departuresList);
        if (departuresList.isEmpty()) {
            return -1;
        }
        for (Departures d : departuresList) {
            if (d.getFlightNo() == null || d.getAirportICAO() == null || d.getDeparture() == null || d.getArrival() == null || d.getSchedule() == null) {
                return -1;
            }
            d.setUpdateDate(DateConverter.getDate());
            d.setStatus(0);
        }

        try {
            insertMapper.departureAdd(departuresList);    
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }
        return 1;
    }

    public List<Map<String, Object>> departuresShow() {
        return departuresMapper.departuresSelectAll();
    }

    public List<Map<String, Object>> departuresSearch(SearchVO searchVO) {
        return departuresMapper.departuresSearch(searchVO);
    }

    /**
     * 根据今天星期几返回数据
     * @return
     */
    public List<Map<String, Object>> departuresToday() {
        String weekday = DateConverter.getWeekday();
        ScheduleVO scheduleVO = new ScheduleVO(weekday);
        List<Map<String,Object>> departuresTodayList = departuresMapper.departuresToday(scheduleVO);
        return departuresTodayList;
    }

    public Integer departuresSearchCount(Departures departures) {
        return departuresMapper.departuresSearchCount(departures);
    }
}
