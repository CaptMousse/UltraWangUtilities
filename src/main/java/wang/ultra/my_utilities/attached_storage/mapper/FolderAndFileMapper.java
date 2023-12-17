package wang.ultra.my_utilities.attached_storage.mapper;

import wang.ultra.my_utilities.attached_storage.entity.vo.FolderAndFileVo;

import java.util.List;

public interface FolderAndFileMapper {

    List<FolderAndFileVo> getFolderAndFile(String folderId);
}
