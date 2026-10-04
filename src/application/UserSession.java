package application;

public class UserSession {
	private static UserSession instance;
	
	private String email;
	private String fullname;
	
	private UserSession(String email, String fullname) {
		this.email = email;
		this.fullname = fullname;
	}
	
	public static void createInstance(String email, String fullname) {
		if(instance==null) {
			instance = new UserSession(email, fullname);
		}
	}
	
	public static UserSession getInstance() {
		if(instance==null) throw new IllegalStateException("UserSession chưa được tạo ra");
		return instance;
	}

	public String getEmail() {
		return email;
	}

	public String getFullname() {
		return fullname;
	}
	
	public void clearUserSession() {
		instance = null;
		fullname = null;
		email = null;
	}
}
