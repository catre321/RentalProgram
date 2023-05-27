package com.nguyenhoang;

import com.nguyenhoang.client.GuestAccount;
import com.nguyenhoang.client.RegularAccount;
import com.nguyenhoang.client.VIPAccount;
import com.nguyenhoang.setvalue.Enum;
import com.nguyenhoang.stock.Item;
import com.nguyenhoang.stock.MovieRecord;
import com.nguyenhoang.stock.DVD;
import com.nguyenhoang.stock.VideoGame;
import com.nguyenhoang.client.Customer;

import java.io.*;
import java.util.ArrayList;


public class RentalSystem {
    private ArrayList<Item> itemList = new ArrayList<>();
    private ArrayList<Customer> customersList = new ArrayList<>();


    public RentalSystem() {
        readItemFromFile();
        readCustomerFromFile();
    }

    public Item findItemById(String id) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId().equals(id)) {
                return itemList.get(i);
            }
        }
        return null;
    }

    public boolean readItemFromFile(){
        long startTime = System.currentTimeMillis();
        String line;
        // readItemFromFile item from file
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                String title = data[1];
                Enum.RentalType rentalType = Enum.RentalType.valueOf(data[2]);
                Enum.LoanType loanType = Enum.LoanType.valueOf(data[3]);
                int numCopies = Integer.parseInt(data[4]);
                double rentalFee = Double.parseDouble(data[5]);
                boolean rentalStatus = numCopies != 0;

                if (rentalType == Enum.RentalType.RECORD || rentalType == Enum.RentalType.DVD) {
                    Enum.Genre genre = Enum.Genre.valueOf(data[6].toUpperCase());
                    if (rentalType == Enum.RentalType.RECORD) {
                        itemList.add(new MovieRecord(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus, genre));
                    } else {
                        itemList.add(new DVD(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus, genre));
                    }
                } else {
                    itemList.add(new VideoGame(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus));
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            return false;
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("readItemFromFile() execution time: " + executionTime + " ms");
        return true;
    }

    public boolean readCustomerFromFile(){
        long startTime = System.currentTimeMillis();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            while ((line = br.readLine()) != null) {
                ArrayList<Item> customerItemList = new ArrayList<>();
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                String address = data[2];
                String phone = data[3];
                int count = Integer.parseInt(data[4]);
                Enum.AccountType accountType = Enum.AccountType.valueOf(data[5]);
                String username = data[6];
                String password = data[7];
                int rewardPoints = Integer.parseInt(data[8]);

                for (int i = 9; i < data.length; i++) {
                    Item item = findItemById(data[i]);
                    customerItemList.add(item);
                }

                if (accountType == Enum.AccountType.GUEST) {
                   customersList.add(new GuestAccount(id, name, address, phone, count, customerItemList, username, password, accountType, customerItemList.size()));

                } else if(accountType == Enum.AccountType.REGULAR){
                    customersList.add(new RegularAccount(id, name, address, phone, count, customerItemList, username, password, accountType));
                } else {
                    customersList.add(new VIPAccount(id, name, address, phone, count, customerItemList, username, password, accountType, rewardPoints));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("readCustomerFromFile() execution time: " + executionTime + " ms");
        return true;
    }

    public boolean writeItemToFile () {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("items.txt"))) {
            for (int i = 0; i < itemList.size(); i++) {
                Item item = itemList.get(i);
                String id = item.getId();
                String title = item.getTitle();
                Enum.RentalType rentalType = item.getRentalType();
                Enum.LoanType loanType = item.getLoanType();
                int numCopies = item.getNumCopies();
                double rentalFee = item.getRentalFee();
                String genre = "";
                if (rentalType == Enum.RentalType.RECORD) {
                    genre = (((MovieRecord)item).getGenre().toString());
                    String line = String.format("%s,%s,%s,%s,%d,%.2f,%s", id, title, rentalType, loanType.toString(), numCopies, rentalFee, genre);
                    bw.write(line);
                } else if (rentalType == Enum.RentalType.DVD) {
                    genre = (((DVD)item).getGenre().toString());
                    String line = String.format("%s,%s,%s,%s,%d,%.2f,%s", id, title, rentalType, loanType.toString(), numCopies, rentalFee, genre);
                    bw.write(line);
                } else{
                    String line = String.format("%s,%s,%s,%s,%d,%.2f", id, title, rentalType.toString(), loanType.toString(), numCopies, rentalFee);
                    bw.write(line);
                }

                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            return false;
        }
        return true;
    }

    public boolean writeCustomerToFile () {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (int i = 0; i < customersList.size(); i++) {
                Customer customer = customersList.get(i);
                ArrayList<Item> customerItemList = new ArrayList<>();

                String id = customer.getId();
                String name = customer.getName();
                String address = customer.getAddress();
                String phone = customer.getPhone();
                int count = customer.getTotalSuccessfulRental();
                customerItemList = customer.getItems();
                Enum.AccountType accountType = customer.getAccountType();
                String username = customer.getUsername();
                String password = customer.getPassword();
                int rewardPoints = 0;
                if(accountType == Enum.AccountType.VIP) {
                    rewardPoints = ((VIPAccount) customer).getRewardPoint();
                }

                StringBuilder line = new StringBuilder();
                if(customerItemList.size() == 0) {
                    line.append(String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s", id, name, address, phone, count, accountType.toString(), username, password, rewardPoints));
                } else {
                    line.append(String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s,", id, name, address, phone, count, accountType.toString(), username, password, rewardPoints));
                }

                for(int j = 0; j < customerItemList.size(); j++){
                    line.append(customerItemList.get(j).getId());
                    if(j == customerItemList.size() - 1) {
                        continue;
                    }
                    line.append(",");
                }

                bw.write(line.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            return false;
        }
        return true;
    }

    public boolean checkLogInAdmin(String username, String password){
        return username.equals("admin") && password.equals("root");
    }

    public boolean checkLogInCustomer(String username, String password){
        for(int i = 0; i < customersList.size(); i++){
            if(customersList.get(i).getUsername().equals(username)){
                if(customersList.get(i).getPassword().equals(password))
                    return true;
            }
        }
        return false;
    }

    public int getHighestItemId(){
        int highestItemId = 0;
        for(int i = 0; i < itemList.size(); i++){
            if(Integer.parseInt(itemList.get(i).getId().substring(1, 4)) > highestItemId){
                highestItemId = Integer.parseInt(itemList.get(i).getId().substring(1, 4));
        }}
        return highestItemId;
    }

    public int getHighestCustomerId(){
        int highestCustomerId = 0;
        for(int i = 0; i < customersList.size(); i++){
            if(Integer.parseInt(customersList.get(i).getId().substring(1, 4)) > highestCustomerId){
                highestCustomerId = Integer.parseInt(customersList.get(i).getId().substring(1, 4));
            }}
        return highestCustomerId;
    }

    public boolean checkItemExist(String title) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getTitle().equals(title))
                return true;
        }
        return false;
    }

    public boolean checkCustomerExist(String name) {
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getName().equals(name))
                return true;
        }
        return false;
    }
    public boolean checkUsernameExist(String username) {
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getUsername().equals(username))
                return true;
        }
        return false;
    }

    public Item addItem(String id, String title, Enum.RentalType rentalType, Enum.LoanType loanType,
                           int numCopies, boolean rentalStatus, double rentalFee, Enum.Genre genre) {
        if (checkItemExist(title))
            throw new IllegalArgumentException("Item already exist");
        else {
            Item item;
            if (rentalType == Enum.RentalType.RECORD) {
                item = new MovieRecord(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus, genre);
            } else if (rentalType == Enum.RentalType.DVD) {
                item = new DVD(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus, genre);
            } else {
                item = new VideoGame(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus);
            }
            itemList.add(item);
            return item;
        }
    }

    public Item updateItem(int idNumber, String id, String title, Enum.RentalType rentalType, Enum.LoanType loanType,
                           int numCopies, boolean rentalStatus, double rentalFee, Enum.Genre genre) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getIdNumber() == idNumber) {
                Item updatedItem;
                if (rentalType == Enum.RentalType.RECORD) {
                    updatedItem = new MovieRecord(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus, genre);
                } else if (rentalType == Enum.RentalType.DVD) {
                    updatedItem = new DVD(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus, genre);
                } else {
                    updatedItem = new VideoGame(id, title, rentalType, loanType, numCopies, rentalFee, rentalStatus);
                }
                itemList.set(i, updatedItem);
                return updatedItem;
            }
        }
        return null;
    }

    public Customer addCustomer(String id, String name, String address, String phone,
                               Enum.AccountType accountType, String username, String password) {
        if (checkCustomerExist(name)) {
            throw new IllegalArgumentException("Customer already exist");
        } else if(checkUsernameExist(username)){
            throw new IllegalArgumentException("Username already exist");
        }
        else {
            int count = 0;
            int countRentals = 0;
            ArrayList<Item> customerItemList = new ArrayList<>();
            Customer newCustomer = new GuestAccount(id, name, address, phone, count, customerItemList, username, password, accountType, countRentals );
            customersList.add(newCustomer);
            return newCustomer;
        }
    }

    public ArrayList<Item> getItemsList() {
        return itemList;
    }

    public ArrayList<Customer> getCustomersList(){
        return customersList;
    }

    public ArrayList<Customer> searchCustomerGroup(Enum.AccountType group){
        ArrayList<Customer> foundCustomer = new ArrayList<>();
        for(int i = 0; i < customersList.size(); i++){
            if(customersList.get(i).getAccountType().equals(group)){
                foundCustomer.add(customersList.get(i));
            }
        }
        return foundCustomer;
    }

    public Customer promoteCustomer(String username, Enum.AccountType upgradeAccountType) throws Exception {
        for(int i = 0; i < customersList.size(); i++){
            if(customersList.get(i).getUsername().equals(username)){
                Customer customer = customersList.get(i);
                if(customer.getAccountType().getLevel() == upgradeAccountType.getLevel()){
                    throw new Exception("Customer already in this account type");
                }
                if(customer.getAccountType().getLevel() > upgradeAccountType.getLevel()){
                    throw new Exception("You cannot downgrade a customer account");
                }
                if(customer.getTotalSuccessfulRental() < 5 && upgradeAccountType == Enum.AccountType.VIP){
                    throw new Exception("You cannot promote a customer with less than 5 successful rental");
                }
                if(customer.getTotalSuccessfulRental() < 3 && upgradeAccountType == Enum.AccountType.REGULAR){
                    throw new Exception("You cannot promote a customer with less than 3 successful rental");
                }
                if(upgradeAccountType == Enum.AccountType.REGULAR){
                    customersList.set(i, new RegularAccount(customer.getId(), customer.getName(), customer.getAddress(),
                            customer.getPhone(), customer.getTotalSuccessfulRental(), customer.getItems(), customer.getUsername(), customer.getPassword(), upgradeAccountType));
                }
                if(upgradeAccountType == Enum.AccountType.VIP){
                    customersList.set(i, new VIPAccount(customer.getId(), customer.getName(), customer.getAddress(),
                            customer.getPhone(), customer.getTotalSuccessfulRental(), customer.getItems(), customer.getUsername(), customer.getPassword(), upgradeAccountType, 0));
                }
                return customersList.get(i);
            }
        }
        return null;
    }

    public ArrayList<Item> getCustomerItemsList(String username){
        for(int i = 0; i < customersList.size(); i++){
            if(customersList.get(i).getUsername().equals(username)){
                ArrayList<Item>  items = customersList.get(i).getItems();
                if(items == null)  {
                    return items = new ArrayList<>();
                } else {
                    return items;
                }
            }
        }
        return null;
    }
    public boolean checkCustomerItemExist(Customer customer, String title){
        for(int i = 0; i < customer.getItems().size(); i++){
            if(customer.getItems().get(i).getTitle().equals(title))
                return true;
        }
        return false;
    }
    public boolean checkItemAvailable(Item item){
        for(int i = 0; i < itemList.size(); i++){
            if(itemList.get(i).getTitle().equals(item.getTitle()) && itemList.get(i).getNumCopies() > 0)
                return true;
        }
        return false;
    }
    public void reduceStock(Item item){
        for(int i = 0; i < itemList.size(); i++){
            if(itemList.get(i).getTitle().equals(item.getTitle()))
                itemList.get(i).reduceNumCopies();
        }
    }
    public void increaseStock(Item item){
        for(int i = 0; i < itemList.size(); i++){
            if(itemList.get(i).getTitle().equals(item.getTitle()))
                itemList.get(i).increaseNumCopies();
        }
    }
    public void addToCustomerRentedItems(String username, Item item) throws Exception {
        System.out.println(item+"rental");
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getUsername().equals(username)) {
                System.out.println(customersList.get(i));
                if(checkCustomerItemExist(customersList.get(i), item.getTitle())) {
                    throw new Exception("Item already exist in customer's list");
                }
                else if(!checkItemAvailable(item)) {
                    throw new Exception("Item is not available - out of stock");
                }
                if(customersList.get(i).getAccountType().equals(Enum.AccountType.GUEST)){
                    GuestAccount guestAccount = (GuestAccount) customersList.get(i);
                    if(guestAccount.getCountInRental() > 2 ){
                        throw new Exception("Customer has reached maximum number of rentals");
                    } else {
                        guestAccount.increaseCountInRental();
                        guestAccount.rentAnItem(item);
                        customersList.set(i, guestAccount);
                        reduceStock(item);
                    }
                } else if(customersList.get(i).getAccountType().equals(Enum.AccountType.REGULAR)){
                    RegularAccount regularAccount = (RegularAccount) customersList.get(i);
                    regularAccount.rentAnItem(item);
                    customersList.set(i, regularAccount);
                    reduceStock(item);
                } else if(customersList.get(i).getAccountType().equals(Enum.AccountType.VIP)){
                    VIPAccount vipAccount = (VIPAccount) customersList.get(i);
                    vipAccount.rentAnItem(item);
                    vipAccount.addRewardPoint();
                    customersList.set(i, vipAccount);
                    reduceStock(item);
                }
            }
        }
    }

    public Customer removeFromCustomerRentedItems(String username, Item item) {
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getUsername().equals(username)) {
                if(customersList.get(i).getAccountType().equals(Enum.AccountType.GUEST)){
                    GuestAccount guestAccount = (GuestAccount) customersList.get(i);
                        guestAccount.reduceCountInRental();
                        guestAccount.returnAnItem(item);
                        guestAccount.increaseTotalSuccessfulRental();
                        customersList.set(i, guestAccount);
                        increaseStock(item);
                        return guestAccount;
                } else if(customersList.get(i).getAccountType().equals(Enum.AccountType.REGULAR)){
                    RegularAccount regularAccount = (RegularAccount) customersList.get(i);
                    regularAccount.returnAnItem(item);
                    regularAccount.increaseTotalSuccessfulRental();
                    customersList.set(i, regularAccount);
                    increaseStock(item);
                    return regularAccount;
                } else if(customersList.get(i).getAccountType().equals(Enum.AccountType.VIP)){
                    VIPAccount vipAccount = (VIPAccount) customersList.get(i);
                    vipAccount.returnAnItem(item);
                    vipAccount.increaseTotalSuccessfulRental();
                    customersList.set(i, vipAccount);
                    increaseStock(item);
                    return vipAccount;
                }
            }
        }
        return null;
    }

    public void removeAnItem(Item item) {
        itemList.remove(item);
    }

    public Customer updateCustomer(String customerId, String name, String address, String phone, String username, String password) {
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getId().equals(customerId)) {
                customersList.get(i).setName(name);
                customersList.get(i).setAddress(address);
                customersList.get(i).setPhone(phone);
                customersList.get(i).setUsername(username);
                customersList.get(i).setPassword(password);
                return customersList.get(i);
            }
        }
        return null;
    }

    public Customer getReward(String customerId) throws Exception {
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getId().equals(customerId)) {
                if(customersList.get(i).getAccountType().equals(Enum.AccountType.VIP)){
                    VIPAccount vipAccount = (VIPAccount) customersList.get(i);
                    if(!vipAccount.getFreeRental()){
                        throw new Exception("Not enough point!");
                    }
                    customersList.set(i, vipAccount);
                    return vipAccount;
                }
                throw new Exception("Customer is not VIP");
            }
        }
        return null;
    }
}

