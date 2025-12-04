# Testing Guide

This document provides comprehensive testing documentation for the User Pet API backend.

## Test Overview

The project includes **37+ test cases** covering all layers of the application:
- **Unit Tests**: Service layer with mocked dependencies
- **Integration Tests**: Controller layer with Spring Boot Test
- **API Integration Tests**: Real external API calls

## Test Files

### 1. Service Layer Tests

#### DogImageServiceImplTest (5 tests)
Tests the Dog CEO API integration service.

**Test Cases:**
- ✅ `testFetchRandomDogImages_WithValidCount_ShouldReturnImages` - Validates image count and URL format
- ✅ `testFetchRandomDogImages_WithCount1_ShouldReturnSingleImage` - Single image request
- ✅ `testFetchRandomDogImages_WithCount50_ShouldReturnMaxImages` - Maximum API limit (50 images)
- ✅ `testFetchRandomDogImages_WithZeroCount_ShouldReturnEmptyOrError` - Invalid count handling
- ✅ `testFetchRandomDogImages_MultipleCalls_ShouldReturnDifferentImages` - Randomness verification

**Coverage:** External API integration, error handling, data validation

---

#### RandomUserServiceImplTest (10 tests)
Tests the Random User API integration service.

**Test Cases:**
- ✅ `testFetchRandomUsers_WithValidCount_ShouldReturnUsers` - User data structure validation
- ✅ `testFetchRandomUsers_WithNationality_ShouldReturnFilteredUsers` - Nationality filtering
- ✅ `testFetchRandomUsers_WithFixedSeed_ShouldReturnConsistentResults` - Deterministic results
- ✅ `testFetchRandomUsers_WithInvalidCountTooLow_ShouldReturnDefaultCount` - Min boundary
- ✅ `testFetchRandomUsers_WithInvalidCountTooHigh_ShouldReturnDefaultCount` - Max boundary
- ✅ `testFetchRandomUsers_WithNullNationality_ShouldReturnAllNationalities` - Null handling
- ✅ `testFetchRandomUsers_WithEmptyNationality_ShouldReturnAllNationalities` - Empty string handling
- ✅ `testFetchRandomUsers_WithMultipleNationalities_ShouldWork` - Multiple country filtering
- ✅ `testFetchRandomUsers_UserValidation_ShouldFilterInvalidUsers` - Data validation
- ✅ `testFetchRandomUsers_WithDefaultOverload_ShouldWork` - Method overloading

**Coverage:** External API integration, nationality filtering, fixed seed behavior, validation

---

#### UserWithPetAggregatorServiceTest (10 tests)
Tests the core aggregation logic with mocked dependencies.

**Test Cases:**
- ✅ `testGetUsersWithPets_ShouldAggregateSuccessfully` - Successful user-pet pairing
- ✅ `testGetUsersWithPets_WithNationality_ShouldPassNationalityFilter` - Filter propagation
- ✅ `testGetUsersWithPets_WhenMoreUsersThanImages_ShouldReturnOnlyMatchedPairs` - Dog API limit handling
- ✅ `testGetUsersWithPets_WhenMoreImagesThanUsers_ShouldReturnAllUsers` - Mismatched counts
- ✅ `testGetUsersWithPets_WithCountBelowMinimum_ShouldNormalizeToDefault` - Min normalization
- ✅ `testGetUsersWithPets_WithCountAboveMaximum_ShouldNormalizeToMax` - Max normalization (1000)
- ✅ `testGetUsersWithPets_WhenServicesReturnEmptyLists_ShouldReturnEmptyList` - Empty handling
- ✅ `testGetUsersWithPets_ShouldPreserveUserData` - Data integrity verification
- ✅ `testGetUsersWithPets_WithValidCount_ShouldWork` - Valid count handling
- ✅ `testGetUsersWithPets_WithDefaultOverload_ShouldWork` - Method overloading

**Coverage:** Aggregation logic, count normalization, data preservation, edge cases

---

### 2. Controller Layer Tests

#### UserWithPetControllerTest (12 tests)
Spring Boot integration tests for REST API endpoints.

**Test Cases:**
- ✅ `testGetUsersWithPet_NoParameters_ShouldReturnDefaultUsers` - Default behavior (50 users)
- ✅ `testGetUsersWithPet_WithResultsParameter_ShouldReturnSpecifiedCount` - Custom count
- ✅ `testGetUsersWithPet_WithNationalityParameter_ShouldFilterByNationality` - Nationality filter
- ✅ `testGetUsersWithPet_WithBothParameters_ShouldApplyBoth` - Combined parameters
- ✅ `testGetUsersWithPet_WithResults1_ShouldReturnSingleUser` - Minimum count
- ✅ `testGetUsersWithPet_WithResults50_ShouldWork` - Dog API limit
- ✅ `testGetUsersWithPet_WithResults100_ShouldCallServiceWith100` - Above API limit
- ✅ `testGetUsersWithPet_VerifyResponseStructure` - JSON structure validation
- ✅ `testGetUsersWithPet_WhenServiceReturnsEmptyList_ShouldReturnEmptyArray` - Empty response
- ✅ `testGetUsersWithPet_VerifyCORSEnabled` - CORS configuration
- ✅ `testGetUsersWithPet_WithDifferentNationalities_ShouldWork` - Multiple countries
- ✅ `testGetUsersWithPet_VerifyHTTPStatus_ShouldReturn200` - Status code validation

