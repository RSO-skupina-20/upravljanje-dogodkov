package si.fri.rso.skupina20.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import si.fri.rso.skupina20.zrna.DogodekZrno;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

@Readiness
@ApplicationScoped
public class PridobiDogodkeHealthCheckBean implements HealthCheck {
    private static final Logger LOG = Logger.getLogger(PridobiDogodkeHealthCheckBean.class.getName());

    @Inject
    private DogodekZrno dogodekZrno;

    @Override
    public HealthCheckResponse call() {
        String description = "Preverjanje ali pridobivanje podatkov o dogodkih deluje";
        try {
            dogodekZrno.getDogodki();
        } catch (Exception e) {
            LOG.severe("Napaka pri preverjanju zdravja: " + e.getMessage());
            return HealthCheckResponse.named(PridobiDogodkeHealthCheckBean.class.getSimpleName())
                    .down()
                    .withData("description", description)
                    .withData("error", e.getMessage())
                    .build();
        }
        return HealthCheckResponse.named(PridobiDogodkeHealthCheckBean.class.getSimpleName())
                .up()
                .withData("description", description)
                .build();
    }
}
