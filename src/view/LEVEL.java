package View;

public enum  LEVEL {
    LEVEL1("Model/Images/chooselv1.png"),
    LEVEL2("Model/Images/chooselv2.png");

    private String urlLevel;

    LEVEL(String urlLevel) {
        this.urlLevel = urlLevel;
    }

    public String getUrl() {
        return this.urlLevel;
    }
}
