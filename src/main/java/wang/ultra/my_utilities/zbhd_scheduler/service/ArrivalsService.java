package wang.ultra.my_utilities.zbhd_scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Arrivals;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.ScheduleVO;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.ArrivalsMapper;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.InsertMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.UpdateMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("arrivalsService")
public class ArrivalsService {
    @Autowired
    InsertMapper insertMapper;

    @Autowired
    UpdateMapper updateMapper;

    @Autowired
    ArrivalsMapper arrivalsMapper;

    public Integer arrivalsAdd(Arrivals arrivals) {

        SearchVO searchVO = new SearchVO();
        searchVO.setFlightNo(arrivals.getFlightNo());
        List<Map<String, Object>> searchList = arrivalsSearch(searchVO);


        if (!searchList.isEmpty()) {
            // 更新
            arrivals.setUpdateDate(DateConverter.getDate());
            arrivals.setStatus(0);
            updateMapper.arrivalUpdate(arrivals);

            return 2;
        }

        if (arrivals.getAirportICAO() == null || arrivals.getDeparture() == null || arrivals.getArrival() == null || arrivals.getSchedule() == null) {
            return -1;
        }
        arrivals.setUpdateDate(DateConverter.getDate());
        arrivals.setStatus(0);

        List<Arrivals> arrivalsList = new ArrayList<>();
        arrivalsList.add(arrivals);

        try {
            insertMapper.arrivalsAdd(arrivalsList);
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    public List<Map<String, Object>> arrivalsShow() {
        return arrivalsMapper.arrivalsSelectAll();
    }

    public List<Map<String, Object>> arrivalsSearch(SearchVO arrivalsSearchVO) {
        return arrivalsMapper.arrivalsSearch(arrivalsSearchVO);
    }

    public List<Map<String, Object>> arrivalsToday() {
        String weekday = DateConverter.getWeekday();
        ScheduleVO arrivalsScheduleVO = new ScheduleVO(weekday);
        // ArrivalsScheduleVO arrivalsScheduleVO = new ArrivalsScheduleVO();
        return arrivalsMapper.arrivalsToday(arrivalsScheduleVO);
    }

    public Integer arrivalsSearchCount(Arrivals arrivals) {
        return arrivalsMapper.arrivalsSearchCount(arrivals);
    }

    public String getLastUpdateDate() {
        return arrivalsMapper.getLastUpdateDate();
    }
}
