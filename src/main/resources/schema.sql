CREATE TABLE IF NOT EXISTS rating
(
    rating_id INTEGER auto_increment,
    name      VARCHAR(20) NOT NULL,
    constraint "RATING_pk"
        primary key (rating_id)
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id INTEGER auto_increment,
    name     VARCHAR(100) NOT NULL,
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS film
(
    film_id      INTEGER auto_increment,
    name         VARCHAR(128) NOT NULL,
    description  VARCHAR(200) DEFAULT 'just film description',
    release_date DATE         NOT NULL,
    duration     INTEGER,
    rate         INTEGER,
    rating_id    INTEGER,
    primary key (FILM_ID),
    constraint "FILM_RATING_RATING_ID_fk"
        foreign key (rating_id) references rating

);

CREATE TABLE IF NOT EXISTS film_genre
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "FILM_GENRE_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "FILM_GENRE_GENRE_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRE

);

CREATE TABLE IF NOT EXISTS users
(
    user_id  INTEGER auto_increment,
    email    VARCHAR(128) NOT NULL,
    login    VARCHAR(128) NOT NULL,
    name     VARCHAR(128),
    birthday DATE,
    constraint "USER_pk"
        primary key (USER_ID)
);

CREATE TABLE IF NOT EXISTS film_user
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint "MOVIE_LIKE_FILM_FILM_ID_fk"
        foreign key (FILM_ID) references FILM,
    constraint "MOVIE_LIKE_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS
);

create table if not exists FRIEND
(
    PAIR_ID   INTEGER auto_increment,
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    constraint "FRIEND_pk"
        primary key (PAIR_ID),
    constraint "FRIEND_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS,
    constraint "FRIEND_USERS_USER_ID_fk2"
        foreign key (FRIEND_ID) references USERS
);