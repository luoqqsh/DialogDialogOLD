package x.com.hl_tools.model;

import java.io.Serializable;

/**
 * 权限申请相关信息，权限信息对话框用
 */
public class PermissionModel implements Serializable {
    private String name;//权限说明
    private boolean isapply;//是否同意
    private String permission;//权限名

    public PermissionModel(String name, boolean isapply, String permission) {
        this.name = name;
        this.isapply = isapply;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsapply() {
        return isapply;
    }

    public void setIsapply(boolean isapply) {
        this.isapply = isapply;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
