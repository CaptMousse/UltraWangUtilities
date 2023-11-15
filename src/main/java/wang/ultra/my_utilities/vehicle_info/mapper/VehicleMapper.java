package wang.ultra.my_utilities.vehicle_info.mapper;

import org.apache.ibatis.annotations.Mapper;
import wang.ultra.my_utilities.vehicle_info.entity.*;

import java.util.List;

@Mapper
public interface VehicleMapper {

    void addBrand(List<BrandEntity> entityList);

    void addYear(List<YearEntity> entityList);

    void addModel(List<ModelEntity> entityList);

    void addSpec(List<SpecEntity> entityList);

    void addSpecBodies(List<SpecBodiesEntity> entityList);

    void addSpecWheels(List<SpecWheelsEntity> entityList);
}
