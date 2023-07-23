package wang.ultra.my_utilities.zbhd_scheduler.mapper;

import wang.ultra.my_utilities.zbhd_scheduler.entity.Arrivals;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Departures;

public interface UpdateMapper {

    void arrivalUpdate(Arrivals arrivals);

    void departureUpdate(Departures departures);
}
