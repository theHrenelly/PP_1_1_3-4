package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        service.createUsersTable();
        service.saveUser("Sasha", "Grey", (byte) 34);
        service.saveUser("Lana", "Del Rey", (byte) 36);
        service.saveUser("Mila", "Kunis", (byte) 38);
        service.saveUser("Johnny", "Depp", (byte) 58);
        System.out.println(service.getAllUsers());
        service.cleanUsersTable();
        service.dropUsersTable();
        Util.closeSessionFactory();
    }
}
