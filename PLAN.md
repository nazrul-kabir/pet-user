# Pet User Microservices Migration Plan
**Project:** From simple Spring Boot monolith → clean microservices architecture
**Goal:** Learn production-grade Spring Boot 3.x microservices, AWS free-tier deployment, observability, resilience

## Overall Strategy
- **Repo style:** Mono-repo + multi-module Maven
- **Deployment target:** AWS ECS Fargate (1 task per service, ALB path-based routing, free-tier friendly)
- **Communication:** Synchronous REST (WebClient) for now
- **Non-functionals from day 1:** Actuator, graceful shutdown, structured logging, health/readiness probes
- **Later phases:** Add aggregator service, resilience (Resilience4j), caching (Caffeine → Redis), observability (CloudWatch + X-Ray lite), CI/CD

We'll extract two services first:

1. user-profile
Responsibility: Fetch from RandomUser API, apply params (results, nat, seed="fixed" for reproducibility), return clean list of users (only needed fields: id/uuid, name, email, picture.large, nat, etc.)
Endpoint: ```GET /api/v1/users?results=10&nat=fi``` (returns array of simplified user DTOs)

2. pet-image
Responsibility: Fetch N random dog images from Dog CEO API
Endpoint: ```GET /api/v1/pets?count=10``` (returns array of image URLs)

Later we'll add the aggregator/gateway service that calls both and zips them together like the original ```/api/users-with-pet```.

## Target Bounded Contexts / Services
| Service                  | Endpoint example                        | Responsibility                                      | Status     | Next milestone                  |
|--------------------------|-----------------------------------------|-----------------------------------------------------|------------|---------------------------------|
| user-profile-service     | GET /api/v1/users?results=10&nat=fi     | Fetch + shape RandomUser.me data                    | In progress| Local run & tests               |
| pet-image-service        | GET /api/v1/pets?count=10               | Fetch random dog images from Dog CEO API            | Pending    | After user-profile              |
| api-gateway-service      | GET /api/users-with-pet?...             | Orchestrate: call both services + zip results       | Planned    | PHASE 3                         |
| frontend                 | (React + TS, served via Nginx or S3)    | Existing UI – calls new /api/users-with-pet         | Existing   | Integrate with new gateway      |


## Phase-by-Phase Plan

### PHASE 0 – Assessment & Plan (completed)
- [x] Repo analyzed
- [x] Scope reduced: only User Profile + Pet Image services first
- [x] No DB/Redis/caching day 1
- [x] Mono-repo + Maven multi-module decided
- [x] Package naming: `com.example.{servicename}`

### PHASE 1 – First Service Extraction (MVP)
**Focus:** user-profile

- [x] Create Spring Boot project (Spring Initializr: web + actuator)
- [x] application.yml (port 8081, graceful shutdown, actuator endpoints)
- [x] DTO: UserDto record
- [x] Service: UserService (WebClient → RandomUser.me)
- [x] Controller: GET /api/v1/users
- [x] Dockerfile
- [ ] Local run & verify
- [ ] Git branch & commit: `feature/extract-user-profile-service`

**Next action:** Finish & verify user-profile-service locally

### PHASE 1b – Second Service Extraction
**Focus:** pet-image

- Create project (same way, port 8082)
- Endpoint: GET /api/v1/pets?count=10
- Call https://dog.ceo/api/breeds/image/random/{count}
- Return List<String> (image URLs)
- Dockerfile
- Update docker-compose.local.yml to run both services
- Local verification

### PHASE 2 – AWS Foundation (free tier)
- Create ECR repositories (one per service)
- Build & push Docker images
- ECS Cluster (Fargate)
- Task definitions & services (1 task each, spot if available)
- Application Load Balancer (ALB)
  - Path rules:
    - `/api/v1/users/` → user-profile-service
    - `/api/v1/pets/` → pet-image-service
- Security groups, IAM task role (least privilege: logs + ecr pull)
- CloudWatch logs enabled
- Basic health check on /actuator/health

### PHASE 3 – Aggregation & Strangler Routing
- Create api-gateway-service (port 8080)
- Use WebClient to call both services
- Endpoint: GET /api/users-with-pet?results=10&nat=fi
- Merge: user[i] + { petImage: pet[i] }
- Update ALB to route `/api/users-with-pet/*` → gateway
- (Optional) Add Resilience4j circuit breaker + retries

### PHASE 4 – Observability & Production Touches
- Structured JSON logging (logback + logstash encoder)
- (Optional) Micrometer + CloudWatch metrics
- Graceful shutdown hooks tested
- Readiness/liveness probes in ECS

### PHASE 5 – Frontend Integration & Cut-over
- Point frontend to new ALB `/api/users-with-pet`
- Run A/B test (some traffic old monolith, some new)

### PHASE 6 – Nice-to-haves (future)
- Caching: Caffeine → Redis (ElastiCache free tier?)
- Persistence: DynamoDB or Aurora Serverless v2 (pause/resume)
- Rate limiting (Gateway)
- CI/CD: GitHub Actions → ECR → ECS update

## Progress Tracking
Update this file after each major step.

Example:
- 2026-01-xx: user-profile-service running locally ✅
- 2026-02-xx: pet-image-service extracted & dockerized ✅

Last updated: January 2026 (fill real dates)

Feel free to add sections like Risks, Decisions, Lessons Learned.