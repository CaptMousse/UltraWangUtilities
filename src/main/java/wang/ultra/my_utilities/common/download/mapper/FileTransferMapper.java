package wang.ultra.my_utilities.common.download.mapper;

import org.apache.ibatis.annotations.Param;
import wang.ultra.my_utilities.common.download.entity.FileTransferEntity;

import java.util.List;
import java.util.Map;

public interface FileTransferMapper {

    void fileAdd(List<FileTransferEntity> fileList);

    void fileUpdate(FileTransferEntity fileTransferEntity);

    void fileAmountCount(@Param("id")String id);

    void fileDeleteById(@Param("id") String id);

    List<Map<String, Object>> fileSelectByShowName(@Param("showName") String showName);

    List<Map<String, Object>> fileSelectByRealName(@Param("realName") String realName);

    List<Map<String, Object>> getFileByCleaner(@Param("createTime") String createTime);
}
