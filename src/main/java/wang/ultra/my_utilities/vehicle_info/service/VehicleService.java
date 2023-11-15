package wang.ultra.my_utilities.vehicle_info.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;
import wang.ultra.my_utilities.vehicle_info.apis.WheelSizeApi;
import wang.ultra.my_utilities.vehicle_info.entity.*;
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
                yearEntity.setId(brand + "_" + year);
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
                modelEntity.setId(brand + "_" + year + "_" + model);
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

    public List<Map<String, Object>> getModelSpec(String brand, String year, String model) {
        List<Object> resultList = api.getSpec(brand, year, model);
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> specList = new ArrayList<>();
        List<SpecEntity> specEntityList = new ArrayList<>();

        for (Object o : resultList) {
            SpecEntity specEntity = new SpecEntity();
            Map<String, Object> specMap = new HashMap<>();
            Map<String, Object> resultMap = (Map<String, Object>) o;
            for (String key : resultMap.keySet()) {
                specEntity.setId(brand + "_" + year + "_" + model);
                specEntity.setModelId(brand + "_" + year + "_" + model);
                specEntity.setUpdateDate(DateConverter.getDate());
                if ("generation".equals(key)) {
                    Map<String, Object> generationMap = (Map<String, Object>) resultMap.get(key);
                    for (String genKey : generationMap.keySet()) {
                        if ("name".equals(genKey)) {
                            specEntity.setGenerationName(String.valueOf(generationMap.get(genKey)));
                            specMap.put("generation_name", generationMap.get(genKey));
                        } else if (("start_year").equals(genKey)) {
                            specEntity.setGenerationStartYear(String.valueOf(generationMap.get(genKey)));
                            specMap.put("generation_start_year", generationMap.get(genKey));
                        } else if (("end_year").equals(genKey)) {
                            specEntity.setGenerationEndYear(String.valueOf(generationMap.get(genKey)));
                            specMap.put("generation_end_year", generationMap.get(genKey));
                        } else if ("years".equals(genKey)) {
                            specEntity.setGenerationYears(String.valueOf(generationMap.get(genKey)));
                            specMap.put("generation_years", generationMap.get(genKey));
                        } else if (("bodies").equals(genKey)) {
                            List<SpecBodiesEntity> specBodiesEntityList = new ArrayList<>();
                            List<Map<String, String>> bodiesList = (List<Map<String, String>>) generationMap.get(genKey);
                            for (Map<String, String> bodiesMap : bodiesList) {
                                SpecBodiesEntity specBodiesEntity = new SpecBodiesEntity();
                                specBodiesEntity.setId(brand + "_" + year + "_" + model + "_" + resultMap.get("trim") + "_" + bodiesMap.get("title"));
                                specBodiesEntity.setSpecId(brand + "_" + year + "_" + model + "_" + resultMap.get("trim"));
                                specBodiesEntity.setBodyTitle(bodiesMap.get("title"));
                                specBodiesEntity.setImage(bodiesMap.get("image"));
                                specBodiesEntity.setUpdateDate(DateConverter.getDate());
                                specBodiesEntityList.add(specBodiesEntity);
                            }
                            specEntity.setBodiesList(specBodiesEntityList);
                            specMap.put("generation_bodies", bodiesList);
                        }
                    }
                } else if ("market".equals(key)) {
                    Map<String, String> marketMap = (Map<String, String>) resultMap.get(key);
                    for (String marketKey : marketMap.keySet()) {
                        if ("abbr".equals(marketKey)) {
                            specEntity.setMarketAbbr(marketMap.get(marketKey));
                            specMap.put("market_abbr", marketMap.get(marketKey));
                        } else if ("name".equals(marketKey)) {
                            specEntity.setMarketName(marketMap.get(marketKey));
                            specMap.put("market_name", marketMap.get(marketKey));
                        }
                    }
                } else if ("power".equals(key)) {
                    Map<String, Object> powerMap = (Map<String, Object>) resultMap.get(key);
                    for (String powerKey : powerMap.keySet()) {
                        if ("PS".equals(powerKey)) {
                            specEntity.setPowerPs(String.valueOf(powerMap.get(powerKey)));
                            specMap.put("power_ps", powerMap.get(powerKey));
                        } else if ("hp".equals(powerKey)) {
                            specEntity.setPowerHp(String.valueOf(powerMap.get(powerKey)));
                            specMap.put("power_ps", powerMap.get(powerKey));
                        } else if ("kw".equals(powerKey)) {
                            specEntity.setPowerKw(String.valueOf(powerMap.get(powerKey)));
                            specMap.put("power_kw", powerMap.get(powerKey));
                        }
                    }
                } else if ("wheels".equals(key)) {
                    List<Map<String, Object>> wheelsList = (List<Map<String, Object>>) resultMap.get(key);
                    List<SpecWheelsEntity> specWheelsEntityList = new ArrayList<>();
                    List<Map<String, String>> specWheelsList = new ArrayList<>();
                    for (Map<String, Object> wheelsMap : wheelsList) {
                        SpecWheelsEntity specWheelsEntity = new SpecWheelsEntity();
                        Map<String, String> specWheelMap = new HashMap<>();
                        for (String wheelsKey : wheelsMap.keySet()) {
                            if ("front".equals(wheelsKey)) {
                                Map<String, Object> frontMap = (Map<String, Object>) wheelsMap.get(wheelsKey);
                                for (String frontKey : frontMap.keySet()) {
                                    if ("load_index".equals(frontKey)) {
                                        specWheelsEntity.setFrontLoadingIndex(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_load_index", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("rim".equals(frontKey)) {
                                        specWheelsEntity.setId(StringUtils.getMyUUID());
                                        specWheelsEntity.setSpecId(brand + "_" + year + "_" + model);
                                        specWheelsEntity.setUpdateDate(DateConverter.getDate());
                                        specWheelsEntity.setFrontRim(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_rim", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("rim_diameter".equals(frontKey)) {
                                        specWheelsEntity.setFrontRimDiameter(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_rim_diameter", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("rim_offset".equals(frontKey)) {
                                        specWheelsEntity.setFrontRimOffset(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_rim_offset", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("rim_width".equals(frontKey)) {
                                        specWheelsEntity.setFrontRimWidth(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_rim_width", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("speed_index".equals(frontKey)) {
                                        specWheelsEntity.setFrontSpeedIndex(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_speed_index", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire".equals(frontKey)) {
                                        specWheelsEntity.setFrontTire(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire_aspect_ratio".equals(frontKey)) {
                                        specWheelsEntity.setFrontTireAspectRatio(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire_aspect_ratio", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire_construction".equals(frontKey)) {
                                        specWheelsEntity.setFrontTireConstruction(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire_construction", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire_diameter".equals(frontKey)) {
                                        specWheelsEntity.setFrontTireDiameter(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire_diameter", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire_is_82series".equals(frontKey)) {
                                        specWheelsEntity.setFrontTireIs82series(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire_is_82series", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire_pressure".equals(frontKey)) {
                                        Map<String, Object> tirePressureMap = (Map<String, Object>) frontMap.get(frontKey);
                                        if (tirePressureMap != null && !tirePressureMap.isEmpty()) {
                                            for (String tirePressureKey : tirePressureMap.keySet()) {
                                                if ("bar".equals(tirePressureKey)) {
                                                    specWheelsEntity.setFrontTirePressureBar(String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                    specWheelMap.put("front_tire_pressure_bar", String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                } else if ("kPa".equals(tirePressureKey)) {
                                                    specWheelsEntity.setFrontTirePressureKpa(String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                    specWheelMap.put("front_tire_pressure_kPa", String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                } else if ("psi".equals(tirePressureKey)) {
                                                    specWheelsEntity.setFrontTirePressurePsi(String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                    specWheelMap.put("front_tire_pressure_psi", String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                }
                                            }
                                        }
                                    } else if ("tire_section_width".equals(frontKey)) {
                                        specWheelsEntity.setFrontTireSectionWidth(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire_section_width", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire_sizing_system".equals(frontKey)) {
                                        specWheelsEntity.setFrontTireSizingSystem(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire_sizing_system", String.valueOf(frontMap.get(frontKey)));
                                    } else if ("tire_width".equals(frontKey)) {
                                        specWheelsEntity.setFrontTireWidth(String.valueOf(frontMap.get(frontKey)));
                                        specWheelMap.put("front_tire_width", String.valueOf(frontMap.get(frontKey)));
                                    }


                                }
                            } else if ("rear".equals(wheelsKey)) {
                                Map<String, Object> rearMap = (Map<String, Object>) wheelsMap.get(wheelsKey);
                                for (String rearKey : rearMap.keySet()) {
                                    if ("load_index".equals(rearKey)) {
                                        specWheelsEntity.setRearLoadingIndex(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("load_index", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("rim".equals(rearKey)) {
                                        specWheelsEntity.setRearRim(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("rim", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("rim_diameter".equals(rearKey)) {
                                        specWheelsEntity.setRearRimDiameter(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("rim_diameter", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("rim_offset".equals(rearKey)) {
                                        specWheelsEntity.setRearRimOffset(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("rim_offset", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("rim_width".equals(rearKey)) {
                                        specWheelsEntity.setRearRimWidth(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("rim_width", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("speed_index".equals(rearKey)) {
                                        specWheelsEntity.setRearSpeedIndex(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("speed_index", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire".equals(rearKey)) {
                                        specWheelsEntity.setRearTire(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire_aspect_ratio".equals(rearKey)) {
                                        specWheelsEntity.setRearTireAspectRatio(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire_aspect_ratio", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire_construction".equals(rearKey)) {
                                        specWheelsEntity.setRearTireConstruction(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire_construction", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire_diameter".equals(rearKey)) {
                                        specWheelsEntity.setRearTireDiameter(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire_diameter", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire_is_82series".equals(rearKey)) {
                                        specWheelsEntity.setRearTireIs82series(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire_is_82series", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire_pressure".equals(rearKey)) {
                                        Map<String, Object> tirePressureMap = (Map<String, Object>) rearMap.get(rearKey);
                                        if (tirePressureMap != null && !tirePressureMap.isEmpty()) {
                                            for (String tirePressureKey : tirePressureMap.keySet()) {
                                                if ("bar".equals(tirePressureKey)) {
                                                    specWheelsEntity.setRearTirePressureBar(String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                    specWheelMap.put("rear_tire_pressure_bar", String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                } else if ("kPa".equals(tirePressureKey)) {
                                                    specWheelsEntity.setRearTirePressureKpa(String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                    specWheelMap.put("rear_tire_pressure_kPa", String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                } else if ("psi".equals(tirePressureKey)) {
                                                    specWheelsEntity.setRearTirePressurePsi(String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                    specWheelMap.put("rear_tire_pressure_psi", String.valueOf(tirePressureMap.get(tirePressureKey)));
                                                }
                                            }
                                        }
                                    } else if ("tire_section_width".equals(rearKey)) {
                                        specWheelsEntity.setRearTireSectionWidth(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire_section_width", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire_sizing_system".equals(rearKey)) {
                                        specWheelsEntity.setRearTireSizingSystem(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire_sizing_system", String.valueOf(rearMap.get(rearKey)));
                                    } else if ("tire_width".equals(rearKey)) {
                                        specWheelsEntity.setRearTireWidth(String.valueOf(rearMap.get(rearKey)));
                                        specWheelMap.put("tire_width", String.valueOf(rearMap.get(rearKey)));
                                    }
                                }
                            } else if ("is_pressed_steel_rims".equals(wheelsKey)) {
                                specWheelsEntity.setIsPressedSteelRims(String.valueOf(wheelsMap.get(wheelsKey)));
                                specWheelMap.put("is_pressed_steel_rims", String.valueOf(wheelsMap.get(wheelsKey)));
                            } else if ("is_recommended_for_winter".equals(wheelsKey)) {
                                specWheelsEntity.setIsRecommendedForWinter(String.valueOf(wheelsMap.get(wheelsKey)));
                                specWheelMap.put("is_recommended_for_winter", String.valueOf(wheelsMap.get(wheelsKey)));
                            } else if ("is_runflat_tires".equals(wheelsKey)) {
                                specWheelsEntity.setIsRunflatTires(String.valueOf(wheelsMap.get(wheelsKey)));
                                specWheelMap.put("is_runflat_tires", String.valueOf(wheelsMap.get(wheelsKey)));
                            } else if ("is_stock".equals(wheelsKey)) {
                                specWheelsEntity.setIsStock(String.valueOf(wheelsMap.get(wheelsKey)));
                                specWheelMap.put("is_stock", String.valueOf(wheelsMap.get(wheelsKey)));
                            } else if ("showing_fp_only".equals(wheelsKey)) {
                                specWheelsEntity.setShowingFpOnly(String.valueOf(wheelsMap.get(wheelsKey)));
                                specWheelMap.put("showing_fp_only", String.valueOf(wheelsMap.get(wheelsKey)));
                            }
                        }
                        specWheelsEntityList.add(specWheelsEntity);
                        specWheelsList.add(specWheelMap);
                    }
                    specEntity.setWheelsList(specWheelsEntityList);
                    specMap.put("wheels", specWheelsList);
                } else if ("body".equals(key)) {
                    specEntity.setBody(String.valueOf(resultMap.get(key)));
                    specMap.put("body", resultMap.get(key));
                } else if ("bolt_pattern".equals(key)) {
                    specEntity.setFrontAxisBoltPattern(String.valueOf(resultMap.get(key)));
                    specMap.put("front_axis_bolt_pattern", resultMap.get(key));
                } else if ("centre_bore".equals(key)) {
                    specEntity.setFrontAxisCentreBore(String.valueOf(resultMap.get(key)));
                    specMap.put("front_axis_centre_bore", resultMap.get(key));
                } else if ("engine_code".equals(key)) {
                    specEntity.setEngineCode(String.valueOf(resultMap.get(key)));
                    specMap.put("engine_code", resultMap.get(key));
                } else if ("engine_type".equals(key)) {
                    specEntity.setEngineType(String.valueOf(resultMap.get(key)));
                    specMap.put("engine_type", resultMap.get(key));
                } else if ("fuel".equals(key)) {
                    specEntity.setFuel(String.valueOf(resultMap.get(key)));
                    specMap.put("fuel", resultMap.get(key));
                } else if ("lock_text".equals(key)) {
                    specEntity.setLockText(String.valueOf(resultMap.get(key)));
                    specMap.put("lock_text", resultMap.get(key));
                } else if ("lock_type".equals(key)) {
                    specEntity.setLockType(String.valueOf(resultMap.get(key)));
                    specMap.put("lock_type", resultMap.get(key));
                } else if ("pcd".equals(key)) {
                    specEntity.setFrontAxisPcd(String.valueOf(resultMap.get(key)));
                    specMap.put("front_axis_pcd", resultMap.get(key));
                } else if ("rear_axis_bolt_pattern".equals(key)) {
                    specEntity.setRearAxisBoltPattern(String.valueOf(resultMap.get(key)));
                    specMap.put("rear_axis_bolt_pattern", resultMap.get(key));
                } else if ("rear_axis_centre_bore".equals(key)) {
                    specEntity.setRearAxisCentreBore(String.valueOf(resultMap.get(key)));
                    specMap.put("rear_axis_centre_bore", resultMap.get(key));
                } else if ("rear_axis_pcd".equals(key)) {
                    specEntity.setRearAxisPcd(String.valueOf(resultMap.get(key)));
                    specMap.put("rear_axis_pcd", resultMap.get(key));
                } else if ("rear_axis_stud_holes".equals(key)) {
                    specEntity.setRearAxisStudHoles(String.valueOf(resultMap.get(key)));
                    specMap.put("rear_axis_stud_holes", resultMap.get(key));
                } else if ("stud_holes".equals(key)) {
                    specEntity.setFrontAxisStudHoles(String.valueOf(resultMap.get(key)));
                    specMap.put("stud_holes", resultMap.get(key));
                } else if ("trim".equals(key)) {
                    specEntity.setTrim(String.valueOf(resultMap.get(key)));
                    specMap.put("trim", resultMap.get(key));
                } else if ("trim_attributes".equals(key)) {
                    specEntity.setTrimAttributes(String.valueOf(resultMap.get(key)));
                    specMap.put("trim_attributes", resultMap.get(key));
                } else if ("trim_body_types".equals(key)) {
                    specEntity.setTrimBodyTypes(String.valueOf(resultMap.get(key)));
                    specMap.put("trim_body_types", resultMap.get(key));
                }
            }
            specList.add(specMap);
            specEntityList.add(specEntity);
        }


        // 遍历持久化
        for (SpecEntity specEntity : specEntityList) {
            List<SpecBodiesEntity> specBodiesEntityList = specEntity.getBodiesList();
            vehicleMapper.addSpecBodies(specBodiesEntityList);
            specEntity.setBodiesList(null);
            List<SpecWheelsEntity> specWheelsEntityList = specEntity.getWheelsList();
            vehicleMapper.addSpecWheels(specWheelsEntityList);
            specEntity.setWheelsList(null);
        }
        vehicleMapper.addSpec(specEntityList);


        return specList;
    }

}
