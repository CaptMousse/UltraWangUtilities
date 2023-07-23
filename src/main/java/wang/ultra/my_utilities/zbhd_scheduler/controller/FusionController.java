package wang.ultra.my_utilities.zbhd_scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.zbhd_scheduler.service.ArrivalsService;
import wang.ultra.my_utilities.zbhd_scheduler.service.DeparturesService;

@RestController
@CrossOrigin
@RequestMapping("/zbhd_scheduler/")
public class FusionController {

    @Autowired
    ArrivalsService arrivalsService;

    @Autowired
    DeparturesService departuresService;

    @GetMapping("getUpdateDate")
    public AjaxUtils getUpdateDate() {

        long departuresLastUpdateDate = Long.parseLong(departuresService.getLastUpdateDate());
        long arrivalsLastUpdateDate = Long.parseLong(arrivalsService.getLastUpdateDate());

        String result = String.valueOf(Math.max(departuresLastUpdateDate, arrivalsLastUpdateDate));

        Object resultObj = DateConverter.getDateFromStr(result);

        return AjaxUtils.success(resultObj);
    }


}
