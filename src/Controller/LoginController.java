package Controller;

import Bean.User;
import DAO.UserDAO;
import application.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField emailTf;
	@FXML
	private PasswordField passwordPf;
	@FXML
	private Label errorLabel;
	@FXML
	private UserDAO userDAO;
	
	public void initialize() {
		userDAO = new UserDAO();
		errorLabel.setVisible(false);
	}
	
	@FXML
	private void onClickLoginBtn() {
		//Lay thong tin nhap tu giao dien
		String email = emailTf.getText().trim();
		String password = passwordPf.getText().trim();
		
		//kiem tra
		if (email.isEmpty() || password.isEmpty()) {
			errorLabel.setText("Email và mật khẩu không được để trống!");
			errorLabel.setVisible(true);
			return;
		}
		
		//lay thong tin nguoi dung
		User user = userDAO.getUserByEmail(email);
		if (user == null || !user.getPassword().equals(password)) {
			errorLabel.setText("Tài khoản nhập chưa đúng!");
			errorLabel.setVisible(true);
			return;
		}
		
		//Dang nhap thanh cong & tao phien user
		UserSession.createInstance(user.getEmail(), user.getFullname());
		errorLabel.setVisible(false);
		goHomeScreen();
	}
	
	@FXML
	private void onClickRegisterLink() {
		try {
			//Dong cua so hien tai
			Stage loginStage = (Stage) emailTf.getScene().getWindow();
			loginStage.close();
			
			//tao root cua so moi
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterForm.fxml"));
			Parent root = loader.load();
			
			//tao stage
			Stage homeStage = new Stage();
			homeStage.setScene(new Scene(root));
			homeStage.setTitle("Đăng Ký Khóa Học");
			homeStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	@FXML
	private void goHomeScreen() {
		try {
			//Dong cua so hien tai
			Stage loginStage = (Stage) emailTf.getScene().getWindow();
			loginStage.close();
			
			//tao cua so home
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomeScreen.fxml"));
			
			//tao root
			Parent root = loader.load();
			
			//tao stage home
			Stage homeStage = new Stage();
			homeStage.setTitle("CLB TIN HỌC");
			homeStage.setScene(new Scene(root));
			homeStage.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
}
