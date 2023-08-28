package ru.yandex.practicum.filmorate.storage;

import java.util.Map;

public interface Storage<T> {
    public Map<Integer, T> getStorage();

    public T create(T unit);

    public T update(T unit);
}
