CREATE SCHEMA app
    AUTHORIZATION postgres;

CREATE TABLE app.current_weather
(
    id uuid,
    "timestamp" timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    temperature real NOT NULL,
    wind_mph integer NOT NULL,
    pressure_mb integer NOT NULL,
    humidity smallint NOT NULL,
    "condition" text NOT NULL,
    location text NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.current_weather
    OWNER to postgres;

    INSERT INTO app.current_weather
    	(id, "timestamp", last_updated, temperature, wind_mph, pressure_mb, humidity, condition, location)
    VALUES ('0d3b93ea-0c69-41aa-a953-b1d7019b196d', '2023-12-02 17:27:26.310019', '2023-12-02 17:15:00', -6.8, 4000, 1010, 98, 'Freezing fog', 'Minsk'),
           ('0d3b93ea-0c69-41aa-a954-b1d7019b196d', '2023-12-02 19:27:28.310019', '2023-12-02 18:15:00', -7, 4000, 1010, 98, 'Freezing fog', 'Minsk');

CREATE TABLE app.history_weather
(
    id uuid,
    "date" timestamp without time zone NOT NULL,
    avg_temperature real NOT NULL,
    avg_wind_mph integer NOT NULL,
    avg_pressure_mb integer NOT NULL,
    avg_humidity smallint NOT NULL,
    "condition" text NOT NULL,
    location text NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.history_weather
    OWNER to postgres;

    INSERT INTO app.history_weather
    	(id, "timestamp", avg_temperature, avg_wind_mph, avg_pressure_mb, avg_humidity, "condition", location)
    VALUES
    ('b2209215-d588-46bb-8d13-6242277b0d9e', '2023-11-25 00:00:00',	-1.9, 20000, 994, 92, 'Moderate snow', 'Minsk' ),
    ('b2209216-d588-46bb-8d13-6242277b0d9e', '2023-11-26 00:00:00',	-3.1, 28000, 994, 92, 'Light snow', 'Minsk' ),
    ('b2209217-d588-46bb-8d13-6242277b0d9e', '2023-11-27 00:00:00',	-5, 26000, 994, 88, 'Light snow', 'Minsk' ),
    ('b2209218-d588-46bb-8d13-6242277b0d9e', '2023-11-28 00:00:00',	-7, 32000, 994, 92, 'Light freezing rain', 'Minsk' ),
    ('b2209219-d588-46bb-8d13-6242277b0d9e', '2023-11-29 00:00:00',	-1, 20000, 994, 94, 'Heavy snow', 'Minsk' );
