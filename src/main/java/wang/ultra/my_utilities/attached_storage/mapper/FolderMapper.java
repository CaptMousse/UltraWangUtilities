package wang.ultra.my_utilities.attached_storage.mapper;

import wang.ultra.my_utilities.attached_storage.entity.FolderEntity;
import wang.ultra.my_utilities.attached_storage.entity.vo.FolderVo;

import java.util.List;

public interface FolderMapper {
    void addFolder(List<FolderEntity> list);

    void updateFolder(FolderEntity entity);

    void deleteFolder(FolderEntity entity);

    List<FolderVo> getFolder(FolderEntity entity);
}
