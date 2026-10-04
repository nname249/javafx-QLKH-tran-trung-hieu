module JavaFx {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	
	opens Bean to javafx.base;
	opens application to javafx.graphics, javafx.fxml;
	opens Controller to javafx.graphics, javafx.fxml;
}
