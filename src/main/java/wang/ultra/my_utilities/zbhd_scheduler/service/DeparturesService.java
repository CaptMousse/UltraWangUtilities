package wang.ultra.my_utilities.zbhd_scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Departures;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.ScheduleVO;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.DeparturesMapper;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.InsertMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.UpdateMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("departuresService")
public class DeparturesService {
    @Autowired
    InsertMapper insertMapper;

    @Autowired
    DeparturesMapper departuresMapper;

    @Autowired
    UpdateMapper updateMapper;

    public Integer departuresAdd(Departures departures) {

//        System.out.println("departuresList = " + departuresList);

        SearchVO searchVO = new SearchVO();
        searchVO.setFlightNo(departures.getFlightNo());
        List<Map<String, Object>> searchList = departuresSearch(searchVO);

        if (!searchList.isEmpty()) {
            // 更新
            departures.setUpdateDate(DateConverter.getDate());
            departures.setStatus(0);
            updateMapper.departureUpdate(departures);

            return 2;
        }

        if (departures.getFlightNo() == null || departures.getAirportICAO() == null || departures.getDeparture() == null || departures.getArrival() == null || departures.getSchedule() == null) {
            return -2;
        }
        departures.setUpdateDate(DateConverter.getDate());
        departures.setStatus(0);

        List<Departures> departuresList = new ArrayList<>();
        departuresList.add(departures);

        try {
            insertMapper.departureAdd(departuresList);
        } catch (Exception e) {
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
     */
    public List<Map<String, Object>> departuresToday() {
        String weekday = DateConverter.getWeekday();
        ScheduleVO scheduleVO = new ScheduleVO(weekday);
        return departuresMapper.departuresToday(scheduleVO);
    }

    public Integer departuresSearchCount(Departures departures) {
        return departuresMapper.departuresSearchCount(departures);
    }

    public String getLastUpdateDate() {
        return departuresMapper.getLastUpdateDate();
    }
}
