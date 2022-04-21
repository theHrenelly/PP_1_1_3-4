package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                String query = new String(Files.readAllBytes(Paths.get("src/main/resources/UsersTable.sql")));
                connection.setAutoCommit(false);
                statement.executeUpdate(query);
                connection.commit();
                System.out.println("Created table of users");
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ebat) {
                    System.err.println(ebat.getMessage());
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                connection.setAutoCommit(false);
                statement.executeUpdate("drop table if exists users");
                connection.commit();
                System.out.println("Dropped the table of users");
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ebat) {
                    System.err.println(ebat.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection()) {
            try (PreparedStatement preST = connection.prepareStatement("insert into users (name, lastName, age)" +
                    "values (?, ?, ?)")) {

                connection.setAutoCommit(false);
                preST.setString(1, name);
                preST.setString(2, lastName);
                preST.setByte(3, age);
                preST.executeUpdate();
                connection.commit();
                System.out.printf("User %s added to the table\n", name);
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ebat) {
                    System.err.println(ebat.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection()) {
            try (PreparedStatement preST = connection.prepareStatement("delete from users where id = ?")) {

                connection.setAutoCommit(false);
                preST.setLong(1, id);
                preST.executeUpdate();
                connection.commit();
                System.out.printf("User with id %d deleted from the table\n", id);
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ebat) {
                    System.err.println(ebat.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            try (Statement preST = connection.createStatement()) {

                connection.setAutoCommit(false);
                ResultSet rs = preST.executeQuery("select * from users");

                while (rs.next()) {
                    User user = new User(rs.getString("name"), rs.getString("lastName"),
                            rs.getByte("age"));
                    user.setId(rs.getLong("id"));
                    list.add(user);
                }

                    connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ebat) {
                    System.err.println(ebat.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection()) {
            try (Statement preST = connection.createStatement()) {

                connection.setAutoCommit(false);
                preST.executeUpdate("delete from users");
                connection.commit();
                System.out.println("The table of users is cleared");
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ebat) {
                    System.err.println(ebat.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
