package application;


public class Config {

    private static Config instance = null;

    public boolean DEBUG = false;

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }
}
