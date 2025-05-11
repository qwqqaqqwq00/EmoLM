package hku.hk.EmoLM.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hku.hk.EmoLM.dao.UserDao;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    // 验证用户登录
    public boolean authenticateUser(String username, String password) {
        return userDao.authenticateUser(username, password) != null;
    }

    // 注册新用户
    public boolean registerUser(String username, String password, String email) {
        return userDao.registerUser(username, password, email);
    }
}