package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface Storage<T> {
    public Map<Integer, T> getStorage();

    public T create(T unit);

    public T update(T unit);
}
