package adria.mayo.proyectopersonal.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Value("${app.database.url}")
    private String dbUrl;

    public String getDatabaseConnection() {
        return "Conectando a: " + dbUrl;
    }
}

