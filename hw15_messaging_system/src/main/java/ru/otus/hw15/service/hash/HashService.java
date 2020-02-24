package ru.otus.hw15.service.hash;

import java.util.Optional;

public interface HashService {
    Optional<String> hash(String value);
}
