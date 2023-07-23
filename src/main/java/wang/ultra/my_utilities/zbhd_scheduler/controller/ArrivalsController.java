package wang.ultra.my_utilities.zbhd_scheduler.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.zbhd_scheduler.entity.Arrivals;
import wang.ultra.my_utilities.zbhd_scheduler.entityVO.SearchVO;
import wang.ultra.my_utilities.zbhd_scheduler.service.ArrivalsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/zbhd_scheduler/arrivals")
public class ArrivalsController {
    @Autowired
    ArrivalsService arrivalsService;

    @PostMapping("arrivalsAdd")
    public AjaxUtils arrivalsAdd(Arrivals arrivals) {

        int resultInt = arrivalsService.arrivalsAdd(arrivals);

        return switch (resultInt) {
            case -2 -> AjaxUtils.failed("数据错误! ");
            case -1 -> AjaxUtils.failed("添加失败! ");
            case 1 -> AjaxUtils.success("添加成功! ");
            case 2 -> AjaxUtils.success("更新成功! ");
            default -> AjaxUtils.failed("发生异常! ");
        };
    }

    @GetMapping("arrivalsShow")
    public AjaxUtils arrivalsShow(int pageNum, int pageSize) {
        List<Map<String, Object>> arrivalsList = arrivalsService.arrivalsShow();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(arrivalsList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        returnList.addAll(arrivalsList);
        return AjaxUtils.success(returnList);
    }

    @GetMapping("arrivalsSearch")
    public AjaxUtils arrivalsSearch(String search, int pageNum, int pageSize) {
        SearchVO searchVO = new SearchVO();
        searchVO.setFlightNo(search);
        searchVO.setAirportICAO(search);
        searchVO.setIATA(search);
        searchVO.setName(search);
        searchVO.setCity(search);

        List<Map<String, Object>> arrivalsSearchList = arrivalsService.arrivalsSearch(searchVO);
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(arrivalsSearchList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        returnList.addAll(arrivalsSearchList);

        return AjaxUtils.success(returnList);
    }

    @GetMapping("arrivalsSearchByFlightNo")
    public AjaxUtils arrivalsSearchByFlightNo(String flightNo) {
        Arrivals arrivals = new Arrivals();
        arrivals.setFlightNo(flightNo);
        return AjaxUtils.success(arrivalsService.arrivalsSearchCount(arrivals));
    }

    @GetMapping("arrivalsToday")
    public AjaxUtils arrivalsToday(int pageNum, int pageSize) {
        List<Map<String,Object>> arrivalsTodayList = arrivalsService.arrivalsToday();

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(arrivalsTodayList);
        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("pageSize", String.valueOf(pageInfo.getSize()));
        pageMap.put("pageTotal", String.valueOf(pageInfo.getTotal()));
        pageMap.put("pageNum", String.valueOf(pageInfo.getPageNum()));
        pageMap.put("pagePages", String.valueOf(pageInfo.getPages()));

        List<Object> returnList = new ArrayList<>();
        returnList.add(pageMap);

        returnList.addAll(arrivalsTodayList);

        return AjaxUtils.success(returnList);
    }
}