**Coverage:** REST API behavior, parameter handling, CORS, response format, status codes

---

### 3. Application Tests

#### UserpetApiApplicationTests (1 test)
Basic Spring Boot application context test.

**Test Cases:**
- ✅ `contextLoads` - Verifies Spring Boot application starts correctly

---

## Running Tests

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=UserWithPetControllerTest
./mvnw test -Dtest=RandomUserServiceImplTest
```

### Run Specific Test Method
```bash
./mvnw test -Dtest=UserWithPetControllerTest#testGetUsersWithPet_NoParameters_ShouldReturnDefaultUsers
```

### Run with Verbose Output
```bash
./mvnw test -X
```

### Run Tests in Docker
```bash
docker build -t userpet-test --target build .
docker run userpet-test ./mvnw test
```

## Test Requirements

### Prerequisites
- **JDK 17+** (not JRE - must have Java compiler)
- **Maven 3.6+**
- **Internet connection** (some tests make real API calls)

### Verify JDK Installation
```bash
# Check Java version
java -version

# Verify JDK (should show javac)
javac -version

# Set JAVA_HOME if needed (Windows)
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Set JAVA_HOME if needed (Linux/Mac)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

## Test Patterns

### AAA Pattern (Arrange-Act-Assert)
All tests follow the AAA pattern for clarity:

```java
@Test
void testMethodName_WithCondition_ShouldExpectedBehavior() {
    // Arrange - Set up test data and mocks
    int count = 10;
    when(mockService.method()).thenReturn(mockData);

    // Act - Execute the method being tested
    List<Result> result = service.performAction(count);

    // Assert - Verify the outcome
    assertNotNull(result);
    assertEquals(10, result.size());
    verify(mockService, times(1)).method();
}
```

### Naming Convention
Test names clearly describe:
1. What is being tested
2. Under what conditions
3. What the expected outcome is

Format: `test[Method]_[Condition]_Should[Outcome]`

Examples:
- `testFetchRandomDogImages_WithValidCount_ShouldReturnImages`
- `testGetUsersWithPets_WhenMoreUsersThanImages_ShouldReturnOnlyMatchedPairs`

## Test Categories

### Unit Tests (Fast)
- Mock all external dependencies
- No network calls
- Tests isolated logic
- Examples: UserWithPetAggregatorServiceTest

### Integration Tests (Medium)
- Real external API calls
- Tests actual integration
- Requires internet connection
- Examples: DogImageServiceImplTest, RandomUserServiceImplTest

### Controller Tests (Fast)
- Uses Spring Boot Test with MockMvc
- Mocked service layer
- Tests REST API behavior
- Example: UserWithPetControllerTest

## Key Test Scenarios

### ✅ Happy Path Tests
- Valid inputs return expected outputs
- Data flows correctly through layers
- API integration works as expected

### ✅ Edge Cases
- Minimum/maximum count boundaries
- Empty results handling
- Null/empty nationality filters
- Mismatched user/image counts

### ✅ Error Handling
- Invalid count normalization
- Network error resilience
- Invalid data filtering
- API limit handling (50 images max)

### ✅ Business Logic
- Fixed seed ensures deterministic results
- Nationality filtering works correctly
- User-pet pairing preserves data integrity
- Count normalization follows business rules

## Important Test Findings

### Dog API Limitation
Tests verify that requests for more than 50 users only return 50 results due to Dog CEO API's maximum limit of 50 images per request.

**Tested in:**
- `testFetchRandomDogImages_WithCount50_ShouldReturnMaxImages`
- `testGetUsersWithPets_WhenMoreUsersThanImages_ShouldReturnOnlyMatchedPairs`
- `testGetUsersWithPet_WithResults100_ShouldCallServiceWith100`

### Fixed Seed Behavior
Tests confirm that using the fixed seed `aimopark2025` returns identical results across multiple calls.

**Tested in:**
- `testFetchRandomUsers_WithFixedSeed_ShouldReturnConsistentResults`

### Data Validation
Tests ensure invalid users (missing ID, invalid email) are filtered out.

**Tested in:**
- `testFetchRandomUsers_UserValidation_ShouldFilterInvalidUsers`

## Troubleshooting Tests

### "No compiler is provided" Error
```bash
# Issue: Running on JRE instead of JDK
# Solution: Set JAVA_HOME to JDK directory
set JAVA_HOME=C:\Program Files\Java\jdk-17
./mvnw test
```

### Network Errors in Tests
```bash
# Issue: External API calls fail
# Possible causes:
# 1. No internet connection
# 2. API rate limiting
# 3. API temporarily down
# Solution: Retry or check connectivity
```

### Test Timeouts
```bash
# Issue: External API calls taking too long
# Solution: Increase timeout in test
@Test(timeout = 10000) // 10 seconds
```

## Continuous Integration

Example GitHub Actions workflow:
```yaml
name: Backend Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run tests
        run: cd backend && ./mvnw test
```

## Test Metrics

- **Total Test Classes:** 5
- **Total Test Cases:** 37+
- **Service Layer Coverage:** 25 tests
- **Controller Layer Coverage:** 12 tests
- **Application Tests:** 1 test

## Future Test Improvements

Potential additions:
- Add test coverage reporting (JaCoCo)
- Performance/load testing
- Mocked API responses for faster unit tests
- Contract testing for external APIs
- End-to-end tests with TestContainers
