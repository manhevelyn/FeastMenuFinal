package Repository;

import Entity.FeastMenu;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuRepository {

    private final File menuFile;

    public MenuRepository(String fileName) {
        menuFile = new File(fileName);
    }

    public List<FeastMenu> load() throws IOException {
        List<FeastMenu> menus = new ArrayList<>();
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(menuFile, "UTF-8");
            boolean firstLine = true;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (firstLine) {
                    line = line.replace("\uFEFF", "");
                    firstLine = false;
                    if (line.toLowerCase().startsWith("code,")) {
                        continue;
                    }
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                List<String> fields = parseCsvLine(line);
                if (fields.size() < 4) {
                    continue;
                }
                String priceText = fields.get(2).replaceAll("[^0-9.]", "");
                menus.add(
                        new FeastMenu(
                                fields.get(0).trim().toUpperCase(),
                                fields.get(1).trim(),
                                new BigDecimal(priceText),
                                fields.get(3).trim()));
            }
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
        return menus;
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (quoted && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    field.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        fields.add(field.toString());
        return fields;
    }
    
    
}