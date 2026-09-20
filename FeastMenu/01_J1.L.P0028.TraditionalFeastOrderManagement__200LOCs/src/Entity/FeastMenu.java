package Entity;

import java.math.BigDecimal;

public class FeastMenu {
    private final String code;
    private final String name;
    private final BigDecimal price;
    private final String ingredients;

    public FeastMenu(String code, String name, BigDecimal price, String ingredients) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getIngredients() {
        return ingredients;
    }
}
