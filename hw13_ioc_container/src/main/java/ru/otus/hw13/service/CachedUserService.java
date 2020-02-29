package ru.otus.hw13.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.hw13.cache.Cache;
import ru.otus.hw13.domain.User;

import java.util.List;
import java.util.Optional;

@Service("cachedUserService")
public class CachedUserService implements UserService {
    private UserService service;
    private Cache<String, User> cache;

    @Autowired
    public CachedUserService(
        @Qualifier("commonUserService") UserService service,
        Cache<String, User> cache
    ) {
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
}