package ru.otus.hw16.db_service.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw16.lib.domain.User;
import ru.otus.hw16.db_service.cache.Cache;
import java.util.List;
import java.util.Optional;

@Service
public class CachedUserService implements UserService {
    private UserService service;
    private Cache<String, User> cache;

    @Autowired
    public CachedUserService(CommonUserService service, Cache<String, User> cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    public Optional<User> save(User user) {
        return 
            service
                .save(user)
                    .map(savedUser -> {
                        cache.put(String.valueOf(user.getId()), user);
                        return savedUser;
                    });
    }

    @Override
    public List<User> allUsers() {
        return service.allUsers();
    }

    @Override
    public List<User> clients() {
        return service.clients();
    }

    @Override
    public Optional<User> getByCredentials(String login, String password) {
        return service.getByCredentials(login, password);
    }
}
