    CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT NOT NULL PRIMARY KEY auto_increment,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    releaseDate DATE NOT NULL,
    duration BIGINT NOT NULL,
    CONSTRAINT film_pk PRIMARY KEY (film_id)
    );

    CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL PRIMARY KEY auto_increment,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (user_id)
    );

    CREATE TABLE IF NOT EXISTS friendship (
    fs_user_id BIGINT NOT NULL,
    fs_friend_id BIGINT NOT NULL,
    UNIQUE(fs_user_id, fs_friend_id),
    CONSTRAINT friendship_pk PRIMARY KEY (fs_user_id, fs_friend_id),
    CONSTRAINT fs_user_pk FOREIGN KEY (fs_user_id) REFERENCES users(user_id),
    CONSTRAINT fs_friend_pk FOREIGN KEY (fs_friend_id) REFERENCES users(user_id)
    );

    CREATE TABLE IF NOT EXISTS likes (
    l_user_id BIGINT NOT NULL,
    l_film_id BIGINT NOT NULL,
    CONSTRAINT likes_pk PRIMARY KEY (l_user_id, l_film_id),
    CONSTRAINT l_user_pk FOREIGN KEY (l_user_id) REFERENCES users(user_id),
    CONSTRAINT l_film_pk FOREIGN KEY (l_film_id) REFERENCES films(film_id)
    );

    CREATE TABLE IF NOT EXISTS genres (
    genre_id BIGINT NOT NULL PRIMARY KEY auto_increment,
    genre_name VARCHAR(255) NOT NULL,
    CONSTRAINT genre_pk PRIMARY KEY (genre_id)
    );

    CREATE TABLE IF NOT EXISTS mpaa_ratings (
    mpaa_id BIGINT NOT NULL PRIMARY KEY auto_increment,
    mpaa VARCHAR(5) NOT NULL,
    CONSTRAINT mpaa_pk PRIMARY KEY (mpaa_id)
    );

    CREATE TABLE IF NOT EXISTS filmMpaa (
    fm_film_id BIGINT NOT NULL,
    fm_mpaa_id BIGINT NOT NULL,
    UNIQUE(fm_film_id, fm_mpaa_id),
    CONSTRAINT filmMpaa_pk PRIMARY KEY (fm_film_id, fm_mpaa_id),
    CONSTRAINT fm_film_pk FOREIGN KEY (fm_film_id) REFERENCES films(film_id),
    CONSTRAINT fm_mpaa_pk FOREIGN KEY (fm_mpaa_id) REFERENCES mpaa_ratings(mpaa_id)
    );

    CREATE TABLE IF NOT EXISTS filmGenre (
    fg_film_id BIGINT NOT NULL,
    fg_genre_id BIGINT NOT NULL,
    UNIQUE(fg_film_id, fg_genre_id),
    CONSTRAINT filmGenre_pk PRIMARY KEY (fg_film_id, fg_genre_id),
    CONSTRAINT fg_film_pk FOREIGN KEY (fg_film_id) REFERENCES films(film_id),
    CONSTRAINT fg_genre_pk FOREIGN KEY (fg_genre_id) REFERENCES genres(genre_id)
    );