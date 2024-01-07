import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "*****";
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patients");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your Choice: ");
                int choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        // Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // view patient
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        // view doctors
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        // book appointments;
                        bookAppointment(connection,scanner,patient,doctor);
                        System.out.println();
                        break;
                    case 5:
                        // Exit
                        //System.out.println();
                        return;
                    default:
                        System.out.print("Enter Valid Choice!!");
                        System.out.println();
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Connection connection, Scanner scanner, Patient patient, Doctor doctor){
        System.out.println("Enter Patient ID: ");
        int patientID= scanner.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorID= scanner.nextInt();
        System.out.println("Enter Appointment date(YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getPatientByID(patientID) && doctor.getDoctorByID(doctorID)){
            if(checkAvailable(doctorID, appointmentDate, connection)){
                String query ="INSERT INTO appointments(patient_id, doctor_id, appointment) VALUES(?, ?, ?);";
                try {
                    PreparedStatement preparedStatement= connection.prepareStatement(query);
                    preparedStatement.setInt(1,patientID);
                    preparedStatement.setInt(2,doctorID);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();
                    if(affectedRows>0){
                        System.out.println("Appointment booked");
                    }else{
                        System.out.println("Failed to book Appointment");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not Available!!");
            }

        }else{
            System.out.println("Invalid patient or doctor details!!");
        }

    }

    public static boolean checkAvailable(int doctor_id, String appointmentDate, Connection connection ){
        String query= "Select Count(*) from appointments where doctor_id=? and appointment = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctor_id);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                int count= resultSet.getInt(1);
                if(count==0){
                    return true;
                }
                else{
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}