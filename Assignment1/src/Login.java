public class Login {

    public boolean verifyUserName(String username) {
        if (username == null) {
            return false;
        }
        int length = username.length();
        return length >= 6 && length <= 12;
    }

    public boolean verifyPassword(String password) {
        return false; // TODO
    }

    public boolean login(String username, String password) {
        return false; // TODO
    }
}