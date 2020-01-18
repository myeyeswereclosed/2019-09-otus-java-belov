package ru.otus.hw11.db;

import ru.otus.hw10.api.entity.User;
import ru.otus.hw10.api.service.DBServiceUser;
import ru.otus.hw11.cache.HwCache;
import java.util.Optional;

public class CachedUserDbService implements DBServiceUser {
    private DBServiceUser service;
    private HwCache<String, User> cache;

    public CachedUserDbService(DBServiceUser service, HwCache<String, User> cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        long id = service.saveUser(user);

        cacheUser(user);

        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        return
            Optional
                .ofNullable(cache.get(String.valueOf(id)))
                .or(() -> service.getUser(id).map(this::cachedUser))
        ;
    }

    private User cachedUser(User user) {
        cacheUser(user);
        return user;
    }

    private void cacheUser(User user) {
        cache.put(String.valueOf(user.getId()), user);
    }
}
