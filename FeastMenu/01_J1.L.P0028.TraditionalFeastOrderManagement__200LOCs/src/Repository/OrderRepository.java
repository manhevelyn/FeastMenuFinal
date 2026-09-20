package Repository;

import Entity.FeastOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class OrderRepository {
    private static final String FILE_NAME = "feast_order_service.dat";

    public void save(ArrayList<FeastOrder> orders) throws IOException {
        FileOutputStream fileOutput = null;
        ObjectOutputStream objectOutput = null;
        try {
            fileOutput = new FileOutputStream(FILE_NAME);
            objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeInt(orders.size());
            for (FeastOrder order : orders) {
                objectOutput.writeObject(order);
            }
        } finally {
            if (objectOutput != null) {
                objectOutput.close();
            } else if (fileOutput != null) {
                fileOutput.close();
            }
        }
    }

    public ArrayList<FeastOrder> load() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<FeastOrder>();
        }

        FileInputStream fileInput = null;
        ObjectInputStream objectInput = null;
        try {
            fileInput = new FileInputStream(file);
            objectInput = new ObjectInputStream(fileInput);
            ArrayList<FeastOrder> orders = new ArrayList<FeastOrder>();
            int numberOfOrders = objectInput.readInt();
            for (int index = 0; index < numberOfOrders; index++) {
                orders.add((FeastOrder) objectInput.readObject());
            }
            return orders;
        } finally {
            if (objectInput != null) {
                objectInput.close();
            } else if (fileInput != null) {
                fileInput.close();
            }
        }
    }
}