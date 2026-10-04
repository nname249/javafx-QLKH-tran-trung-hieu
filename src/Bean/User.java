package Bean;

import java.time.LocalDate;

public class User {
	private int userId;
	private String email;
	private String fullname;
	private boolean gender;
	private LocalDate birthday;
	private String course;
	private String password;
	
	public User(int userId, String email, String fullname, boolean gender, LocalDate birthday, String course,
			String password) {
		super();
		this.userId = userId;
		this.email = email;
		this.fullname = fullname;
		this.gender = gender;
		this.birthday = birthday;
		this.course = course;
		this.password = password;
	}

	public User(String email, String fullname, boolean gender, LocalDate birthday, String course, String password) {
		super();
		this.email = email;
		this.fullname = fullname;
		this.gender = gender;
		this.birthday = birthday;
		this.course = course;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserId() {
		return userId;
	}
}
