package wang.ultra.my_utilities.zbhd_scheduler.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Airports;

import java.util.List;

@Mapper
public interface AirportsMapper {
    List<Airports> airportsSelectAll();

    List<Airports> airportsSearch(Airports airports);
    
    Integer airportsSearchCount(Airports airports);
}
