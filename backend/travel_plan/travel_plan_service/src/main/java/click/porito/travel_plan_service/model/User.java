package click.porito.travel_plan_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "account_principal")
public class User {
    @Id
    private Long id;
}
