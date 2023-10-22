package wang.ultra.my_utilities.vehicle_info.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.vehicle_info.apis.WheelSizeApi;
import wang.ultra.my_utilities.vehicle_info.entity.BrandEntity;
import wang.ultra.my_utilities.vehicle_info.entity.ModelEntity;
import wang.ultra.my_utilities.vehicle_info.entity.YearEntity;
import wang.ultra.my_utilities.vehicle_info.mapper.VehicleMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("vehicleService")
public class VehicleService {

    @Autowired
    VehicleMapper vehicleMapper;

    WheelSizeApi api = new WheelSizeApi();

    public List<Map<String, String>> getBrand() {
        List<Object> resultList = api.getBrand();
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        List<Map<String, String>> returnList = new ArrayList<>();
        List<BrandEntity> brandList = new ArrayList<>();
        for (Object o : resultList) {
            Map<String, String> tempMap = (Map<String, String>) o;
            BrandEntity brandEntity = new BrandEntity();
            Map<String, String> map = new HashMap<>();
            if (tempMap.containsKey("slug")) {
                map.put("id", tempMap.get("slug"));
                brandEntity.setId(tempMap.get("slug"));
            }
            if (tempMap.containsKey("name")) {
                map.put("brand", tempMap.get("name"));
                brandEntity.setBrandName(tempMap.get("name"));
            }
            returnList.add(map);
            brandEntity.setUpdateDate(DateConverter.getDate());
            brandList.add(brandEntity);
        }
        vehicleMapper.addBrand(brandList);
        return returnList;
    }

    public List<String> getBrandYear(String brand) {
        List<Object> resultList = api.getYear(brand);
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        List<String> yearList = new ArrayList<>();
        List<YearEntity> yearEntityList = new ArrayList<>();
        for (Object o : resultList) {
            Map<String, Object> yearMap = (Map<String, Object>) o;
            System.out.println("yearMap = " + yearMap);
            if (yearMap.containsKey("name")) {
                String year = String.valueOf(yearMap.get("name"));
                yearList.add(year);
                YearEntity yearEntity = new YearEntity();
                yearEntity.setId(brand + year);
                yearEntity.setBrandName(brand);
                yearEntity.setYear(year);
                yearEntity.setUpdateDate(DateConverter.getDate());
                yearEntityList.add(yearEntity);
            }
        }
        vehicleMapper.addYear(yearEntityList);
        return yearList;
    }

    public List<String> getYearModel(String brand, String year) {
        List<Object> resultList = api.getModel(brand, year);
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        List<String> modelList = new ArrayList<>();
        List<ModelEntity> modelEntityList = new ArrayList<>();
        for (Object o : resultList) {
            Map<String, Object> modelMap = (Map<String, Object>) o;
            if (modelMap.containsKey("name")) {
                String model = String.valueOf(modelMap.get("name"));
                modelList.add(model);
                ModelEntity modelEntity = new ModelEntity();
                modelEntity.setId(brand + year + model);
                modelEntity.setBrandName(brand);
                modelEntity.setYear(year);
                modelEntity.setModelName(model);
                modelEntity.setUpdateDate(DateConverter.getDate());
                modelEntityList.add(modelEntity);
            }
        }
        vehicleMapper.addModel(modelEntityList);
        return modelList;
    }


}
