public class Login {

	public boolean verifyUserName(String username) {
	    return username != null
	        && username.length() >= 6
	        && username.length() <= 12;
	}

    public boolean verifyPassword(String password) {
        if (password == null) {
            return false;
        }
        return password.matches("\\d{4,6}");
    }

    public boolean login(String username, String password) {
    	return verifyUserName(username) && verifyPassword(password);
    }
}