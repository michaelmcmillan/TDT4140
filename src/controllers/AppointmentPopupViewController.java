package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import models.Appointment;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Morten on 02.03.15.
 */
public class AppointmentPopupViewController extends AnchorPane implements Initializable {

    @FXML private TextArea purposeTextArea;
    @FXML private TextField roomTextField;
    @FXML private DatePicker fromDatePicker;
    @FXML private TextField fromTimeTextField;
    @FXML private TextField toTimeTextField;
    @FXML private CheckBox repetitionCheckbox;
    @FXML private Label repetitionFrequencyLabel;
    @FXML private TextField repetitionFrequencyTextField;
    @FXML private Label endDateLabel;
    @FXML private DatePicker endDatePicker;
    private Appointment model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Appointment();
        removeEndDayForm(); // Don't show calendar repetition events at startup
        //initListeners();
    }

    @FXML void addAppointmentButtonPressed(ActionEvent event) {

    }

    @FXML private void repetitionCheckboxStateChanged(ActionEvent event) {
        if(repetitionCheckbox.isSelected()) {
            showEndDayForm();
        } else {
            removeEndDayForm();
        }
    }

    private void showEndDayForm() {
        repetitionFrequencyLabel.setVisible(true);
        repetitionFrequencyTextField.setVisible(true);
        endDateLabel.setVisible(true);
        endDatePicker.setVisible(true);
    }

    private void removeEndDayForm() {
        repetitionFrequencyLabel.setVisible(false);
        repetitionFrequencyTextField.setVisible(false);
        endDateLabel.setVisible(false);
        endDatePicker.setVisible(false);
    }
}
