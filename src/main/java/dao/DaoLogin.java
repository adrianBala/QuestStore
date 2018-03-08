package dao;

import model.User;
import model.Admin;
import model.Mentor;
import model.Student;


import java.sql.*;

public class DaoLogin implements IDaoLogin {

    @Override
    public User getUser(String email, String password){
        User user = null;

        String query = "SELECT * FROM users WHERE email= ? AND password= ?;";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.isClosed()) {
                int id_role = resultSet.getInt("id_role");
                String role = getRole(id_role);
                user = createUser(resultSet, role);
            }

        }catch(SQLException e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return user;
    }

    @Override
    public String getRole(int id_role){

        String role = null;

        String query = "SELECT name FROM roles WHERE id_role= ?;";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setInt(1, id_role);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {

                if (!resultSet.isClosed()) {
                    role = resultSet.getString("name");

                }
            }

        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return role;
    }

    private User createUser(ResultSet resultSet, String role){
        User user = null;

        int userId = 0;
        String name = null;
        String password = null;
        String email = null;

        try {
            userId = resultSet.getInt("id_user");
            name = resultSet.getString("name");
            password = resultSet.getString("password");
            email = resultSet.getString("email");
        } catch (SQLException e) {
            return user;
        }

        switch (role.toUpperCase()) {
            case "ADMIN":
                user = new DaoAdmin().importAdmin(userId);
                break;
            case "MENTOR":
                user = new DaoMentor().importMentor(userId);
                break;
            case "STUDENT":
                user = new DaoStudent().importStudent(userId);
                break;
        }

        return user;
    }

}