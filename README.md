# Template API - Spring Boot with Keycloak JWT Authentication

A comprehensive Java Spring Boot API template using Spring Boot 3.3.x with Java 17, Maven, MySQL, Keycloak JWT authentication (not local JWT implementation), and OpenAPI/Swagger documentation.

## Features

- **Security**: Keycloak JWT authentication via OAuth2 Resource Server (no local JWT implementation)
- **Database**: MySQL with JPA and Flyway migrations
- **Documentation**: OpenAPI/Swagger with JWT authentication support
- **Validation**: Jakarta Validation annotations
- **Exception Handling**: Global exception handler with standardized responses
- **Profiles**: Environment-specific configurations (dev, prod)
- **Code Quality**: Lombok for boilerplate reduction
- **Best Practices**: Layered architecture (Controller → Service → Repository)

## Prerequisites

- Java 17 or higher
- Maven 3.9+
- MySQL 8.0 or higher
- Keycloak server

## Project Structure

```
template-api/
├── src/
│   ├── main/
│   │   ├── java/com/euclidesroberto/template_api/
│   │   │   ├── TemplateApiApplication.java
│   │   │   ├── config/
│   │   │   │   ├── KeycloakConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── ExampleController.java
│   │   │   │   └── HealthController.java
│   │   │   ├── dto/
│   │   │   │   ├── ExampleDto.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── model/
│   │   │   │   └── Example.java
│   │   │   ├── repository/
│   │   │   │   └── ExampleRepository.java
│   │   │   ├── service/
│   │   │   │   └── ExampleService.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       └── ResourceNotFoundException.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/
│   │           └── migration/
│   │               └── V1__init_schema.sql
├── pom.xml
└── README.md
```

## Installation

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd template-api
   ```

2. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Set up MySQL database**
   - Install MySQL 8.0 or higher
   - Create a database named `template_api`
   - Update `application.yml` with your MySQL credentials

4. **Set up Keycloak** (see Keycloak Setup section below)

5. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8081`

## Configuration

### Application Configuration

Edit `src/main/resources/application.yml` to configure:

- **Database settings**: MySQL connection parameters
- **Keycloak settings**: Server URL, realm, client ID, and secret
- **Server port**: Default is 8080
- **SpringDoc**: OpenAPI/Swagger configuration

### Environment Variables

The following environment variables can be set:

- `SPRING_PROFILES_ACTIVE`: Active profile (dev, prod)
- `MYSQL_HOST`: MySQL server host
- `MYSQL_PORT`: MySQL server port (default: 3306)
- `MYSQL_DATABASE`: Database name
- `MYSQL_USER`: Database user
- `MYSQL_PASSWORD`: Database password
- `KEYCLOAK_SERVER_URL`: Keycloak server URL
- `KEYCLOAK_REALM`: Keycloak realm name
- `KEYCLOAK_CLIENT_ID`: Keycloak client ID
- `KEYCLOAK_CLIENT_SECRET`: Keycloak client secret

## Keycloak Setup

### 1. Start Keycloak

Download and start Keycloak locally:

```bash
# Download Keycloak
wget https://github.com/keycloak/keycloak/releases/download/24.0.5/keycloak-24.0.5.tar.gz
tar -xzf keycloak-24.0.5.tar.gz
cd keycloak-24.0.5/bin

# Start Keycloak
./kc.sh start-dev
```

Or use Docker if you prefer:

```bash
docker run -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:24.0.5 start-dev
```

### 2. Access Keycloak Admin Console

Navigate to `http://localhost:8080/admin`

Login with:
- Username: `admin`
- Password: `admin`

### 3. Create a Realm

1. Click on the dropdown in the top-left corner (next to "Master")
2. Click "Create Realm"
3. Enter realm name: `api-template`
4. Click "Create"

### 4. Create a Client

