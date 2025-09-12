package top.cs.boot.config.enums;

public enum DrinkType {
    COFFEE("咖啡",8.0),
    TEA("茶",5.0),
    JUICE("果汁",3.0);

    private final String type;
    private final double price;

    DrinkType(String type ,double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}
