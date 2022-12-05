package org.prepay.prepay.service.Imple;

import org.prepay.prepay.model.dao.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private static Map<String, User> userMap = new HashMap<>();

    static {
        String userId = "b6d32f04afd241b8a3026eb8a7212f57";
        User user = new User();
        userMap.put(userId, new User());
    }

    public User getUserById(String userId) {
        if (userMap.containsKey(userId)) {
            return userMap.get(userId);
        }
        return null;
    }
}
