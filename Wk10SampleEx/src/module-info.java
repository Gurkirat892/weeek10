module Wk10SampleEx {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
