package wang.ultra.my_utilities.zbhd_scheduler.mapper;

import wang.ultra.my_utilities.zbhd_scheduler.entity.Airports;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Arrivals;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Departures;

import java.util.List;

public interface InsertMapper {

    void airportsAdd(List<Airports> airports);

    void arrivalsAdd(List<Arrivals> arrivals);

    void departureAdd(List<Departures> departures);
}
