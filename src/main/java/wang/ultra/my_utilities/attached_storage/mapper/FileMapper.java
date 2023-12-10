package wang.ultra.my_utilities.attached_storage.mapper;

import wang.ultra.my_utilities.attached_storage.entity.FileEntity;

import java.util.List;

public interface FileMapper {
    void addFile(List<FileEntity> list);

    void updateFile(FileEntity entity);

    void deleteFile(FileEntity entity);

    List<FileEntity> getFile(FileEntity entity);
}
