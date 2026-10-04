package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Bean.User;

public class UserDAO {
	private Connection connection;
	
	public UserDAO() {
		try {
			String dbURL = "jdbc:ucanaccess://lib/QLKH.accdb";
			connection = DriverManager.getConnection(dbURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// lay danh sach tat ca nguoi dung
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		
		String query = "SELECT * FROM tblUser";
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				int userId = rs.getInt("UserID");
				String email = rs.getString("Email");
				String fullname = rs.getString("Fullname");
				boolean gender = rs.getBoolean("Gender");
				LocalDate birthday = rs.getDate("Birthday").toLocalDate();
				String course = rs.getString("Course");
				String password = rs.getString("Password");
				
				//tao doi tuong
				User user = new User(userId, email, fullname, gender, birthday, course, password);
				
				//them vao ds tra ve
				userList.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userList;
	}
	
	//Lay mot nguoi dung theo UserID
	public User getUserById(int userId) {
		User user = null;
		
		String query = "SELECT * FROM tblUser WHERE UserID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)){
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				String email = rs.getString("Email");
				String fullname = rs.getString("Fullname");
				boolean gender = rs.getBoolean("Gender");
				LocalDate birthday = rs.getDate("Birthday").toLocalDate();
				String course = rs.getString("Course");
				String password = rs.getString("Password");
				
				//tao doi tuong
				user = new User(userId, email, fullname, gender, birthday, course, password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public User getUserByEmail(String email) {
		User user = null;
		
		String query = "SELECT * FROM tblUser WHERE Email = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)){
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				int userId = rs.getInt("UserID");
				String fullname = rs.getString("Fullname");
				boolean gender = rs.getBoolean("Gender");
				LocalDate birthday = rs.getDate("Birthday").toLocalDate();
				String course = rs.getString("Course");
				String password = rs.getString("Password");
				
				//tao doi tuong
				user = new User(userId, email, fullname, gender, birthday, course, password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	//Them 1 nguoi dung moi vao DB
	public boolean addUser(User user) {
		String query = "INSERT INTO tblUser (Email, Fullname, Gender, Birthday, Course, Password) VALUES (?, ?, ?, ?, ?, ?)";
		
		try (PreparedStatement stmt = connection.prepareStatement(query)){
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getFullname());
			stmt.setBoolean(3, user.isGender());
			stmt.setDate(4, Date.valueOf(user.getBirthday()));
			stmt.setString(5, user.getCourse());
			stmt.setString(6, user.getPassword());
			
			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Cap nhat thong tin nguoi dung
	public boolean updateUser(User user) {
		String query = "UPDATE tblUser SET Email = ?, Fullname = ?, Gender = ?, Birthday = ?, Course = ?, Password = ? WHERE UserID = ?";
		
		try (PreparedStatement stmt = connection.prepareStatement(query)){
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getFullname());
			stmt.setBoolean(3, user.isGender());
			stmt.setDate(4, Date.valueOf(user.getBirthday()));
			stmt.setString(5, user.getCourse());
			stmt.setString(6, user.getPassword());
			stmt.setInt(7, user.getUserId());
			
			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Xoa nguoi dung theo UserID
	public boolean deleteUser(int userId) {
		String query = "DELETE FROM tblUser WHERE UserID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)){
			stmt.setInt(1, userId);
			
			int rowsDeleted = stmt.executeUpdate();
			return rowsDeleted > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
	
	
