MERGE INTO rating (NAME) KEY (NAME)
    VALUES ('G'),
           ('PG'),
           ('PG-13'),
           ('R'),
           ('NC-17');

MERGE INTO genre (NAME) KEY (NAME)
    VALUES ('Комедия'),
           ('Драма'),
           ('Мультфильм'),
           ('Триллер'),
           ('Документальный'),
           ('Боевик');

