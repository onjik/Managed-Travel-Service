package click.porito.place_common.domain;

public enum PlaceType {
    TOURIST_ATTRACTION(new String[]{"tourist_attraction"}),
    RESTAURANT(new String[]{"restaurant"}),
    ACCOMMODATION(new String[]{"lodging"}),
    SHOPPING(new String[]{"shopping_mall", "store"}),
    TRANSPORTATION(new String[]{"airport", "bus_station", "bus_stop", "ferry_terminal", "heliport", "light_rail_station", "park_and_ride", "subway_station", "taxi_stand", "train_station", "transit_depot", "transit_station", "truck_stop"});
    private final String[] typeNames;

    PlaceType(String[] typeNames) {
        this.typeNames = typeNames;
    }

    public String[] getTypeNames() {
        return typeNames;
    }
}
