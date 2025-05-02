# 🚆 Railway Management System

A console-based Railway Management System developed in Java that allows admins to manage trains and users, and enables passengers to book and cancel train tickets. It uses file handling for persistent storage of train details, user data, and ticket history.

---

## 📁 Features

### 👤 Admin
- Add and remove trains
- Manage Trains
- View all registered users
- Add New User
- Authorization 

### 👥 Passenger
- Book and cancel tickets
- View ticket history
- Check Train Details

### 🗃️ Data Storage
- Train data stored in `trainData.txt`
- User data and their ticket history stored in `userData.txt`
- Ticket bookings stored in `ticketBookings.txt`

---

## 📂 File Structure

```
 RailwayManagementSystem/
├── trainData.txt            # Stores train details (ID, name, seats)
├── userData.txt             # Stores user credentials and ticket history
├── ticketBookings.txt       # Stores booking logs
├── Main.java                # Main source code file
└── README.md
```
---

## 🛠️ How It Works

### Classes

#### `Train`
- Attributes: `trainID`, `trainName`, `totalSeats`, `availableSeats`
- Methods:
  - `bookSeat()` / `cancelSeat()`
  - `displayTrainDetails()`
  - `toFileString()` / `fromFileString()`

#### `User`
- Attributes: `username`, `password`, `userType`, `ticketHistory`
- Methods:
  - `checkPassword()`
  - `addTicketHistory()`
  - `displayTicketHistory()`
  - `toFileString()` / `fromFileString()`

#### `RailwayManagementSystem`
- Manages:
  - Train and user operations
  - Admin and passenger login flow
  - Booking, cancellation, and data persistence

---

## ▶️ How to Run

1. **Compile the program:**

```bash
   javac Main.java

```
2. **Run the program:**
   
```bash
  java Main

```


## **Contributing** 🤝
Contributions are welcome! If you’d like to improve LetMeCut, feel free to fork the repo and submit a pull request.

### **Steps to Contribute:**
**Fork the repository**
### **1. Create a new branch:**
```bash
git checkout -b feature-branch
```

### **2. Make your changes and commit:**

```bash
git commit -m "Added new feature"
```
### **3. Push to the branch:**
```bash
git push origin feature-branch
```
### **Open a Pull Request**
---
## **License** 📜
This project is licensed under the MIT License.

💡 Developed with ❤️ by [Mohammad Hashim](https://github.com/mohammadhashim135/Railway-Management-JAVA.git)

