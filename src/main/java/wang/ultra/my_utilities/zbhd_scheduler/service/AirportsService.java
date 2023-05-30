package wang.ultra.my_utilities.zbhd_scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Airports;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.AirportsMapper;
import wang.ultra.my_utilities.zbhd_scheduler.mapper.InsertMapper;

import java.util.List;

@Service("airportsService")
public class AirportsService {

    @Autowired
    AirportsMapper airportsMapper;

    @Autowired
    InsertMapper insertMapper;
    
    public Integer airportsAdd(List<Airports> airports) {
        System.out.println("airports = " + airports);
        if (airports.isEmpty()) {
            return -1;
        }

        for (Airports a : airports) {
            if (a.getICAO() == null || a.getIATA() == null || a.getName() == null || a.getCity() == null) {
                return -1;
            }
        }

        try {
            insertMapper.airportsAdd(airports);    
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }

        return 1;
    }

    public List<Airports> airportsSelectAll() {
        return airportsMapper.airportsSelectAll();
    }

    public List<Airports> airportsSearch(Airports airports) {
        return airportsMapper.airportsSearch(airports);
    }

    public Integer airportsSearchCount(Airports airports) {
        return airportsMapper.airportsSearchCount(airports);
    }
    
}
