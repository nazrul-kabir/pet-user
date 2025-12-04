# User Pet API

A Java-based REST API service that aggregates random user data with pet images, built with Spring Boot.

## Overview

This service exposes a simple API endpoint that fetches data from two external APIs:
- **Random User API**: Provides random user profiles
- **Dog CEO API**: Provides random dog images

The service combines both data sources into a single JSON response containing user information paired with pet images.

## Features

- 🎯 **REST API Endpoint**: `GET /api/users-with-pet`
- 🔄 **Data Aggregation**: Combines user data with pet images
- 🐕 **External API Integration**: Integrates with RandomUser.me and Dog CEO APIs
- 🌍 **Nationality Filtering**: Filter users by country code (e.g., FI, US, GB)
- 🎲 **Deterministic Results**: Uses fixed seed for consistent, reproducible data
- ⚙️ **Configurable Parameters**: Customize result count and nationality
- ⚡ **Spring Boot**: Built on Spring Boot 4.0.0
- 🧪 **Basic Testing**: Includes a simple Spring Boot test

## Technology Stack

- **Java 17**
- **Spring Boot 4.0.0**
- **Maven**
- **Lombok** (for reducing boilerplate code)
- **Jackson** (for JSON processing)
- **Spring Web MVC**

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Building the Application

```bash
# Clone the repository

# Build the project
./mvnw clean package

# Or if you're on Windows
mvnw.cmd clean package
```

### Running the Application

```bash
# Run the JAR file
java -jar target/userpet-api-0.0.1-SNAPSHOT.jar

# Or use Maven
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## API Documentation

### GET /api/users-with-pet

Returns a list of users with their associated pet images.

**Query Parameters:**

| Parameter | Type   | Required | Default | Description                                          |
|-----------|--------|----------|---------|------------------------------------------------------|
| `results` | int    | No       | 50      | Number of users to return (1-1000)                   |
| `nat`     | string | No       | null    | Nationality filter (e.g., FI, US, GB) - see below    |

**Important Limitation:** The Dog CEO API has a maximum limit of 50 images per request. While the `results` parameter accepts values from 1 to 1000, requests exceeding 50 will only return 50 users with pet images due to this external API constraint.

**Available Nationalities:**
`AU`, `BR`, `CA`, `CH`, `DE`, `DK`, `ES`, `FI`, `FR`, `GB`, `IE`, `IN`, `IR`, `MX`, `NL`, `NO`, `NZ`, `RS`, `TR`, `UA`, `US`

**Response Format:**
```json
[
  {
    "id": "7145588T",
    "gender": "male",
    "country": "FI",
    "name": "Kaylie Greenfelder",
    "email": "Greenfelder@hotmail.com",
    "dob": {
      "date": "1968-03-29T05:26:03.876Z",
      "age": 57
    },
    "phone": "(743) 374-5564 x9928",
    "petImage": "https://images.dog.ceo/breeds/pariah-indian/Indian_Pa..."
  }
]
```

**Example Requests:**

```bash
# Get 50 users from all nationalities (default)
curl http://localhost:8080/api/users-with-pet

# Get 20 users from all nationalities
curl http://localhost:8080/api/users-with-pet?results=20

# Get 20 Finnish users
curl http://localhost:8080/api/users-with-pet?results=20&nat=FI

# Get 50 American users
curl http://localhost:8080/api/users-with-pet?results=50&nat=US

# Get 10 British users
curl http://localhost:8080/api/users-with-pet?results=10&nat=GB
```

**Important Notes:**
- The API uses a **fixed seed** (`aimopark2025`) to ensure consistent, reproducible results
- Each user is paired with a unique random dog image

## Project Structure

```
src/main/java/com/example/userpet_api/
├── UserpetApiApplication.java          # Main Spring Boot application
├── controller/
│   └── UserWithPetController.java     # REST controller
├── model/
│   └── UserWithPet.java               # Data model
└── service/
    ├── UserWithPetAggregatorService.java  # Main aggregation logic
    ├── RandomUserService.java         # Random user service interface
    ├── RandomUserServiceImpl.java     # Random user service implementation
    ├── DogImageService.java          # Dog image service interface
    └── DogImageServiceImpl.java      # Dog image service implementation

