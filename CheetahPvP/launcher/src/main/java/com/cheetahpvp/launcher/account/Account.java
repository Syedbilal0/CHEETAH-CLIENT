package com.cheetahpvp.launcher.account;

/**
 * Represents a Minecraft account (offline or premium)
 */
public class Account {

    public enum Type {
        OFFLINE,
        PREMIUM
    }

    private String username;
    private Type type;
    private String uuid;
    private String accessToken;

    public Account(String username, Type type) {
        this.username = username;
        this.type = type;
        this.uuid = generateOfflineUUID(username);
        this.accessToken = "";
    }

    public Account(String username, Type type, String uuid, String accessToken) {
        this.username = username;
        this.type = type;
        this.uuid = uuid;
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public Type getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isOffline() {
        return type == Type.OFFLINE;
    }

    /**
     * Generate offline UUID from username (same method Minecraft uses)
     */
    private String generateOfflineUUID(String username) {
        return java.util.UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes())
                .toString().replace("-", "");
    }

    @Override
    public String toString() {
        return username + " (" + (type == Type.OFFLINE ? "Offline" : "Premium") + ")";
    }
}
