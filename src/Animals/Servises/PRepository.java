package Animals.Servises;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

import Animals.ObjectsPets.*;


public class PRepository implements AnimalsRepository <Pet> {
    private Creator petCreator;
    private Statement sqlSt;
    private ResultSet resultSet;
    private String SQLstr;
    
    public PRepository() {
        this.petCreator = new CreatorPet();
    };

    @Override
    public List<Pet> getAll() {
        List<Pet> farm = new ArrayList<Pet>();
        Pet pet;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection dbConnection = getConnection()) {
                sqlSt = dbConnection.createStatement();
                SQLstr = "SELECT GenusId, Id, PetName, Birthday FROM pet_list ORDER BY Id";
                resultSet = sqlSt.executeQuery(SQLstr);
                while (resultSet.next()) {

                    TypeOfPet type = TypeOfPet.getType(resultSet.getInt(1));
                    int id = resultSet.getInt(2);
                    String petName = resultSet.getString(3);
                    LocalDate birthday = resultSet.getDate(4).toLocalDate();
                    
                    pet = petCreator.createPet(type, petName, birthday);
                    pet.setPetId(id);
                    farm.add(pet);
                }
                return farm;
            } 
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(PRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }           
    }

    @Override
    public Pet getById(int petID) {
        Pet pet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection dbConnection = getConnection()) {

                SQLstr = "SELECT GenusId, Id, PetName, Birthday FROM pet_list WHERE Id = ?";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setInt(1, petID);
                resultSet = prepSt.executeQuery();

                if (resultSet.next()) {

                    TypeOfPet type = TypeOfPet.getType(resultSet.getInt(1));
                    int id = resultSet.getInt(2);
                    String petName = resultSet.getString(3);
                    LocalDate birthday = resultSet.getDate(4).toLocalDate();

                    pet = petCreator.createPet(type, petName, birthday);
                    pet.setPetId(id);
                } 
                return pet;
            } 
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(PRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public int create(Pet pet) {
        int rows;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection dbConnection = getConnection()) {

                SQLstr = "INSERT INTO pet_list (PetName, Birthday, GenusId) SELECT ?, ?, (SELECT Id FROM pet_types WHERE Genus_name = ?)";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setString(1, pet.getName());
                prepSt.setDate(2, Date.valueOf(pet.getBirthdayDate())); 
                prepSt.setString(3, pet.getClass().getSimpleName());

                rows = prepSt.executeUpdate();
                return rows;
            }
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(PRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    public void train(int id, String command){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection dbConnection = getConnection()) {
                String SQLstr = "INSERT INTO pet_command (PetId, CommandId) SELECT ?, (SELECT Id FROM commands WHERE Command_name = ?)";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setInt(1, id);
                prepSt.setString(2, command);

                prepSt.executeUpdate();
            }
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(PRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    public List<String> getCommandsById(int petID, int commands_type){   
        
        // commands type = 1  - получить команды, выполняемые питомцем, 2 - команды, выполнимые животным того рода, к которому относится питомец

        List <String> commands = new ArrayList <>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection dbConnection = getConnection()) {
                if (commands_type == 1){
                    SQLstr = "SELECT Command_name FROM pet_command pc JOIN commands c ON pc.CommandId = c.Id WHERE pc.PetId = ?";
                } else {
                    SQLstr = "SELECT Command_name FROM commands c JOIN Genus_command gc ON c.Id = gc.CommandId WHERE gc.GenusId = (SELECT GenusId FROM pet_list WHERE Id = ?)";
                }
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setInt(1, petID);
                resultSet = prepSt.executeQuery();
                while (resultSet.next()) {
                    commands.add(resultSet.getString(1));
                }
                return commands;
            }
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(PRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    @Override
    public int update(Pet pet) {
        int rows;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection dbConnection = getConnection()) {
                SQLstr = "UPDATE pet_list SET PetName = ?, Birthday = ? WHERE Id = ?";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);

                prepSt.setString(1, pet.getName());
                prepSt.setDate(2, Date.valueOf(pet.getBirthdayDate())); 
                prepSt.setInt(3, pet.getPetID());
                
                rows = prepSt.executeUpdate();
                return rows;
            }
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(PRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    @Override
    public void delete(int id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection dbConnection = getConnection()) {
                SQLstr = "DELETE FROM pet_list WHERE Id = ?";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setInt(1,id);
                prepSt.executeUpdate();
            }
        } catch (ClassNotFoundException | IOException | SQLException ex) {
            Logger.getLogger(PRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    public static Connection getConnection() throws SQLException, IOException {

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("Animals/DBResources/db.properties")) {

            props.load(fis);
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            return DriverManager.getConnection(url, username, password);
        }
    }
}
