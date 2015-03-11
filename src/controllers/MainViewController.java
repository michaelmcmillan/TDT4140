package controllers;

import helpers.CalendarHelper;
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
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private final String MAINVIEW_PATH = "../views/MainView.fxml";
    private CalendarViewController calendarViewController;
    private SidebarViewController sidebarViewController;
    private GroupPopupViewController groupPopupViewController;
    private EditGroupPopupViewController editGroupPopupViewController;
    private MenuBar menuBar;
    private Person person;
    private int weekNumber;
    private LocalDate firstDayOfWeek = CalendarHelper.getFirstDateOfWeek();
    private Label weekNumberLabel;
    private Stage primaryStage;

    public MainViewController(Stage primaryStage, Person person) throws Exception {

        this.person = person;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        sidebarViewController = new SidebarViewController(this, primaryStage);
        LocalDate firstDayOfWeek = CalendarHelper.getFirstDateOfWeek();
        calendarViewController = new CalendarViewController(this, primaryStage);
        Pane calendarPane = (AnchorPane) scene.lookup("#calendarPane");
        groupPopupViewController = new GroupPopupViewController(calendarPane);
        editGroupPopupViewController = new EditGroupPopupViewController(calendarPane);
        Button nextWeek = (Button) scene.lookup("#nextWeek");
        Button pastWeek = (Button) scene.lookup("#pastWeek");
        weekNumberLabel = (Label) scene.lookup("#ukenr");
        weekNumber = CalendarHelper.getCurrentWeek();

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
        weekNumberLabel.setText("UKE "+ getSelectedWeekNumber());

        // Item menu
        Menu itemMenu = menus.get(1);
        ObservableList<MenuItem> itemMenuItems = itemMenu.getItems();

        MenuItem editGroupMenuItem = itemMenuItems.get(0);
        editGroupMenuItem.setOnAction(e -> fireEditGroup(e));

        MenuItem addGroupMenuItem = itemMenuItems.get(1);
        addGroupMenuItem.setOnAction(e -> fireAddGroup(e));

        //HTTPConnection connection = new HTTPConnection("https://www.github.com");
    }

    void fireAddGroup(ActionEvent event) {
        groupPopupViewController.show();
    }

    void fireEditGroup(ActionEvent event) {
        editGroupPopupViewController.show();
    }

    void fireLogout(ActionEvent event) {System.out.println("Log out");}

    void fireNextWeek(ActionEvent event) {
        if(weekNumber < 52) {
            setSelectedWeekNumber(++weekNumber);
        }
        weekNumberLabel.setText("UKE " + weekNumber);
        firstDayOfWeek = firstDayOfWeek.plusWeeks(1);
        calendarViewController.generateDayPanes(firstDayOfWeek);
    }

    void fireLastWeek(ActionEvent event) {
        if(weekNumber > 1) {
            setSelectedWeekNumber(--weekNumber);
        }
        weekNumberLabel.setText("UKE " + weekNumber);
        firstDayOfWeek = firstDayOfWeek.minusWeeks(1);
        calendarViewController.generateDayPanes(firstDayOfWeek);
    }

    public int getSelectedWeekNumber() {
        return weekNumber;
    }

    public void setSelectedWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    protected Person getPerson() {
        return this.person;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("yolo");
    }

}