src/main/resources/
└── application.properties             # Application configuration

src/test/java/com/example/userpet_api/
└── UserpetApiApplicationTests.java   # Basic Spring Boot test
```

## Configuration

### External APIs

The application uses the following external APIs:

- **RandomUser API**: `https://randomuser.me/api/`
- **Dog CEO API**: `https://dog.ceo/api/breeds/image/random/`

### Centralized Configuration

All constants and configuration values are centralized in `config/Constants.java`:

```java
// User count limits
MIN_USER_COUNT = 1
MAX_USER_COUNT = 1000
DEFAULT_USER_COUNT = 50  // Default for API endpoint (matches Dog API limit)

// Seed for deterministic results
FIXED_SEED = "aimopark2025"

// API URLs
API_URL_RANDOM_USER = "https://randomuser.me/api/?results=%d&seed=%s"
API_URL_RANDOM_USER_WITH_NAT = "https://randomuser.me/api/?results=%d&seed=%s&nat=%s"
API_URL_DOG_IMAGE = "https://dog.ceo/api/breeds/image/random/%d"
```

**To change the seed value:**
1. Open `src/main/java/com/example/userpet_api/config/Constants.java`
2. Modify the `FIXED_SEED` constant
3. Rebuild and restart the app

## Dependencies

### Main Dependencies
- `spring-boot-starter-webmvc` - Web framework support
- `spring-boot-devtools` - Development tools
- `lombok` - Boilerplate code reduction
- `jackson-databind` - JSON processing

### Build Dependencies
- `spring-boot-maven-plugin` - Maven plugin for Spring Boot
- `maven-compiler-plugin` - Java compilation
- `spring-boot-starter-webmvc-test` - Testing support

## Error Handling

The service includes basic error handling:

- **Network Errors**: If external APIs are unavailable, the service returns partial results or empty lists
- **JSON Parsing**: Robust error handling for API response parsing
- **Validation**: Filters out users with missing IDs or pet images

## Testing

Run the tests with:

```bash
./mvnw test
```

The project includes a basic Spring Boot context loading test.

## Development

### Adding New Features

1. **User Data Enhancement**: Modify `UserWithPet.java` model to add new fields
2. **New Pet APIs**: Create new service implementations following the existing pattern
3. **Additional Endpoints**: Add new controller methods in `UserWithPetController.java`

### Code Style

- Follows Spring Boot conventions
- Uses constructor injection where appropriate
- Includes proper package structure
- Uses Java 17 features

## Configuration Options

Currently, the application uses minimal configuration in `application.properties`:

```properties
spring.application.name=userpet-api
```

## External APIs Used

### RandomUser.me API
- **Documentation**: https://randomuser.me/documentation
- **Usage**: Fetches user profiles including name, email, location, etc.
- **Parameters**: Uses `results` parameter to specify number of users

### Dog CEO API
- **Documentation**: https://dog.ceo/dog-api/
- **Usage**: Fetches random dog image URLs
- **Endpoint**: `/api/breeds/image/random/{count}`

## License

This project is provided as-is for demonstration purposes.

## Troubleshooting

**Common Issues:**

1. **Port 8080 in use**: Change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

2. **Network connectivity**: Ensure internet access for external API calls

3. **Java version**: Ensure Java 17+ is installed and JAVA_HOME is set correctly

**Performance Notes:**

- The service makes two external API calls per request
- Response time depends on external API availability
- Consider adding caching for production use

## Recent Enhancements

✅ **Implemented:**
- Configurable user count parameter via `results` query param
- Nationality filtering via `nat` query param
- Fixed seed for deterministic, reproducible results
- Centralized configuration in `Constants.java`
- Enhanced logging and error handling

## Future Enhancements

Potential improvements:
- Implement caching for external API responses
- Add pagination support (page 1, page 2, etc. with same seed)
- Include health check endpoints
- Implement retry mechanisms for external API calls
- Add metrics and monitoring
- Support multiple nationalities in a single request (e.g., `nat=FI,US,GB`)
- Add optional seed parameter to override the fixed seed