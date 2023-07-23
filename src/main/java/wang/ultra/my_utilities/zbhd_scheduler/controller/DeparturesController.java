package wang.ultra.my_utilities.zbhd_scheduler.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
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
    public AjaxUtils departuresAdd(Departures departures) {

        int resultInt = departuresService.departuresAdd(departures);

        return switch (resultInt) {
            case -2 -> AjaxUtils.failed("数据错误! ");
            case -1 -> AjaxUtils.failed("添加失败! ");
            case 1 -> AjaxUtils.success("添加成功! ");
            case 2 -> AjaxUtils.success("更新成功! ");
            default -> AjaxUtils.failed("发生异常! ");
        };

    }

    @GetMapping("departuresShow")
    public AjaxUtils departuresShow(int pageNum, int pageSize) {
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

        returnList.addAll(departuresList);
        return AjaxUtils.success(returnList);
    }

    @GetMapping("departuresSearch")
    public AjaxUtils departuresSearch(String search, int pageNum, int pageSize) {
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

        returnList.addAll(departuresSearchList);

        return AjaxUtils.success(returnList);
    }

    @GetMapping("departuresSearchByFlightNo")
    public Integer departuresSearchByFlightNo(String flightNo) {
        Departures departures = new Departures();
        departures.setFlightNo(flightNo);
        return departuresService.departuresSearchCount(departures);
    }

    @GetMapping("departuresToday")
    public AjaxUtils departuresToday(int pageNum, int pageSize) {
        List<Map<String, Object>> departuresTodayList = departuresService.departuresToday();

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(departuresTodayList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        returnList.addAll(departuresTodayList);

        return AjaxUtils.success(returnList);
    }
}
