package Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import Bean.User;
import DAO.UserDAO;
import application.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterController {
	@FXML private TextField emailTf;
	@FXML private TextField fullnameTf;
	@FXML private ToggleGroup genderGroup;
	@FXML private DatePicker birthdayPicker;
	@FXML private ChoiceBox<String> courseChoicebox;
	@FXML private PasswordField passwordPf;
	@FXML private TextField passwordTf;
	@FXML private CheckBox displayCheckbox;
	@FXML private Label messageLabel;
	@FXML private Hyperlink toHomeLink;
	@FXML private Button registerButton;
	
	private UserDAO userDAO = new UserDAO();
	private User loginedUser;
	
	public void initialize() {
		//Du lieu cho choicebox khoa hoc
		List<String> courses = Arrays.asList("Java", "Python", "C++", "Web Development");
		courseChoicebox.getItems().addAll(courses);
		courseChoicebox.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void onClickToHomeLink() {
		try {
			UserSession.createInstance(loginedUser.getEmail(), loginedUser.getFullname());
			
			//dong cua so hienj tai thong qua thanh phan con
			Stage registerStage = (Stage) emailTf.getScene().getWindow();
			registerStage.close();
			
			//tao root homeScreen
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomeScreen.fxml"));
			Parent root = loader.load();
			
			//tao stage
			Stage homeStage = new Stage();
			homeStage.setTitle("CLB TIN HỌC");
			homeStage.setScene(new Scene(root));
			homeStage.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	@FXML
	private void onClickDisplayCheckbox(ActionEvent event) {
		//Kiem tra trang thai
		boolean isChecked = displayCheckbox.isSelected();
		handlePasswordVisibility(isChecked);
	}
	
	private void handlePasswordVisibility(boolean isChecked) {
		if(isChecked) {
			passwordTf.setText(passwordPf.getText());
			passwordTf.setVisible(true);
			passwordPf.setVisible(false);
		}else {
			passwordPf.setText(passwordTf.getText());
			passwordPf.setVisible(true);
			passwordTf.setVisible(false);
		}
	}
	
	@FXML
	private void onClickRegisterBtn(ActionEvent event) {
		String email = emailTf.getText().trim();
		String fullname = fullnameTf.getText().trim();
		
		//TODO: lay sai du lieu khi nguoi dung an vao hien thi password 1 lan roi nhap tiep
		String password = displayCheckbox.isSelected() ? passwordTf.getText() : passwordPf.getText();
		
		LocalDate birthday = birthdayPicker.getValue();
		String gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
		String course = courseChoicebox.getValue();
		
		//Kiem tra tinh hop le cua cac truong du lieu
		String validationMessage = validateForm(email, fullname, birthday, password, course);
		if(validationMessage != null) {
			showMessage(false, validationMessage);
			return;
		}
		
		if(userDAO.getUserByEmail(email) != null) {
			showMessage(false, "Email đã tồn tại!");
			return;
		}
		
		//Tạo đối tượng User từ dữ liệu nhập vào
		User newUser = new User(email, fullname, gender.equals("Nam"), birthday, course, password);
		
		//luu tru de ghi vao session khi chuyen toi home
		loginedUser = newUser;
		
		//Them nguoi dung vao CSDL
		boolean isAdded = userDAO.addUser(newUser);
		if(isAdded) {
			showMessage(true, "Đăng ký thành công!");
		}else {
			showMessage(false, "Đăng ký thất bại");
		}
	}
	
	private String validateForm(String email, String fullname, LocalDate birthday, String password, String course) {
		if(email == null || email.isEmpty()) {
			return "Email không hợp lệ";
		}
		if(fullname == null || fullname.isEmpty() || !isEmailValid(email)) return "Vui lòng nhập họ tên";
		if(birthday == null) return "Vui lòng nhập ngày sinh!";
		
		LocalDate now = LocalDate.now();
		if(birthday.isAfter(now)) return "Ngày sinh phải là ngày trong quá khứ!";
		if(password == null || password.isEmpty()) return "Vui lòng nhập mật khẩu!";
		if(course == null || course.isEmpty()) return "Vui lòng chọn khóa học!";
		
		//Hop le
		return null;
	}
	
	private boolean isEmailValid(String email) {
		return email !=null && email.contains("@");
	}
	
	private void showMessage(boolean messageSuccess, String message) {
		if(messageSuccess) {
			messageLabel.setText(message);
			messageLabel.setTextFill(Color.BLUE);
			toHomeLink.setVisible(true);
		}else {
			messageLabel.setText(message);
			messageLabel.setTextFill(Color.RED);
			toHomeLink.setVisible(false);
		}
	}
}
