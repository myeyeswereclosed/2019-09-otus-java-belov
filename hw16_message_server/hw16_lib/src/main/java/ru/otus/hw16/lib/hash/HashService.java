package ru.otus.hw16.lib.hash;

import java.util.Optional;

public interface HashService {
    Optional<String> hash(String value);
}
