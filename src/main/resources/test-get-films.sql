MERGE INTO films (film_id, name, description, releaseDate, duration)
VALUES (1, 'Jhon Dig', 'Thirst Jhon Dig adventure', '2000-01-01', 123);

MERGE INTO films (film_id, name, description, releaseDate, duration)
VALUES (2, 'Jhon Dig 2', 'Second Jhon Dig adventure', '2001-01-01', 321);

MERGE INTO films (film_id, name, description, releaseDate, duration)
VALUES (3, 'Jhon Dig 3', 'Thrid Jhon Dig adventure', '2002-01-01', 456);

MERGE INTO films (film_id, name, description, releaseDate, duration)
VALUES (4, 'Jhon Dig 4', 'Another Jhon Dig adventure', '2003-01-01', 654);

MERGE INTO filmMpaa (fm_film_id, fm_mpaa_id)
VALUES (1, 2);

MERGE INTO filmMpaa (fm_film_id, fm_mpaa_id)
VALUES (2, 3);

MERGE INTO filmMpaa (fm_film_id, fm_mpaa_id)
VALUES (3, 4);

MERGE INTO filmMpaa (fm_film_id, fm_mpaa_id)
VALUES (4, 5);
