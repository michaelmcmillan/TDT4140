package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Person;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private final String MAINVIEW_PATH = "../views/MainView.fxml";
    private CalendarViewController calendarViewController;
    private SidebarViewController sidebarViewController;
    private GroupPopupViewController groupPopupViewController;
    private CalendarPopupViewController calendarPopupViewController;
    private MenuBar menuBar;
    private Person person;
    private int weekNumber;
    private Label ukenr;

    public MainViewController(Stage primaryStage, Person person) throws Exception {

        this.person = person;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        sidebarViewController = new SidebarViewController(this, primaryStage);
        calendarViewController = new CalendarViewController(this, primaryStage);
        Pane calendarPane = (AnchorPane) scene.lookup("#calendarPane");
        groupPopupViewController = new GroupPopupViewController(calendarPane);
        calendarPopupViewController = new CalendarPopupViewController(calendarPane);
        Button nextWeek = (Button) scene.lookup("#nextWeek");
        Button pastWeek = (Button) scene.lookup("#pastWeek");
        ukenr = (Label) scene.lookup("#ukenr");

        LocalDate date = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        weekNumber = date.get(woy);

        // Listeners
        menuBar = (MenuBar) main.getChildrenUnmodifiable().get(1); // TODO: Find a better way to do this
        ObservableList<Menu> menus = menuBar.getMenus();

        // Account menu
        Menu accountMenu = menus.get(0);
        ObservableList<MenuItem> accountMenuItems = accountMenu.getItems();

        MenuItem logoutMenuItem = accountMenuItems.get(0);
        logoutMenuItem.setOnAction(e -> fireLogout(e));

        //Change week
        nextWeek.setOnAction(e -> fireNextWeek(e));
        pastWeek.setOnAction(e -> fireLastWeek(e));
        ukenr.setText("UKE "+weekNumber);


        // Item menu
        Menu itemMenu = menus.get(1);
        ObservableList<MenuItem> itemMenuItems = itemMenu.getItems();

        MenuItem addCalendarMenuItem = itemMenuItems.get(0);
        addCalendarMenuItem.setOnAction(e -> fireAddCalendar(e));

        MenuItem addGroupMenuItem = itemMenuItems.get(1);
        addGroupMenuItem.setOnAction(e -> fireAddGroup(e));
    }

    void fireAddGroup(ActionEvent event) {
        groupPopupViewController.show();
    }

    void fireAddCalendar(ActionEvent event) {
        calendarPopupViewController.show();
    }

    void fireLogout(ActionEvent event) {System.out.println("Log out");}

    void fireNextWeek(ActionEvent event) {if(weekNumber<52){weekNumber++;} ukenr.setText("UKE "+weekNumber);}

    void fireLastWeek(ActionEvent event) {if(weekNumber>1){weekNumber--;} ukenr.setText("UKE "+weekNumber);}

    public int getWeekNumber(){return weekNumber;}

    public void setWeekNumber(int weekNumber){this.weekNumber = weekNumber;}

    protected Person getPerson() {
        return this.person;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("yolo");
    }
}
