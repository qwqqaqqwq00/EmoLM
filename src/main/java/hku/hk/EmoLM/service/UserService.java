package hku.hk.EmoLM.service;

import hku.hk.EmoLM.util.UserBloomFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hku.hk.EmoLM.dao.UserDao;

import java.util.Date;
import java.util.Map;

@Service
public class UserService {

    private static final String SECRET_KEY = "[-70, -71, 9, 111, -75, 124, 109, 54, 63, -118, 31, -92, 19, 95, -9, 65, 18, 92, 59, -110, 47, -120, 43, 114, 104, 117, 68, -107, -13, -81, -113, 83, -102, -12, 85, 44, 30, -10, -100, -43, 54, 77, -93, -108, -102, -34, 52, 43, -54, 19, -78, 55, -114, 44, -16, -17, 105, -54, -69, 111, 19, -45, -107, 4]";

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserBloomFilter userBloomFilter;

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

    public Object UserToken2Uid(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            } else {
                return null;
            }

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.get("uid") == null) {
                return null;
            }
            if(!userBloomFilter.mightContain((Integer) claims.get("uid"))){
                return null;
            }
            Object uid = userDao.getUserId((Integer) claims.get("uid"));
            userBloomFilter.add((Integer) uid);
            return uid;
        } catch (Exception e) {
            return null;
        }
    }

    public String JWTToken(String username, String encryptedPassword) {
        return Jwts.builder()
                .setSubject(username)
                .claim("uid", getUserIdByUsername(username, encryptedPassword))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours expiration
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512) // Use Keys.hmacShaKeyFor
                .compact();
    }
}