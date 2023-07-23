package wang.ultra.my_utilities.zbhd_scheduler.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
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
    public AjaxUtils airportsAdd(Airports airports) {
//        List<Airports> airportsList = new ArrayList<>();
        List<Airports> airportsList = new ArrayList<>();

        airportsList.add(airports);


        Integer resultInt = airportsService.airportsAdd(airportsList);

        return switch (resultInt) {
            case -2 -> AjaxUtils.failed("数据错误! ");
            case -1 -> AjaxUtils.failed("添加失败! ");
            case 1 -> AjaxUtils.success("添加成功! ");
            case 2 -> AjaxUtils.success("更新成功! ");
            default -> AjaxUtils.failed("发生异常! ");
        };
    }

    @GetMapping("/airportsSelectByAll")
    public AjaxUtils airportsSelectByAll(int pageNum, int pageSize) {
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

        returnList.addAll(airportsList);

        return AjaxUtils.success(returnList);
    }

    @GetMapping("/airportsSearch")
    public AjaxUtils airportsSearch(String search, int pageNum, int pageSize) {
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

        returnList.addAll(airportsList);
        return AjaxUtils.success(returnList);
    }

    @GetMapping("/airportsSearchByICAO")
    public AjaxUtils airportsSearchByICAO(String icao) {
        Airports airports = new Airports();
        airports.setICAO(icao);
        return AjaxUtils.success(airportsService.airportsSearchCount(airports));
    }

    @GetMapping("/airportsSearchByCity")
    public AjaxUtils airportsSearchByCity(String search) {
        Airports airports = new Airports();
        airports.setCity(search);
        airports.setName(search);
        return AjaxUtils.success(airportsService.airportsSearch(airports));
    }

}
