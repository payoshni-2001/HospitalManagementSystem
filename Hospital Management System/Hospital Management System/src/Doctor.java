import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;
    public Doctor(Connection connection){
        this.connection=connection;
        //this.scanner=scanner;
    }

    public void viewDoctor(){
        String query= "select * from doctors;";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);

            ResultSet resultSet= preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+-----------------+----------------+");
            System.out.println("| Doctor ID  | Name            | Specialization |");
            System.out.println("+------------+-----------------+----------------+");
            while (resultSet.next()){
                int id= resultSet.getInt("id");
                String name= resultSet.getString("name");
                String Specialization= resultSet.getString("specialization");
                System.out.printf("|%-12s|%-17s|%-16s|\n",id,name,Specialization);
                System.out.println("+------------+-----------------+----------------+");
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorByID(int id){
        String query="Select * from doctors where id = ? ;";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
