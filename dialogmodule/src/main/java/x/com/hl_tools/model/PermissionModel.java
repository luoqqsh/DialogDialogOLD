package x.com.hl_tools.model;

public class PermissionModel {
    private String name = "";
    private boolean isapply = false;
    private String permission;

    public PermissionModel(String name, boolean isapply,String permission) {
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
