package wang.ultra.my_utilities.attached_storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.ultra.my_utilities.attached_storage.entity.FolderEntity;
import wang.ultra.my_utilities.attached_storage.entity.vo.FolderVo;
import wang.ultra.my_utilities.attached_storage.mapper.FolderMapper;
import wang.ultra.my_utilities.common.utils.DateConverter;
import wang.ultra.my_utilities.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("folderService")
public class FolderService {

    @Autowired
    FolderMapper folderMapper;

    public int addFolder(FolderEntity entity) {
        entity.setFolderId(StringUtils.getMyUUID());
        if (null == entity.getParentId() || entity.getParentId().isEmpty()) {
            entity.setParentId("root");
        }
        entity.setCreator("shark");
        entity.setCreateTime(DateConverter.getNoSymbolTime());
        entity.setStatus("0");

        List<FolderEntity> folderList = new ArrayList<>();
        folderList.add(entity);

        try {
            folderMapper.addFolder(folderList);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    public int updateFolder(FolderEntity entity) {
        try {
            folderMapper.updateFolder(entity);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    public List<FolderVo> getFolders(FolderEntity entity) {
        List<FolderVo> folderList = folderMapper.getFolder(entity);
        return generateFolderTree(folderList);
    }

    /**
     * 生成目录树
     * @param folderList
     * @return
     */
    private List<FolderVo> generateFolderTree(List<FolderVo> folderList) {

        List<FolderVo> treeList = new ArrayList<>();
        for (FolderVo vo : folderList) {
            if ("root".equals(vo.getParentId())) {
                treeList.add(vo);
            }
            for (FolderVo child : folderList) {
                if (child.getParentId().equals(vo.getFolderId())) {
                    vo.addChild(child);
                }
            }
        }

        return treeList;
    }
}
