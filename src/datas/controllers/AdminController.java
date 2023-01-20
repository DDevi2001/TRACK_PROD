package datas.controllers;

import Users.User;

public interface AdminController {

    User getUser(String id);

    void addUser(String id, String role);

    boolean isUser(String id);

}
