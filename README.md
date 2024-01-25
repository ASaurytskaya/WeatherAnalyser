# Weather Analyzer

## Overview:

This project – Weather Analyzer app that leverages third-party API (WeatherAPI.com), to fetch and store weather data for a predefined city at scheduled intervals. The app features two endpoints: one delivering real-time weather information and another providing calculated average daily temperatures over specified periods.

## Functionality:

The application operates as follows:

1. The application retrieves weather information for a predefined city (e.g., Minsk) from a third-party API at scheduled intervals and stores it in a database.
2. The city for weather inquiries remains constant.

## Endpoints:

1. **Current Weather Endpoint:**
    
        http://localhost:8080/api/v1/weather

    - Response: Provides information about the current weather, reflecting the most up-to-date data stored in the service's database.
    - Information Included:
        - Temperature in C
        - Wind speed in m/h
        - Atmospheric pressure in  millibars
        - Air humidity
        - Weather conditions (e.g., sunny, cloudy)
        - Location

2. **Average Daily Temperature Endpoint:**

        http://localhost:8080/api/v1/weather/history?from=2023-11-11&to=2023-11-15
    - Request parameters: Users should provide two dates in yyyy-MM-dd format to specify the period they are interested in. This period is inclusive of both the starting date ('from') and the ending date ('to').
    - Response: Calculates and provides information about the average daily temperature and other parameters based on stored historical data or the available information in the service.
    - User Interaction: Users can request information for a specified period. 

## Additional Details:

- The application utilizes a third-party API (e.g., WeatherAPI.com) to gather weather data.
- Weather information is stored in a database for efficient retrieval.
- The system maintains a consistent city for weather inquiries.

The following technologies/frameworks were used in this project:
        - Spring:
            *Spring JPA;
            *Spring Boot;
        - PostgreSQL;
        - Hibernate;
        - OpenFeign; 
        - Maven;
        - Docker;
        - Nginx.

### Prerequisites

- Java 17 or higher
- Docker

## Installation

1. Clone the repository:

         # git clone https://github.com/ASaurytskaya/WeatherAnalyser weather_analyser
2. Navigate to the project directory: 

         # cd weather_analyser
3. Set up environmental  variables in compose.yaml file.
4. Run application: 

         # docker compose up
 
