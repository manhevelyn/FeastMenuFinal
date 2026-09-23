package Repository;

import Entity.Customer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CustomerRepository {
    private static final String FILE_NAME = "customers.dat";

    public void save(ArrayList<Customer> customers) throws IOException {
        FileOutputStream fileOutput = null;
        ObjectOutputStream objectOutput = null;
        try {
            fileOutput = new FileOutputStream(FILE_NAME);
            objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeInt(customers.size());
            for (Customer customer : customers) {
                objectOutput.writeObject(customer);
            }
        } finally {
            if (objectOutput != null) {
                objectOutput.close();
            } else if (fileOutput != null) {
                fileOutput.close();
            }
        }
    }

    public ArrayList<Customer> load() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<Customer>();
        }

        FileInputStream fileInput = null;
        ObjectInputStream objectInput = null;
        try {
            fileInput = new FileInputStream(file);
            objectInput = new ObjectInputStream(fileInput);
            ArrayList<Customer> customers = new ArrayList<Customer>();
            int numberOfCustomers = objectInput.readInt();
            for (int index = 0; index < numberOfCustomers; index++) {
                customers.add((Customer) objectInput.readObject());
            }
            return customers;
        } finally {
            if (objectInput != null) {
                objectInput.close();
            } else if (fileInput != null) {
                fileInput.close();
            }
        }
    }
}