1. Navigate to "Clients" in the left menu
2. Click "Create Client"
3. Fill in the form:
   - **Client ID**: `template-api`
   - **Client Authentication**: Off (public client for Authorization Code Flow)
   - **Authentication Flow**: Standard Flow Enabled
   - **Valid Redirect URIs**: `http://localhost:3000/*`
   - **Web Origins**: `http://localhost:3000`
4. Click "Save"

### 5. Create Client Roles

1. Navigate to "Clients" → click on "template-api"
2. Go to "Roles" tab
3. Create roles: `admin`, `user`

### 6. Create Users

1. Navigate to "Users" in the left menu
2. Click "Add User"
3. Fill in user details:
   - **Username**: `testuser`
   - **Email**: `test@example.com`
   - **Email Verified**: On
   - **First Name**: Test
   - **Last Name**: User
4. Click "Create"
5. Go to the "Credentials" tab for the user
6. Set a password (e.g., `password123`)
7. Click "Set Password" and confirm

### 7. Assign Client Roles to User

1. Navigate to the user you created
2. Click on "Role Mappings" tab
3. Select "Client Roles" → "template-api"
4. Select the roles you want to assign (e.g., `admin`, `user`)
5. Click "Add selected"

### 8. Update Application Configuration

Update your `.env` file with the Keycloak configuration:

```env
KEYCLOAK_SERVER_URL=http://localhost:8080
KEYCLOAK_REALM=api-template
KEYCLOAK_CLIENT_ID=template-api
```

Note: Since we're using Authorization Code Flow with a public client, no client secret is needed.

## API Usage

### Access Swagger UI

Navigate to `http://localhost:8080/swagger-ui.html`

To test protected endpoints:
1. Click the "Authorize" button
2. Enter your JWT token (without "Bearer " prefix)
3. Click "Authorize"
4. Now you can test the protected endpoints

### Example API Endpoints

#### Public Endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health
```

#### Protected Endpoints (require JWT token)

```bash
# Get all examples
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/examples

# Get example by ID
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/examples/1

# Create example (requires admin role)
curl -X POST \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Example Name","description":"Example Description"}' \
  http://localhost:8080/api/examples

# Update example (requires admin role)
curl -X PUT \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Name","description":"Updated Description"}' \
  http://localhost:8080/api/examples/1

# Delete example (requires admin role)
curl -X DELETE \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/examples/1
```

## Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

## What to Change in This Template

1. **Package Name**: Change `com.euclidesroberto.template_api` to your package name
   - **Important**: Also update the logging configuration in `application.yml`, `application-dev.yml`, and `application-prod.yml` to use your new package name
2. **Application Name**: Update in `pom.xml` and `application.yml`
3. **CORS Configuration**: Update `cors.allowed-origins` in `application.yml` to match your frontend URL
4. **Database Schema**: Modify `V1__init_schema.sql` for your tables
5. **Models**: Create your own entity models in the `model` package
6. **DTOs**: Create DTOs for your API in the `dto` package
7. **Repositories**: Create repositories for your models
8. **Services**: Implement business logic in the `service` package
9. **Controllers**: Create controllers for your API endpoints
10. **Keycloak Configuration**: Update realm, client, and role names
11. **Security Rules**: Modify `SecurityConfig.java` for your authorization rules

## Troubleshooting

### Database Connection Issues

- Ensure MySQL is running and accessible
- Check connection parameters in `application.yml`
- Verify database exists and user has proper permissions

### Keycloak JWT Validation Errors

- Verify Keycloak server URL is correct
- Check realm name matches your Keycloak setup
- Ensure client ID is correct
- Verify JWT token is not expired

### Flyway Migration Issues

- Check that Flyway is enabled in `application.yml`
- Verify migration scripts are in `src/main/resources/db/migration`
- Use `spring.jpa.hibernate.ddl-auto=update` in dev profile for auto-schema creation

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server.html)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [Flyway Documentation](https://flywaydb.org/documentation/)

## License

This template is provided as-is for educational and development purposes.
