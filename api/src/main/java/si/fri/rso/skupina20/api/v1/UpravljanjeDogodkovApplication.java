package si.fri.rso.skupina20.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;

@OpenAPIDefinition(info = @Info(
        title = "Upravljanje dogodkov API",
        version = "v1",
        description = "Upravljanje dogodkov API omogoča upravljanje z dogodki"),
        servers = {
        @Server(url = "http://172.212.44.19/upravljanje-dogodkov"),
        @Server(url = "http://localhost:8082")
    })
@SecurityScheme(
        securitySchemeName = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
@ApplicationPath("v1")
public class UpravljanjeDogodkovApplication extends javax.ws.rs.core.Application {
}