package top.cs.boot.config.enums;

public enum ExpressStatus {
    CREATED("已揽收"),
    TRANSIT("在途中"),
    SUCCESS("签收");

    private final String label;

    ExpressStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
