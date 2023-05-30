package wang.ultra.my_utilities.zbhd_scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Arrivals;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.ScheduleVO;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.ArrivalsMapper;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.InsertMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;

import java.util.List;
import java.util.Map;

@Service("arrivalsService")
public class ArrivalsService {
    @Autowired
    InsertMapper insertMapper;

    @Autowired
    ArrivalsMapper arrivalsMapper;

    public Integer arrivalsAdd(List<Arrivals> arrivalsList) {

        System.out.println("arrivalsList = " + arrivalsList);
        if (arrivalsList.isEmpty()) {
            return -1;
        }
        for (Arrivals a : arrivalsList) {
            if (a.getFlightNo() == null || a.getAirportICAO() == null || a.getDeparture() == null || a.getArrival() == null || a.getSchedule() == null) {
                return -1;
            }
            a.setUpdateDate(DateConverter.getDate());
            a.setStatus(0);
            System.out.println("a = " + a);
        }

        try {
            insertMapper.arrivalsAdd(arrivalsList);    
        } catch (Exception e) {
            // TODO: handle exception
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
        List<Map<String,Object>> arrivalsTodayList = arrivalsMapper.arrivalsToday(arrivalsScheduleVO);
        // ArrivalsScheduleVO arrivalsScheduleVO = new ArrivalsScheduleVO();
        return arrivalsTodayList;
    }

    public Integer arrivalsSearchCount(Arrivals arrivals) {
        return arrivalsMapper.arrivalsSearchCount(arrivals);
    }
}
