package wang.ultra.my_utilities.zbhd_scheduler.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Airports;
import wang.ultra.my_utilities.zbhd_scheduler.service.AirportsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/zbhd_scheduler/airports")
public class AirportsController {
    @Autowired
    AirportsService airportsService;

    @PostMapping("/airportAdd")
    public Integer airportsAdd(Airports airports) {
//        List<Airports> airportsList = new ArrayList<>();
        List<Airports> airportsList = new ArrayList<>();

        airportsList.add(airports);


        return airportsService.airportsAdd(airportsList);
    }

    @GetMapping("/airportsSelectByAll")
    public List<Object> airportsSelectByAll(int pageNum, int pageSize) {
        //PageHelper的次序不能乱

        PageHelper.startPage(pageNum, pageSize);

        List<Airports> airportsList = airportsService.airportsSelectAll();

        PageInfo<Airports> pageInfo = new PageInfo<>(airportsList);

        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        for (Airports airport : airportsList) {
            returnList.add(airport);
        }

        return returnList;
    }

    @GetMapping("/airportsSearch")
    public List<Object> airportsSearch(String search, int pageNum, int pageSize) {
        Airports airports = new Airports();
        airports.setICAO(search);
        airports.setIATA(search);
        airports.setName(search);
        airports.setCity(search);

        PageHelper.startPage(pageNum, pageSize);
        List<Airports> airportsList = airportsService.airportsSearch(airports);
        PageInfo<Airports> pageInfo = new PageInfo<>(airportsList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        for (Airports airport : airportsList) {
            returnList.add(airport);
        }
        return returnList;
    }

    @GetMapping("/airportsSearchByICAO")
    public Integer airportsSearchByICAO(String icao) {
        Airports airports = new Airports();
        airports.setICAO(icao);
        return airportsService.airportsSearchCount(airports);
    }

    @GetMapping("/airportsSearchByCity")
    public List<Airports> airportsSearchByCity(String search) {
        Airports airports = new Airports();
        airports.setCity(search);
        airports.setName(search);
        return airportsService.airportsSearch(airports);
    }

}
