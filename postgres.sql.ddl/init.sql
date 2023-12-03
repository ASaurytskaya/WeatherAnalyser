CREATE SCHEMA app
   AUTHORIZATION weather_analyser_manager;

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
            OWNER to weather_analyser_manager;

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
            OWNER to weather_analyser_manager;


