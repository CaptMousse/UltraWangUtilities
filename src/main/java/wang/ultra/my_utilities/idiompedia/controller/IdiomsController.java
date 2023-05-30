package wang.ultra.my_utilities.idiompedia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.idiompedia.service.MapperService;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/idioms")
public class IdiomsController {

    @Autowired
    MapperService mapperService;

    @GetMapping("/insert")
    public String insert() {
        Long timeStart = System.currentTimeMillis();
        System.out.println("timeStart = " + timeStart);
        mapperService.csvFile2Mapper("WabbyWabbo", "idiom.csv");
        Long timeStop = System.currentTimeMillis();
        System.out.println("timeStop = " + timeStop);
        Long timeTotal = timeStop - timeStart;

        String total;
        String timeTotalStr = String.valueOf(timeTotal);
        if(timeTotalStr.length() <= 3) {
            total = "timeTotal = " + timeTotalStr + "ms";
        } else {
            StringBuilder sb = new StringBuilder(timeTotalStr);
            sb.insert(timeTotalStr.length() - 3, ".");
            total = "timeTotal = " + sb + "s";
        }        
        System.out.println(total);

        return total;
    }

    @GetMapping("/selectByWord")
    public String selectByWord(String word) {
        return mapperService.idiomsSearchByWord(word).toString();
    }

    @GetMapping("/selectBySort")
    public List<Map<String, Object>> selectBySort(String sort, String word) {
        return mapperService.idiomsSearchBySort(Integer.parseInt(sort), word);
    }

}
