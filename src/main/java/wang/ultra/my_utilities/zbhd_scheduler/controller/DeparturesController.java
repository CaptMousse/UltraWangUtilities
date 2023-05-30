package wang.ultra.my_utilities.zbhd_scheduler.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Departures;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;
import wang.ultra.my_utilities.zbhd_scheduler.service.DeparturesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/zbhd_scheduler/departures")
public class DeparturesController {
    @Autowired
    DeparturesService departuresService;

    @PostMapping("departuresAdd")
    public Integer departuresAdd(Departures departures) {

        List<Departures> departuresList = new ArrayList<>();
        departuresList.add(departures);

        return departuresService.departuresAdd(departuresList);
    }

    @GetMapping("departuresShow")
    public List<Object> departuresShow(int pageNum, int pageSize) {
        List<Map<String, Object>> departuresList = departuresService.departuresShow();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(departuresList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        for (Map<String, Object> map : departuresList) {
            returnList.add(map);
        }
        return returnList;
    }

    @GetMapping("departuresSearch")
    public List<Object> departuresSearch(String search, int pageNum, int pageSize) {
        SearchVO searchVO = new SearchVO();
        searchVO.setFlightNo(search);
        searchVO.setAirportICAO(search);
        searchVO.setIATA(search);
        searchVO.setName(search);
        searchVO.setCity(search);

        List<Map<String, Object>> departuresSearchList = departuresService.departuresSearch(searchVO);
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(departuresSearchList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        for (Map<String, Object> map : departuresSearchList) {
            returnList.add(map);
        }

        return returnList;
    }

    @GetMapping("departuresSearchByFlightNo")
    public Integer departuresSearchByFlightNo(String flightNo) {
        Departures departures = new Departures();
        departures.setFlightNo(flightNo);
        return departuresService.departuresSearchCount(departures);
    }

    @GetMapping("departuresToday")
    public List<Object> departuresToday(int pageNum, int pageSize) {
        List<Map<String,Object>> departuresTodayList = departuresService.departuresToday();

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(departuresTodayList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        for (Map<String, Object> map : departuresTodayList) {
            returnList.add(map);
        }

        return returnList;
    }
}
