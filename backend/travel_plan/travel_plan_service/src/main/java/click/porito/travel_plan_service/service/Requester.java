package click.porito.travel_plan_service.service;

public record Requester(
        String id,
        String[] roles
) {
    public static Requester of(String id, String[] roles) {
        return new Requester(id, roles);
    }

    public static Requester of(String id, String role) {
        return new Requester(id, new String[]{role});
    }
}
