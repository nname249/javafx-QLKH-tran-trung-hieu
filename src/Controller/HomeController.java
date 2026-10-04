package Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import Bean.User;
import DAO.UserDAO;
import application.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HomeController {
	@FXML private TableView<User> userListTableView;
	@FXML private TableColumn<User, Integer> idCol;
	@FXML private TableColumn<User, String> emailCol;
	@FXML private TableColumn<User, String> fullnameCol;
	@FXML private TableColumn<User, String> courseCol;
	
	@FXML private Label welcomeLabel;
	@FXML private TextField emailTf;
	@FXML private TextField fullnameTf;
	@FXML private DatePicker birthdayPicker;
	@FXML private ChoiceBox<String> courseChoicebox;
	@FXML private RadioButton maleRadioButton;
	@FXML private RadioButton femaleRadioButton;
	@FXML private ToggleGroup genderGroup;
	
	//layout stackpane chua 2 truong mat khau
	@FXML private StackPane passwordStackPane;
	@FXML private PasswordField passwordPf;
	@FXML private TextField passwordTf;
	@FXML private CheckBox displayCheckbox;
	
	//cac nut chuc nang
	@FXML private Button addBtn;
	@FXML private Button updateBtn;
	@FXML private Button deleteBtn;
	
	private UserDAO userDAO = new UserDAO();
	
	private UserSession userSession;
	
	public void initialize() {
		userSession = UserSession.getInstance();
		
		welcomeLabel.setText("Xin chào "+userSession.getFullname());
		
		//Bat cac nut hanh dong them sua xoa tuy theo quyen user
		if("admin@gmail.com".equals(userSession.getEmail())) {
			addBtn.setDisable(false);
			updateBtn.setDisable(false);
			deleteBtn.setDisable(false);
			passwordStackPane.setDisable(false);
			displayCheckbox.setDisable(false);
		}
		
		//Bat cap cac cot cua tableview voi cac truong cua doi tuong User
		idCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("userId"));
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		fullnameCol.setCellValueFactory(new PropertyValueFactory<User, String>("fullname"));
		courseCol.setCellValueFactory(new PropertyValueFactory<User, String>("course"));
		
		//Lay danh sach nguoi dung va hien thi
		loadUserList();
		
		//khoi tao du lieu cho choicebox khoa hoc
		List<String> courses = Arrays.asList("Java", "Python", "C++", "Web Development");
		courseChoicebox.getItems().addAll(courses);
	}
	
	private void loadUserList() {
		ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAllUsers());
		userListTableView.setItems(users);
	}
	
	@FXML
	private void onClickLogout(ActionEvent event) {
		try {
			userSession.clearUserSession();
			
			//lay doi tuong stage tu scene hien tai thong qua doi tuong event
			Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			currentStage.close();
			
			//Mo trang login tao root
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginForm.fxml"));
			Parent root = loader.load();
			
			//tao stage
			Stage loginStage = new Stage();
			loginStage.setTitle("Đăng nhập");
			loginStage.setScene(new Scene(root));
			loginStage.show();
		} catch (Exception e) {
			// TODO: handle exception
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
	private void onClickAddBtn() {
		String email = emailTf.getText();
		String fullname = fullnameTf.getText();
		boolean gender = maleRadioButton.isSelected();
		LocalDate birthday = birthdayPicker.getValue();
		String course = courseChoicebox.getValue();
		String password = displayCheckbox.isSelected() ? passwordTf.getText() : passwordPf.getText();
		
		User user = new User(email, fullname, gender, birthday, course, password);
		
		if(userDAO.addUser(user)) {
			loadUserList();
			showAlert(AlertType.INFORMATION, "Thành công", "Thêm người dùng thành công");
			clearForm();
		}else {
			showAlert(AlertType.ERROR, "Lỗi", "Lỗi khi thêm người dùng");
		}
	}
	
	private Optional<ButtonType> showAlert(AlertType type, String title, String desc) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(desc);
		return alert.showAndWait();
	}
	
	private void clearForm() {
		emailTf.clear();
		fullnameTf.clear();
		passwordPf.clear();
		passwordTf.clear();
		courseChoicebox.setValue(null);
		maleRadioButton.setSelected(false);
		femaleRadioButton.setSelected(false);
	}
	
	@FXML 
	private void onClickUpdateBtn() {
		User selectedUser = userListTableView.getSelectionModel().getSelectedItem();
		if(selectedUser == null) {
			showAlert(AlertType.ERROR, "Lỗi", "Chưa chọn người dùng để cập nhật");
			return;
		}
		
		selectedUser.setEmail(emailTf.getText().trim());
		selectedUser.setFullname(fullnameTf.getText().trim());
		selectedUser.setGender(maleRadioButton.isSelected());
		selectedUser.setBirthday(birthdayPicker.getValue());
		selectedUser.setCourse(courseChoicebox.getValue());
		selectedUser.setPassword(displayCheckbox.isSelected() ? passwordTf.getText() : passwordPf.getText());
		
		if(!userDAO.updateUser(selectedUser)) {
			showAlert(AlertType.ERROR, "Lỗi", "Cập nhật người dùng thất bại!");
			return;
		}
		
		//thành công
		showAlert(AlertType.INFORMATION, "Thành công", "Cập nhật người dùng thành công!");
		loadUserList();
		clearForm();
		
	}
	
	@FXML
	private void onClickDeleteBtn() {
		User selectedUser = userListTableView.getSelectionModel().getSelectedItem();
		if(selectedUser == null) {
			showAlert(AlertType.ERROR, "Lỗi", "Chưa chọn người dùng để xóa!");
			return;
		}
		
		Optional<ButtonType> option = showAlert(AlertType.CONFIRMATION, "Xác minh", "Bạn chắc chắn muốn xóa?");
		if(option.get() == ButtonType.OK) {
			if(userDAO.deleteUser(selectedUser.getUserId())) {
				showAlert(AlertType.INFORMATION, "Thành công", "Xóa user thành công");
				loadUserList();
				clearForm();
			}else {
				showAlert(AlertType.ERROR, "Lỗi", "Xóa người dùng thất bại!");
			}
		}
	}
	
	@FXML 
	private void onClickRow() {
		User userSelected = userListTableView.getSelectionModel().getSelectedItem();
		if(userSelected != null) {
			showUserDetails(userSelected);
		}
	}
	
	private void showUserDetails(User user) {
		emailTf.setText(user.getEmail());
		fullnameTf.setText(user.getFullname());
		courseChoicebox.setValue(user.getCourse());
		if(user.isGender()) {
			maleRadioButton.setSelected(true);
		}else {
			femaleRadioButton.setSelected(true);
		}
		birthdayPicker.setValue(user.getBirthday());
		
		passwordPf.setText(user.getPassword());
		passwordTf.setText(user.getPassword());
		
		if(!"admin@gmail.com".equals(userSession.getEmail())) {
			if(user.getEmail().equals(userSession.getEmail())) {
				updateBtn.setDisable(false);
				passwordStackPane.setDisable(false);
				displayCheckbox.setDisable(false);
			}else {
				passwordTf.clear();
				passwordPf.clear();
				updateBtn.setDisable(true);
				passwordStackPane.setDisable(true);
				displayCheckbox.setDisable(true);
			}
		}
	}
}