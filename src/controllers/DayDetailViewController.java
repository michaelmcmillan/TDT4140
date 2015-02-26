package controllers;

import application.Main;
import controllers.MainViewController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DayDetailViewController implements Initializable {

    public AnchorPane fxmlPane;
    double startX;
    double startY;
    double endX;
    double endY;
    private MainViewController mainViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML public void mouseClicked(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();
        System.out.println("Clicked at " + startX + ", " + startY);
    }

    @FXML public void mouseReleased(MouseEvent event) {
        endX = event.getX();
        endY = event.getY();
        System.out.println("Clicked at " + endX + ", " + endY);
        //mainViewController.createRectangle(startX, startY, endX, endY, 12);
    }

    @FXML public void mouseIsDragging(MouseEvent event) {
    }

    public void setMain(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }
}
