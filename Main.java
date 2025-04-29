import java.io.*;
import java.util.*;

class Train {
    int trainID;
    String trainName;
    int availableSeats;
    int totalSeats;

    public Train(int trainID, String trainName, int totalSeats) {
        this.trainID = trainID;
        this.trainName = trainName;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    public Train(int trainID, String trainName, int totalSeats, int availableSeats) {
        this.trainID = trainID;
        this.trainName = trainName;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    public boolean bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean cancelSeat() {
        if (availableSeats < totalSeats) {
            availableSeats++;
            return true;
        }
        return false;
    }

    public void displayTrainDetails() {
        System.out.println("Train ID: " + trainID);
        System.out.println("Train Name: " + trainName);
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Available Seats: " + availableSeats);
        System.out.println("----------------------------------");
    }

    public String toFileString() {
        return trainID + "," + trainName + "," + totalSeats + "," + availableSeats;
    }

    public static Train fromFileString(String line) {
        String[] parts = line.split(",");
        int trainID = Integer.parseInt(parts[0]);
        String trainName = parts[1];
        int totalSeats = Integer.parseInt(parts[2]);
        int availableSeats = Integer.parseInt(parts[3]);
        return new Train(trainID, trainName, totalSeats, availableSeats);
    }
}

class User {
    String username;
    String password;
    String userType;
    List<String> ticketHistory;

    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.ticketHistory = new ArrayList<>();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addTicketHistory(String ticketDetail) {
        ticketHistory.add(ticketDetail);
    }

    public void displayTicketHistory() {
        if (ticketHistory.isEmpty()) {
            System.out.println("No ticket history available.");
        } else {
            System.out.println("Ticket History for " + username + ":");
            for (String ticket : ticketHistory) {
                System.out.println(ticket);
            }
        }
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(username).append(",").append(password).append(",").append(userType);
        for (String ticket : ticketHistory) {
            sb.append(",").append(ticket);
        }
        return sb.toString();
    }

    public static User fromFileString(String line) {
        String[] parts = line.split(",");
        String username = parts[0];
        String password = parts[1];
        String userType = parts[2];
        User user = new User(username, password, userType);
        for (int i = 3; i < parts.length; i++) {
            user.addTicketHistory(parts[i]);
        }
        return user;
    }
}

class RailwayManagementSystem {

    private List<Train> trains;
    private List<User> users;
    private List<String> ticketBookings;
    private BufferedReader reader;
    private final String TRAIN_FILE = "trainData.txt";
    private final String USER_FILE = "userData.txt";
    private final String TICKET_FILE = "ticketBookings.txt";

    public RailwayManagementSystem() {
        trains = new ArrayList<>();
        users = new ArrayList<>();
        ticketBookings = new ArrayList<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
        loadTrainData();
        loadUserData();
        loadTicketBookings();
    }

    public void addTrain() throws IOException {
        System.out.print("Enter Train ID: ");
        int trainID = Integer.parseInt(reader.readLine());
        System.out.print("Enter Train Name: ");
        String trainName = reader.readLine();
        System.out.print("Enter Total Seats: ");
        int totalSeats = Integer.parseInt(reader.readLine());

        Train train = new Train(trainID, trainName, totalSeats);
        trains.add(train);
        saveTrainData();
        System.out.println("Train added successfully!");
    }

    public void removeTrain() throws IOException {
        System.out.print("Enter Train ID to remove: ");
        int trainID = Integer.parseInt(reader.readLine());
        Train train = findTrainByID(trainID);
        if (train != null) {
            trains.remove(train);
            saveTrainData();
            System.out.println("Train removed successfully!");
        } else {
            System.out.println("Train not found.");
        }
    }

    public void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users available.");
        } else {
            System.out.println("Listing all users:");
            for (User user : users) {
                System.out.println("Username: " + user.username + ", Type: " + user.userType);
            }
        }
    }

    private void addUser() throws IOException {
        System.out.print("Enter Username: ");
        String username = reader.readLine();
        System.out.print("Enter Password: ");
        String password = reader.readLine();
        System.out.print("Enter User Type (admin/passenger): ");
        String userType = reader.readLine();

        if (!userType.equals("admin") && !userType.equals("passenger")) {
            System.out.println("Invalid user type. Please enter 'admin' or 'passenger'.");
            return;
        }

        User newUser = new User(username, password, userType);
        users.add(newUser);
        saveUserData();
        System.out.println("User added successfully!");
    }

