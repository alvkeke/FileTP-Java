package Server;

public interface CommandCallback {
    void refreshUserList();
    void sendFile(String deviceName, String fileFullPath);
}
