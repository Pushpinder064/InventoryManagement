import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class version2 {
    private Queue<ItemWithQuantity> warehouse;
    private Scanner scanner;
    private String databaseFileName = "warehouse_db.txt";

    public version2() {
        warehouse = new LinkedList<>();
        scanner = new Scanner(System.in);
        loadWarehouseData();
    }
    public void addItem(String item, int quantity) {
        warehouse.offer(new ItemWithQuantity(item, quantity));
        System.out.println(quantity + " " + item + "(s) added to the warehouse.");

        updateDatabaseFile();
    }
    // Remove and ship the oldest item from the warehouse
    public void shipItem() {
        if (!warehouse.isEmpty()) {
            ItemWithQuantity shippedItem = warehouse.poll();
            System.out.println(shippedItem.getQuantity() + " " + shippedItem.getItem() + "(s) shipped from the warehouse.");

            // Update the database file
            updateDatabaseFile();
        } else {
            System.out.println("Warehouse is empty. Nothing to ship.");
        }
    }
    public boolean checkItem(String item) {
        for (ItemWithQuantity itemWithQuantity : warehouse) {
            if (itemWithQuantity.getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }
    public int checkItemQuantity(String item) {
        int totalQuantity = 0;
        for (ItemWithQuantity itemWithQuantity : warehouse) {
            if (itemWithQuantity.getItem().equals(item)) {
                totalQuantity += itemWithQuantity.getQuantity();
            }
        }
        return totalQuantity;
    }
    public void receiveItem(String item, int additionalQuantity) {
        boolean itemFound = false;
        for (ItemWithQuantity itemWithQuantity : warehouse) {
            if (itemWithQuantity.getItem().equals(item)) {
                itemWithQuantity.receiveQuantity(additionalQuantity);
                itemFound = true;
                break; // No need to continue searching if the item is found and received
            }
        }
        if (itemFound) {
            System.out.println("Received " + additionalQuantity + " additional " + item + "(s) in the warehouse.");

            // Update the database file
            updateDatabaseFile();
        } else {
            System.out.println("Item '" + item + "' not found in the warehouse. Cannot receive quantity.");
        }
    }
    // Load warehouse data from the database file
    private void loadWarehouseData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(databaseFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String item = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    warehouse.offer(new ItemWithQuantity(item, quantity));
                }
            }
        } catch (IOException e) {
            // Handle any errors when reading the file (e.g., file not found)
            System.out.println("Failed to load warehouse data from the database file.");
        }
    }
    // Update the database file with the current warehouse data
    private void updateDatabaseFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(databaseFileName))) {
            for (ItemWithQuantity itemWithQuantity : warehouse) {
                writer.println(itemWithQuantity.getItem() + "," + itemWithQuantity.getQuantity());
            }
        } catch (IOException e) {
            // Handle any errors when writing to the file
            System.out.println("Failed to update the database file.");
        }
    }
    // Display the main menu
    public void displayMenu() {
        System.out.println("\nWarehouse Management System Menu:");
        System.out.println("1. Add Item to Warehouse");
        System.out.println("2. Ship Item from Warehouse");
        System.out.println("3. Check Item in Warehouse");
        System.out.println("4. Check Item Quantity in Warehouse");
        System.out.println("5. Receive Item in Warehouse");
        System.out.println("6. Quit");
        System.out.print("Enter your choice: ");
    }
    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            switch (choice) {
                case 1:
                    System.out.print("Enter item to add to the warehouse: ");
                    String itemToAdd = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantityToAdd = scanner.nextInt();
                    addItem(itemToAdd, quantityToAdd);
                    break;
                case 2:
                    shipItem();
                    break;
                case 3:
                    System.out.print("Enter item to check in the warehouse: ");
                    String itemToCheck = scanner.nextLine();
                    boolean exists = checkItem(itemToCheck);
                    System.out.println("Item '" + itemToCheck + "' exists in the warehouse: " + exists);
                    break;
                case 4:
                    System.out.print("Enter item to check the quantity in the warehouse: ");
                    String itemToCheckQuantity = scanner.nextLine();
                    int quantity = checkItemQuantity(itemToCheckQuantity);
                    System.out.println("Quantity of '" + itemToCheckQuantity + "' in the warehouse: " + quantity);
                    break;
                case 5:
                    System.out.print("Enter item to receive in the warehouse: ");
                    String itemToReceive = scanner.nextLine();
                    System.out.print("Enter additional quantity to receive: ");
                    int additionalQuantity = scanner.nextInt();
                    receiveItem(itemToReceive, additionalQuantity);
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting the Warehouse Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
        scanner.close();
    }
    public static void main(String[] args) {
        version2 wms = new version2();
        wms.run();
    }
}
class ItemWithQuantity {
    private String item;
    private int quantity;
    public ItemWithQuantity(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
    public String getItem() {
        return item;
    }
    public int getQuantity() {
        return quantity;
    }
    public void receiveQuantity(int additionalQuantity) {
        quantity += additionalQuantity;
    }
}