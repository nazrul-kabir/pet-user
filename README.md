# User Pet API - Full Stack Application

A full-stack application that aggregates random user data with pet images, featuring a Java Spring Boot backend and a React TypeScript frontend.

## Overview

This application combines data from two external APIs to create a unified user experience:
- **Random User API**: Provides random user profiles
- **Dog CEO API**: Provides random dog images

The application consists of:
- **Backend**: Java Spring Boot REST API (port 8080)
- **Frontend**: React TypeScript SPA (port 3000)

## Features

- 🎯 RESTful API with configurable parameters
- 🔄 Real-time data aggregation from external APIs
- 🐕 Random dog image pairing for each user
- 🌍 Country-wise filtering support
- 🎲 Deterministic results with fixed seed
- 🐳 Fully containerized with Docker
- ⚡ Modern React frontend with TypeScript
- 🎨 Styled with Tailwind CSS

## Tech Stack

### Backend
- Java 17
- Spring Boot 4.0.0
- Maven
- Lombok
- Jackson

### Frontend
- React 18
- TypeScript
- Tailwind CSS
- Vite

## Quick Start

### Option 1: Docker (Recommended)

The easiest way to run the entire application:

```bash
# From the project root
docker-compose up --build
```

**Access the application:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api/users-with-pet

**Stop the application:**
```bash
docker-compose down
```

For detailed Docker instructions, see [DOCKER.md](DOCKER.md).

### Option 2: Run Locally (Development)

#### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Node.js 18 or higher
- npm or yarn

#### Backend Setup

```bash
# Navigate to backend directory
cd backend

# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Or run the JAR directly
java -jar target/userpet-api-0.0.1-SNAPSHOT.jar
```

The backend will start on http://localhost:8080

#### Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

The frontend will start on http://localhost:3000

## API Usage

### Get Users with Pets

```bash
# Get 50 users (default)
curl http://localhost:8080/api/users-with-pet

# Get specific number of users
curl http://localhost:8080/api/users-with-pet?results=20

# Get users from specific country
curl http://localhost:8080/api/users-with-pet?results=20&nat=FI

# Combine parameters
curl http://localhost:8080/api/users-with-pet?results=50&nat=US
```

### Query Parameters

| Parameter | Type   | Default | Description                        |
|-----------|--------|---------|------------------------------------|
| `results` | int    | 50      | Number of users (1-1000)          |
| `nat`     | string | null    | Country code (e.g., FI, US, GB)   |

**Important Note:** Due to Dog CEO API limitations, requests for more than 50 users will only return 50 users with pet images. While the `results` parameter accepts values up to 1000, the actual number of users returned is capped at 50 due to the external API constraint.

### Supported Countries

`AU`, `BR`, `CA`, `CH`, `DE`, `DK`, `ES`, `FI`, `FR`, `GB`, `IE`, `IN`, `IR`, `MX`, `NL`, `NO`, `NZ`, `RS`, `TR`, `UA`, `US`

### Response Format

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
    "petImage": "https://images.dog.ceo/breeds/pariah-indian/..."
  }
]
```

## Project Structure

```
pet-user/
├── backend/                 # Java Spring Boot API
│   ├── src/
│   │   ├── main/java/
│   │   │   └── com/example/userpet_api/
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── service/         # Business logic
│   │   │       ├── model/           # Data models
│   │   │       └── config/          # Configuration
│   │   └── resources/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md           # Backend documentation
│
├── frontend/               # React TypeScript SPA
│   ├── src/
│   │   ├── components/    # React components
│   │   └── ...
│   ├── package.json
│   ├── Dockerfile
│   ├── nginx.conf
│   └── README.md          # Frontend documentation
│
├── docker-compose.yml     # Docker orchestration
├── DOCKER.md             # Docker deployment guide
└── README.md             # This file
```

## Development

### Backend Development

For detailed backend information, see [backend/README.md](backend/README.md).

```bash
cd backend

# Run with auto-reload
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build JAR
./mvnw clean package
```

### Frontend Development

For detailed frontend information, see [frontend/README.md](frontend/README.md).

```bash
cd frontend

# Start dev server with hot reload
npm start

# Run tests
npm test

# Build for production
npm run build
```

### Running Backend and Frontend Together (Development)

**Terminal 1 - Backend:**
```bash
cd backend
./mvnw spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm start
```

## Docker Deployment

### Full Application

```bash
# Build and start all services
docker-compose up --build

# Run in detached mode
docker-compose up --build -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Individual Services

**Backend only:**
```bash
cd backend
docker build -t users-pets-backend .
docker run -p 8080:8080 users-pets-backend
```

**Frontend only:**
```bash
cd frontend
docker-compose up --build
```

For comprehensive Docker instructions, see [DOCKER.md](DOCKER.md).

## Configuration

### Backend Configuration

The backend uses centralized configuration in `backend/src/main/java/com/example/userpet_api/config/Constants.java`:

- **Fixed seed**: `aimopark2025` (ensures deterministic results)
- **Default user count**: 50
- **User count range**: 1-1000
- **External API URLs**: Configured for RandomUser.me and Dog CEO API

To modify settings, edit `backend/src/main/resources/application.properties`:

```properties
server.port=8080
spring.application.name=userpet-api
```

### Frontend Configuration

The frontend connects to the backend API. If running locally with custom ports, update the API endpoint in the frontend source.

## Testing

### Backend Tests

```bash
cd backend
./mvnw test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## Troubleshooting

### Port Conflicts

**Backend (8080):**
- Edit `backend/src/main/resources/application.properties`: `server.port=8081`
- Or set environment variable: `SERVER_PORT=8081`

**Frontend (3000):**
- Frontend dev server will prompt to use a different port
- For Docker, modify `docker-compose.yml` ports mapping

### Network Issues

- Ensure internet connectivity for external API calls
- Check firewall settings if APIs are blocked
- Verify CORS configuration in backend for local development

### Docker Issues

```bash
# Rebuild without cache
docker-compose build --no-cache

# Clean up everything
docker-compose down -v --rmi all

# View container logs
docker-compose logs -f backend
docker-compose logs -f frontend
```

## Performance Notes

- The API makes two external API calls per request
- Response time depends on external API availability
- Consider implementing caching for production use
- The fixed seed ensures consistent results across requests

## External APIs

- **RandomUser.me**: https://randomuser.me/api/
- **Dog CEO API**: https://dog.ceo/api/

## License

This project is provided as-is for demonstration purposes.

## Additional Resources

- [Backend Documentation](backend/README.md) - Detailed API and architecture info
- [Frontend Documentation](frontend/README.md) - React setup and development
- [Docker Deployment Guide](DOCKER.md) - Comprehensive containerization guide

## Support

For issues or questions:
1. Check the troubleshooting sections in this README and [DOCKER.md](DOCKER.md)
2. Review component-specific READMEs in `backend/` and `frontend/`
3. Verify all prerequisites are installed correctly
