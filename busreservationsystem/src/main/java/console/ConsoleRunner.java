package com.aqm.console;

import com.aqm.entity.*;
import com.aqm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class ConsoleRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private BusService busService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FeedbackService feedbackService;

    private Scanner scanner = new Scanner(System.in);
    private User currentUser;
    private Admin currentAdmin;

    public void run() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   BUS RESERVATION MANAGEMENT SYSTEM    ║");
        System.out.println("╚════════════════════════════════════════╝");

        while (true) {
            showMainMenu();
        }
    }

    private void showMainMenu() {
        System.out.println("\n══════════ MAIN MENU ══════════");
        System.out.println("1. User Login");
        System.out.println("2. User Registration");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");
        System.out.print("Choose option: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> userLogin();
            case 2 -> userRegistration();
            case 3 -> adminLogin();
            case 4 -> {
                System.out.println("Thank you for using Bus Reservation System!");
                System.exit(0);
            }
            default -> System.out.println("Invalid option!");
        }
    }

    // ========== USER OPERATIONS ==========

    private void userLogin() {
        System.out.println("\n──── USER LOGIN ────");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> user = userService.login(email, password);
        if (user.isPresent()) {
            currentUser = user.get();
            System.out.println("✓ Login successful! Welcome " + currentUser.getName());
            userMenu();
        } else {
            System.out.println("✗ Invalid credentials!");
        }
    }

    private void userRegistration() {
        System.out.println("\n──── USER REGISTRATION ────");
        User user = new User();

        System.out.print("Name: ");
        user.setName(scanner.nextLine());

        System.out.print("Email: ");
        user.setEmail(scanner.nextLine());

        System.out.print("Password: ");
        user.setPassword(scanner.nextLine());

        System.out.print("Phone Number: ");
        user.setPhoneNumber(scanner.nextLine());

        System.out.print("Address: ");
        user.setAddress(scanner.nextLine());

        userService.registerUser(user);
        System.out.println("✓ Registration successful!");
    }

    private void userMenu() {
        while (currentUser != null) {
            System.out.println("\n══════════ USER MENU ══════════");
            System.out.println("1. View All Buses");
            System.out.println("2. Search Routes");
            System.out.println("3. Make Reservation");
            System.out.println("4. View My Reservations");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Give Feedback");
            System.out.println("7. Logout");
            System.out.print("Choose option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> viewAllBuses();
                case 2 -> searchRoutes();
                case 3 -> makeReservation();
                case 4 -> viewMyReservations();
                case 5 -> cancelReservation();
                case 6 -> giveFeedback();
                case 7 -> {
                    currentUser = null;
                    System.out.println("✓ Logged out successfully!");
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void viewAllBuses() {
        List<Bus> buses = busService.getAllBuses();
        System.out.println("\n──── ALL BUSES ────");
        if (buses.isEmpty()) {
            System.out.println("No buses available.");
        } else {
            buses.forEach(bus -> {
                System.out.println("\n┌─────────────────────────────────");
                System.out.println("│ Bus ID: " + bus.getBusId());
                System.out.println("│ Bus Number: " + bus.getBusNumber());
                System.out.println("│ Type: " + bus.getBusType());
                System.out.println("│ Total Seats: " + bus.getTotalSeats());
                System.out.println("│ Available Seats: " + bus.getAvailableSeats());
                System.out.println("│ Fare/Seat: ₹" + bus.getFarePerSeat());
                if (bus.getRoute() != null) {
                    System.out.println("│ Route: " + bus.getRoute().getSource() + " → " + bus.getRoute().getDestination());
                }
                System.out.println("└─────────────────────────────────");
            });
        }
    }

    private void searchRoutes() {
        System.out.println("\n──── SEARCH ROUTES ────");
        System.out.print("Source: ");
        String source = scanner.nextLine();
        System.out.print("Destination: ");
        String destination = scanner.nextLine();

        List<Route> routes = routeService.searchRoutes(source, destination);
        if (routes.isEmpty()) {
            System.out.println("No routes found.");
        } else {
            routes.forEach(route -> {
                System.out.println("\n┌─────────────────────────────────");
                System.out.println("│ Route ID: " + route.getRouteId());
                System.out.println("│ " + route.getSource() + " → " + route.getDestination());
                System.out.println("│ Distance: " + route.getDistance() + " km");
                System.out.println("│ Duration: " + route.getEstimatedDuration() + " mins");
                System.out.println("└─────────────────────────────────");
            });
        }
    }

    private void makeReservation() {
        System.out.println("\n──── MAKE RESERVATION ────");
        viewAllBuses();

        System.out.print("\nEnter Bus ID: ");
        Long busId = getLongInput();

        Optional<Bus> optBus = busService.getBusById(busId);
        if (optBus.isEmpty()) {
            System.out.println("✗ Bus not found!");
            return;
        }

        Bus bus = optBus.get();

        System.out.print("Number of seats: ");
        int seats = getIntInput();

        if (seats > bus.getAvailableSeats()) {
            System.out.println("✗ Only " + bus.getAvailableSeats() + " seats available!");
            return;
        }

        System.out.print("Travel date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate travelDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            Reservation reservation = reservationService.createReservation(currentUser, bus, travelDate, seats);
            System.out.println("\n✓ Reservation successful!");
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Total Fare: ₹" + reservation.getTotalFare());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewMyReservations() {
        List<Reservation> reservations = reservationService.getUserReservations(currentUser);
        System.out.println("\n──── MY RESERVATIONS ────");

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(res -> {
                System.out.println("\n┌─────────────────────────────────");
                System.out.println("│ Reservation ID: " + res.getReservationId());
                System.out.println("│ Bus: " + res.getBus().getBusNumber());
                System.out.println("│ Travel Date: " + res.getTravelDate());
                System.out.println("│ Seats: " + res.getNumberOfSeats());
                System.out.println("│ Total Fare: ₹" + res.getTotalFare());
                System.out.println("│ Status: " + res.getStatus());
                System.out.println("└─────────────────────────────────");
            });
        }
    }

    private void cancelReservation() {
        viewMyReservations();
        System.out.print("\nEnter Reservation ID to cancel: ");
        Long resId = getLongInput();

        reservationService.cancelReservation(resId);
        System.out.println("✓ Reservation cancelled successfully!");
    }

    private void giveFeedback() {
        System.out.println("\n──── GIVE FEEDBACK ────");
        viewAllBuses();

        System.out.print("Enter Bus ID: ");
        Long busId = getLongInput();

        Optional<Bus> optBus = busService.getBusById(busId);
        if (optBus.isEmpty()) {
            System.out.println("✗ Bus not found!");
            return;
        }

        System.out.print("Rating (1-5): ");
        int rating = getIntInput();

        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        Feedback feedback = new Feedback();
        feedback.setUser(currentUser);
        feedback.setBus(optBus.get());
        feedback.setRating(rating);
        feedback.setComment(comment);

        feedbackService.addFeedback(feedback);
        System.out.println("✓ Feedback submitted successfully!");
    }

    // ========== ADMIN OPERATIONS ==========

    private void adminLogin() {
        System.out.println("\n──── ADMIN LOGIN ────");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<Admin> admin = adminService.login(email, password);
        if (admin.isPresent()) {
            currentAdmin = admin.get();
            System.out.println("✓ Admin login successful! Welcome " + currentAdmin.getFirstName() + " " + currentAdmin.getLastName());

            adminMenu();
        } else {
            System.out.println("✗ Invalid credentials!");
        }
    }

    private void adminMenu() {
        while (currentAdmin != null) {
            System.out.println("\n══════════ ADMIN MENU ══════════");
            System.out.println("1. Add Route");
            System.out.println("2. View All Routes");
            System.out.println("3. Add Bus");
            System.out.println("4. View All Buses");
            System.out.println("5. Update Bus");
            System.out.println("6. Delete Bus");
            System.out.println("7. View All Reservations");
            System.out.println("8. View All Feedbacks");
            System.out.println("9. View All Users");
            System.out.println("10. Logout");
            System.out.print("Choose option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> addRoute();
                case 2 -> viewAllRoutes();
                case 3 -> addBus();
                case 4 -> viewAllBuses();
                case 5 -> updateBus();
                case 6 -> deleteBus();
                case 7 -> viewAllReservations();
                case 8 -> viewAllFeedbacks();
                case 9 -> viewAllUsers();
                case 10 -> {
                    currentAdmin = null;
                    System.out.println("✓ Logged out successfully!");
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addRoute() {
        System.out.println("\n──── ADD ROUTE ────");
        Route route = new Route();

        System.out.print("Source: ");
        route.setSource(scanner.nextLine());

        System.out.print("Destination: ");
        route.setDestination(scanner.nextLine());

        System.out.print("Distance (km): ");
        route.setDistance(getDoubleInput());

        System.out.print("Estimated Duration (mins): ");
        route.setEstimatedDuration(getIntInput());

        routeService.addRoute(route);
        System.out.println("✓ Route added successfully!");
    }

    private void viewAllRoutes() {
        List<Route> routes = routeService.getAllRoutes();
        System.out.println("\n──── ALL ROUTES ────");

        if (routes.isEmpty()) {
            System.out.println("No routes available.");
        } else {
            routes.forEach(route -> {
                System.out.println("\n┌─────────────────────────────────");
                System.out.println("│ Route ID: " + route.getRouteId());
                System.out.println("│ " + route.getSource() + " → " + route.getDestination());
                System.out.println("│ Distance: " + route.getDistance() + " km");
                System.out.println("│ Duration: " + route.getEstimatedDuration() + " mins");
                System.out.println("└─────────────────────────────────");
            });
        }
    }

    private void addBus() {
        System.out.println("\n──── ADD BUS ────");
        viewAllRoutes();

        Bus bus = new Bus();

        System.out.print("\nBus Number: ");
        bus.setBusNumber(scanner.nextLine());

        System.out.print("Bus Type (AC/Non-AC/Sleeper): ");
        bus.setBusType(scanner.nextLine());

        System.out.print("Total Seats: ");
        int totalSeats = getIntInput();
        bus.setTotalSeats(totalSeats);
        bus.setAvailableSeats(totalSeats);

        System.out.print("Fare per Seat: ");
        bus.setFarePerSeat(getDoubleInput());

        System.out.print("Route ID: ");
        Long routeId = getLongInput();

        Optional<Route> route = routeService.getRouteById(routeId);
        if (route.isPresent()) {
            bus.setRoute(route.get());
            busService.addBus(bus);
            System.out.println("✓ Bus added successfully!");
        } else {
            System.out.println("✗ Route not found!");
        }
    }

    private void updateBus() {
        System.out.println("\n──── UPDATE BUS ────");
        viewAllBuses();

        System.out.print("\nEnter Bus ID to update: ");
        Long busId = getLongInput();

        Optional<Bus> optBus = busService.getBusById(busId);
        if (optBus.isEmpty()) {
            System.out.println("✗ Bus not found!");
            return;
        }

        Bus bus = optBus.get();

        System.out.print("New Fare per Seat (current: " + bus.getFarePerSeat() + "): ");
        bus.setFarePerSeat(getDoubleInput());

        busService.updateBus(bus);
        System.out.println("✓ Bus updated successfully!");
    }

    private void deleteBus() {
        System.out.println("\n──── DELETE BUS ────");
        viewAllBuses();

        System.out.print("\nEnter Bus ID to delete: ");
        Long busId = getLongInput();

        busService.deleteBus(busId);
        System.out.println("✓ Bus deleted successfully!");
    }

    private void viewAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        System.out.println("\n──── ALL RESERVATIONS ────");

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            reservations.forEach(res -> {
                System.out.println("\n┌─────────────────────────────────");
                System.out.println("│ Reservation ID: " + res.getReservationId());
                System.out.println("│ User: " + res.getUser().getName());
                System.out.println("│ Bus: " + res.getBus().getBusNumber());
                System.out.println("│ Travel Date: " + res.getTravelDate());
                System.out.println("│ Seats: " + res.getNumberOfSeats());
                System.out.println("│ Total Fare: ₹" + res.getTotalFare());
                System.out.println("│ Status: " + res.getStatus());
                System.out.println("└─────────────────────────────────");
            });
        }
    }

    private void viewAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        System.out.println("\n──── ALL FEEDBACKS ────");

        if (feedbacks.isEmpty()) {
            System.out.println("No feedbacks available.");
        } else {
            feedbacks.forEach(fb -> {
                System.out.println("\n┌─────────────────────────────────");
                System.out.println("│ Feedback ID: " + fb.getFeedbackId());
                System.out.println("│ User: " + fb.getUser().getName());
                if (fb.getBus() != null) {
                    System.out.println("│ Bus: " + fb.getBus().getBusNumber());
                }
                System.out.println("│ Rating: " + fb.getRating() + "/5");
                System.out.println("│ Comment: " + fb.getComment());
                System.out.println("└─────────────────────────────────");
            });
        }
    }

    private void viewAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("\n──── ALL USERS ────");

        if (users.isEmpty()) {
            System.out.println("No users registered.");
        } else {
            users.forEach(user -> {
                System.out.println("\n┌─────────────────────────────────");
                System.out.println("│ User ID: " + user.getUserId());
                System.out.println("│ Name: " + user.getName());
                System.out.println("│ Email: " + user.getEmail());
                System.out.println("│ Phone: " + user.getPhoneNumber());
                System.out.println("└─────────────────────────────────");
            });
        }
    }

    // ========== UTILITY METHODS ==========

    private int getIntInput() {
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
            return 0;
        }
    }

    private Long getLongInput() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid ID.");
            return 0L;
        }
    }

    private Double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
            return 0.0;
        }
    }
}
