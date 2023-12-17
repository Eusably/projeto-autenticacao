package config;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/api")
@DeclareRoles({"administrador", "suporte"})
@LoginConfig(authMethod = "MP-JWT")
public class ApplicationConfig extends Application {

}
