package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private UserDao userDao = new UserDao();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Registration Form");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Registration Form");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);
        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 2);
        TextField lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 2);

        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 3);
        TextField firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 3);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 4);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 4);

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 5);
        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 5);

        Label dobLabel = new Label("Date of Birth:");
        grid.add(dobLabel, 0, 6);
        DatePicker dobPicker = new DatePicker();
        grid.add(dobPicker, 1, 6);

        Label genderLabel = new Label("Gender:");
        grid.add(genderLabel, 0, 7);
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        RadioButton femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);
        RadioButton otherRadio = new RadioButton("Other");
        otherRadio.setToggleGroup(genderGroup);
        HBox genderBox = new HBox(10, maleRadio, femaleRadio, otherRadio);
        grid.add(genderBox, 1, 7);

        Label courseLabel = new Label("Course:");
        grid.add(courseLabel, 0, 8);
        ComboBox<String> courseComboBox = new ComboBox<>();
        courseComboBox.getItems().addAll("Course 1", "Course 2", "Course 3");
        grid.add(courseComboBox, 1, 8);

        Button saveButton = new Button("Save");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(saveButton);
        grid.add(hBox, 1, 9);

        saveButton.setOnAction(actionEvent -> {
            String username = usernameTextField.getText().trim();
            String lastName = lastNameTextField.getText().trim();
            String firstName = firstNameTextField.getText().trim();
            String password = passwordField.getText();
            String email = emailTextField.getText().trim();
            String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";
            String gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
            String course = courseComboBox.getValue();

            if (!StringPool.BLANK.equals(username) && !StringPool.BLANK.equals(lastName)
                    && !StringPool.BLANK.equals(firstName) && !StringPool.BLANK.equals(password)
                    && !StringPool.BLANK.equals(email) && !StringPool.BLANK.equals(dob)
                    && genderGroup.getSelectedToggle() != null && course != null) {
                try {
                    if (!userDao.userExists(username)) {
                        User user = this.createUserObject(username, lastName, firstName, password, email, dob, gender, course);
                        int userId = userDao.saveUser(user);
                        if (userId > 0) {
                            this.alert("Save", "Successful!", Alert.AlertType.INFORMATION);
                        } else {
                            this.alert("Error", "Failed!", Alert.AlertType.ERROR);
                        }
                    } else {
                        this.alert("Error", "User already exists!", Alert.AlertType.ERROR);
                    }
                } catch (Exception exception) {
                    logger.log(Level.SEVERE, exception.getMessage());
                }
            } else {
                this.alert("Error", "Please complete fields!", Alert.AlertType.ERROR);
            }
        });

        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void alert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public User createUserObject(String username, String lastName, String firstName, String password, String email, String dob, String gender, String course) {
        User user = new User();
        user.setUsername(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setEmail(email);
        user.setDateOfBirth(dob);
        user.setGender(gender);
        user.setCourse(course);
        return user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}