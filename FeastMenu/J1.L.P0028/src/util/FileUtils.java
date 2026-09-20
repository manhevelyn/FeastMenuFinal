package util;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import model.FeastMenu;

public class FileUtils {
// Hàm đọc file 
    public static ArrayList<FeastMenu> readMenus(String fileName) {
        ArrayList<FeastMenu> MenuBox = new ArrayList<>();

        // Kiểm tra xem file có tồn tại ko
        try {
            // Quét file
            File file = new File(fileName);
            Scanner sc = new Scanner(file); // Quét file
            // Nếu file có tồn tại thì bỏ qua cái header chỉ lấy data
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            // Đọc data
            while (sc.hasNextLine()) { // Ktra có tồn tại dữ liệu
                String line = sc.nextLine(); // Đọc giá trị gắn vô biến line + 1 đơn vị
                if (line.isEmpty()) {
                    continue; 
                }
                
                // Tách chuỗi lớn thành nhiều chuỗi con gán vào data
                String[]data = line.split(",",4);
                double price = Double.parseDouble(data[2]);
                String ingredient = data[3].trim().replace("\"","");
                // Chuyển thành object
                FeastMenu menu = new FeastMenu(data[0], data[1], price, ingredient);
                // Data [0]: PW001
                MenuBox.add(menu);
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return MenuBox;
    }

}
