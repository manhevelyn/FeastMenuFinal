package model;

public class FeastMenu {

    private String menuCode, name, ingredients;
    private double price;

    public FeastMenu(String menuCode, String name, String ingredients, double price) {
        this.menuCode = menuCode;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
    }

    public FeastMenu(String string, String string0, double price, String ingredient) {
        this.menuCode = menuCode;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        String xuLiIngredients = getIngredients().replace("#", "\n");
        return String.format("Code        :%s\n"
                + "Name        :%s\n"
                + "Price       :%,.0f%n Vnd\n" // Tách ra // 375,000
                + "Ingredient  :%s" // Thay # ==> \n
                ,
                 menuCode, name, price, xuLiIngredients);
    }

}
