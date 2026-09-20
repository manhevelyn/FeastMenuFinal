package Repository;

import Entity.FeastMenu;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuService {
    private final List<FeastMenu> menus;

    public MenuService(String fileName) {
        List<FeastMenu> loadedMenus;
        try {
            loadedMenus = new MenuRepository(fileName).load();
        } catch (IOException exception) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            loadedMenus = new ArrayList<FeastMenu>();
        } catch (RuntimeException exception) {
            System.out.println("Invalid data in feastMenu.csv. Please check it.");
            loadedMenus = new ArrayList<FeastMenu>();
        }
        menus = loadedMenus;
    }

    public FeastMenu findByCode(String code) {
        if (code == null) return null;
        for (FeastMenu menu : menus) {
            if (menu.getCode().equalsIgnoreCase(code.trim())) return menu;
        }
        return null;
    }

    public List<FeastMenu> getSortedMenus() {
        ArrayList<FeastMenu> results = new ArrayList<FeastMenu>(menus);
        for (int first = 0; first < results.size() - 1; first++) {
            for (int second = first + 1; second < results.size(); second++) {
                if (results.get(first).getPrice().compareTo(results.get(second).getPrice()) > 0) {
                    FeastMenu temporary = results.get(first);
                    results.set(first, results.get(second));
                    results.set(second, temporary);
                }
            }
        }
        return results;
    }

    public boolean isEmpty() {
        return menus.isEmpty();
    }
}