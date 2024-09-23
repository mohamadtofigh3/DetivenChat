package ir.detiven.detivenchat.modules.connection.objects;

public class ConnectionObject {

    private final String permission;

    private final String message;

    private final ConnectionAction action;

    public ConnectionObject(String permission, String message, ConnectionAction action) {
        this.permission = permission;
        this.message = message;
        this.action = action;
    }

    public ConnectionAction getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public String getPermission() {
        return permission;
    }
}