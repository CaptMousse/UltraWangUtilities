package wang.ultra.my_utilities.vehicle_info.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.ultra.my_utilities.common.utils.AjaxUtils;
import wang.ultra.my_utilities.vehicle_info.apis.WheelSizeApi;
import wang.ultra.my_utilities.vehicle_info.service.VehicleService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/vehicleInfo")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @GetMapping("/getVehicleBrand")
    public AjaxUtils getVehicleBrand() {
        List<Map<String, String>> brandList = vehicleService.getBrand();
        return AjaxUtils.success(brandList);
    }

    @GetMapping("/getBrandYear")
    public AjaxUtils getBrandYear(String brand) {
        List<String> yearList = vehicleService.getBrandYear(brand);
        return AjaxUtils.success(yearList);
    }

    @GetMapping("/getYearModel")
    public AjaxUtils getYearModel(String brand, String year) {
        List<String> modelList = vehicleService.getYearModel(brand, year);
        return AjaxUtils.success(modelList);
    }

    @GetMapping("/getInfo")
    public AjaxUtils getVehicleInfo(String brand, String year, String model) {

        WheelSizeApi wheelSizeApi = new WheelSizeApi();
        List<Object> resultList = wheelSizeApi.getInfo(brand, year, model);

        return AjaxUtils.success(resultList);
    }
}
