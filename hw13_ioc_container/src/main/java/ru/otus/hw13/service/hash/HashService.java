package ru.otus.hw13.service.hash;

import java.util.Optional;

public interface HashService {
    Optional<String> hash(String value);
}
