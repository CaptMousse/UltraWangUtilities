package wang.ultra.my_utilities.attached_storage.entity.vo;

import lombok.Data;
import wang.ultra.my_utilities.attached_storage.entity.FolderEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class FolderVo extends FolderEntity {
    private List<FolderVo> children;
    public void addChild(FolderVo vo) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(vo);
    }
}
