package hku.hk.EmoLM.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hku.hk.EmoLM.dao.UserDao;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    // Verify user login
    public boolean authenticateUser(String username, String password) {
        return userDao.authenticateUser(username, password) != null;
    }

    // Register new user
    public boolean registerUser(String username, String password, String email) {
        return userDao.registerUser(username, password, email);
    }

    public Object getUserIdByUsername(String username, String password) {
        return userDao.getUserIdByUsername(username, password);
    }
}