    public void bookTicket(User user) throws IOException {
        System.out.print("Enter Train ID to book ticket: ");
        int trainID = Integer.parseInt(reader.readLine());
        Train train = findTrainByID(trainID);
        if (train != null) {
            if (train.bookSeat()) {
                String ticketDetail = "Booked " + train.trainName + " (Train ID: " + trainID + ")";
                user.addTicketHistory(ticketDetail);
                ticketBookings.add(ticketDetail);
                saveUserData();
                saveTicketBookings();
                saveTrainData();
                System.out.println("Ticket booked successfully!");
            } else {
                System.out.println("No available seats on this train.");
            }
        } else {
            System.out.println("Train not found.");
        }
    }

    public void cancelTicket(User user) throws IOException {
        System.out.print("Enter Train ID to cancel ticket: ");
        int trainID = Integer.parseInt(reader.readLine());
        Train train = findTrainByID(trainID);
        if (train != null) {
            if (train.cancelSeat()) {
                String ticketDetail = "Cancelled " + train.trainName + " (Train ID: " + trainID + ")";
                user.addTicketHistory(ticketDetail);
                ticketBookings.removeIf(ticket -> ticket.equals(ticketDetail));
                saveUserData();
                saveTicketBookings();
                saveTrainData();
                System.out.println("Ticket canceled successfully!");
            } else {
                System.out.println("No tickets to cancel or the train is fully booked.");
            }
        } else {
            System.out.println("Train not found.");
        }
    }

    public void displayTicketHistory(User user) {
        user.displayTicketHistory();
    }

    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    private Train findTrainByID(int trainID) {
        for (Train train : trains) {
            if (train.trainID == trainID) {
                return train;
            }
        }
        return null;
    }

    private void loadTrainData() {
        try (BufferedReader br = new BufferedReader(new FileReader(TRAIN_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Train train = Train.fromFileString(line);
                trains.add(train);
            }
        } catch (IOException e) {
            System.out.println("Error loading train data: " + e.getMessage());
        }
    }

    private void saveTrainData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRAIN_FILE))) {
            for (Train train : trains) {
                bw.write(train.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving train data: " + e.getMessage());
        }
    }

    private void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = User.fromFileString(line);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    private void saveUserData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                bw.write(user.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    private void loadTicketBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader(TICKET_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                ticketBookings.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading ticket bookings: " + e.getMessage());
        }
    }

    private void saveTicketBookings() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TICKET_FILE))) {
            for (String ticket : ticketBookings) {
                bw.write(ticket);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving ticket bookings: " + e.getMessage());
        }
    }

    public void start() throws IOException {
        int choice;
        System.out.println("\n==========================================");
        System.out.println("Welcome to the Railway Management System");
        System.out.println("==========================================\n");

        do {
            System.out.println("1. Admin Login");
            System.out.println("2. Passenger Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    passengerLogin();
                    break;
                case 3:
                    System.out.println("Visit Again .... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 3);
    }

    private void adminLogin() throws IOException {
        System.out.print("Enter Admin Username: ");
        String username = reader.readLine();
        System.out.print("Enter Admin Password: ");
        String password = reader.readLine();

        User admin = authenticateUser(username, password);
        if (admin != null && admin.userType.equals("admin")) {
            adminMenu();
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void adminMenu() throws IOException {
        int choice;
        do {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add Train");
            System.out.println("2. Remove Train");
            System.out.println("3. View All Users");
            System.out.println("4. View All Trains");
            System.out.println("5. Add User");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1:
                    addTrain();
                    break;
                case 2:
                    removeTrain();
                    break;
                case 3:
                    displayAllUsers();
                    break;
                case 4:
                    displayAllTrains();
                    break;
                case 5:
                    addUser();
                    break;
                case 6:
                    System.out.println("Admin Logout Successful.");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        } while (choice != 6);
    }

    private void passengerLogin() throws IOException {
        System.out.print("Enter Username: ");
        String username = reader.readLine();
        System.out.print("Enter Password: ");
        String password = reader.readLine();

        User passenger = authenticateUser(username, password);
        if (passenger != null && passenger.userType.equals("passenger")) {
            passengerMenu(passenger);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void passengerMenu(User passenger) throws IOException {
        int choice;
        do {
            System.out.println("\nPassenger Menu");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Ticket History");
            System.out.println("4. View Available Trains");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(reader.readLine());

            switch (choice) {
                case 1:
                    bookTicket(passenger);
                    break;
                case 2:
                    cancelTicket(passenger);
                    break;
                case 3:
                    displayTicketHistory(passenger);
                    break;
                case 4:
                    displayAllTrains();
                    break;
                case 5:
                    System.out.println("Passenger Logout Successful.");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        } while (choice != 5);
    }

    private void displayAllTrains() {
        if (trains.isEmpty()) {
            System.out.println("No trains available.");
        } else {
            System.out.println("Available Trains:");
            for (Train train : trains) {
                train.displayTrainDetails();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        RailwayManagementSystem rms = new RailwayManagementSystem();
        rms.start();
    }
}
