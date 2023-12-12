package click.porito.place_service.dto;

import lombok.Getter;

@Getter
public enum PlaceType {
    // Services
    barber_shop(Category.SERVICES),
    beauty_salon(Category.SERVICES),
    cemetery(Category.SERVICES),
    child_care_agency(Category.SERVICES),
    consultant(Category.SERVICES),
    courier_service(Category.SERVICES),
    electrician(Category.SERVICES),
    florist(Category.SERVICES),
    funeral_home(Category.SERVICES),
    hair_care(Category.SERVICES),
    hair_salon(Category.SERVICES),
    insurance_agency(Category.SERVICES),
    laundry(Category.SERVICES),
    lawyer(Category.SERVICES),
    locksmith(Category.SERVICES),
    moving_company(Category.SERVICES),
    painter(Category.SERVICES),
    plumber(Category.SERVICES),
    real_estate_agency(Category.SERVICES),
    roofing_contractor(Category.SERVICES),
    storage(Category.SERVICES),
    tailor(Category.SERVICES),
    telecommunications_service_provider(Category.SERVICES),
    travel_agency(Category.SERVICES),
    veterinary_care(Category.SERVICES),

    // Shopping
    auto_parts_store(Category.SHOPPING),
    bicycle_store(Category.SHOPPING),
    book_store(Category.SHOPPING),
    cell_phone_store(Category.SHOPPING),
    clothing_store(Category.SHOPPING),
    convenience_store(Category.SHOPPING),
    department_store(Category.SHOPPING),
    discount_store(Category.SHOPPING),
    electronics_store(Category.SHOPPING),
    furniture_store(Category.SHOPPING),
    gift_shop(Category.SHOPPING),
    grocery_store(Category.SHOPPING),
    hardware_store(Category.SHOPPING),
    home_goods_store(Category.SHOPPING),
    home_improvement_store(Category.SHOPPING),
    jewelry_store(Category.SHOPPING),
    liquor_store(Category.SHOPPING),
    market(Category.SHOPPING),
    pet_store(Category.SHOPPING),
    shoe_store(Category.SHOPPING),
    shopping_mall(Category.SHOPPING),
    sporting_goods_store(Category.SHOPPING),
    store(Category.SHOPPING),
    supermarket(Category.SHOPPING),
    wholesaler(Category.SHOPPING),

    // Sports
    athletic_field(Category.SPORTS),
    fitness_center(Category.SPORTS),
    golf_course(Category.SPORTS),
    gym(Category.SPORTS),
    playground(Category.SPORTS),
    ski_resort(Category.SPORTS),
    sports_club(Category.SPORTS),
    sports_complex(Category.SPORTS),
    stadium(Category.SPORTS),
    swimming_pool(Category.SPORTS),

    // Transportation
    airport(Category.TRANSPORTATION),
    bus_station(Category.TRANSPORTATION),
    bus_stop(Category.TRANSPORTATION),
    ferry_terminal(Category.TRANSPORTATION),
    heliport(Category.TRANSPORTATION),
    light_rail_station(Category.TRANSPORTATION),
    park_and_ride(Category.TRANSPORTATION),
    subway_station(Category.TRANSPORTATION),
    taxi_stand(Category.TRANSPORTATION),
    train_station(Category.TRANSPORTATION),
    transit_depot(Category.TRANSPORTATION),
    transit_station(Category.TRANSPORTATION),
    truck_stop(Category.TRANSPORTATION),

    // Automotive
    car_dealer(Category.AUTOMOTIVE),
    car_rental(Category.AUTOMOTIVE),
    car_repair(Category.AUTOMOTIVE),
    car_wash(Category.AUTOMOTIVE),
    electric_vehicle_charging_station(Category.AUTOMOTIVE),
    gas_station(Category.AUTOMOTIVE),
    parking(Category.AUTOMOTIVE),
    rest_stop(Category.AUTOMOTIVE),

    // Business
    farm(Category.BUSINESS),

    // Culture
    art_gallery(Category.CULTURE),
    museum(Category.CULTURE),
    performing_arts_theater(Category.CULTURE),

    // Education
    library(Category.EDUCATION),
    preschool(Category.EDUCATION),
    primary_school(Category.EDUCATION),
    school(Category.EDUCATION),
    secondary_school(Category.EDUCATION),
    university(Category.EDUCATION),

    // Entertainment and Recreation
    amusement_center(Category.ENTERTAINMENT_AND_RECREATION),
    amusement_park(Category.ENTERTAINMENT_AND_RECREATION),
    aquarium(Category.ENTERTAINMENT_AND_RECREATION),
    banquet_hall(Category.ENTERTAINMENT_AND_RECREATION),
    bowling_alley(Category.ENTERTAINMENT_AND_RECREATION),
    casino(Category.ENTERTAINMENT_AND_RECREATION),
    community_center(Category.ENTERTAINMENT_AND_RECREATION),
    convention_center(Category.ENTERTAINMENT_AND_RECREATION),
    cultural_center(Category.ENTERTAINMENT_AND_RECREATION),
    dog_park(Category.ENTERTAINMENT_AND_RECREATION),
    event_venue(Category.ENTERTAINMENT_AND_RECREATION),
    hiking_area(Category.ENTERTAINMENT_AND_RECREATION),
    historical_landmark(Category.ENTERTAINMENT_AND_RECREATION),
    marina(Category.ENTERTAINMENT_AND_RECREATION),
    movie_rental(Category.ENTERTAINMENT_AND_RECREATION),
    movie_theater(Category.ENTERTAINMENT_AND_RECREATION),
    national_park(Category.ENTERTAINMENT_AND_RECREATION),
    night_club(Category.ENTERTAINMENT_AND_RECREATION),
    park(Category.ENTERTAINMENT_AND_RECREATION),
    tourist_attraction(Category.ENTERTAINMENT_AND_RECREATION),
    visitor_center(Category.ENTERTAINMENT_AND_RECREATION),
    wedding_venue(Category.ENTERTAINMENT_AND_RECREATION),
    zoo(Category.ENTERTAINMENT_AND_RECREATION),

    // Finance
    accounting(Category.FINANCE),
    atm(Category.FINANCE),
    bank(Category.FINANCE),

    // Food and Drink
    american_restaurant(Category.FOOD_AND_DRINK),
    bakery(Category.FOOD_AND_DRINK),
    bar(Category.FOOD_AND_DRINK),
    barbecue_restaurant(Category.FOOD_AND_DRINK),
    brazilian_restaurant(Category.FOOD_AND_DRINK),
    breakfast_restaurant(Category.FOOD_AND_DRINK),
    brunch_restaurant(Category.FOOD_AND_DRINK),
    cafe(Category.FOOD_AND_DRINK),
    chinese_restaurant(Category.FOOD_AND_DRINK),
    coffee_shop(Category.FOOD_AND_DRINK),
    fast_food_restaurant(Category.FOOD_AND_DRINK),
    french_restaurant(Category.FOOD_AND_DRINK),
    greek_restaurant(Category.FOOD_AND_DRINK),
    hamburger_restaurant(Category.FOOD_AND_DRINK),
    ice_cream_shop(Category.FOOD_AND_DRINK),
    indian_restaurant(Category.FOOD_AND_DRINK),
    indonesian_restaurant(Category.FOOD_AND_DRINK),
    italian_restaurant(Category.FOOD_AND_DRINK),
    japanese_restaurant(Category.FOOD_AND_DRINK),
    korean_restaurant(Category.FOOD_AND_DRINK),
    lebanese_restaurant(Category.FOOD_AND_DRINK),
    meal_delivery(Category.FOOD_AND_DRINK),
    meal_takeaway(Category.FOOD_AND_DRINK),
    mediterranean_restaurant(Category.FOOD_AND_DRINK),
    mexican_restaurant(Category.FOOD_AND_DRINK),
    middle_eastern_restaurant(Category.FOOD_AND_DRINK),
    pizza_restaurant(Category.FOOD_AND_DRINK),
    ramen_restaurant(Category.FOOD_AND_DRINK),
    restaurant(Category.FOOD_AND_DRINK),
    sandwich_shop(Category.FOOD_AND_DRINK),
    seafood_restaurant(Category.FOOD_AND_DRINK),
    spanish_restaurant(Category.FOOD_AND_DRINK),
    steak_house(Category.FOOD_AND_DRINK),
    sushi_restaurant(Category.FOOD_AND_DRINK),
    thai_restaurant(Category.FOOD_AND_DRINK),
    turkish_restaurant(Category.FOOD_AND_DRINK),
    vegan_restaurant(Category.FOOD_AND_DRINK),
    vegetarian_restaurant(Category.FOOD_AND_DRINK),
    vietnamese_restaurant(Category.FOOD_AND_DRINK),

    // Geographical Area
    administrative_area_level_1(Category.GEOGRAPHICAL_AREA),
    administrative_area_level_2(Category.GEOGRAPHICAL_AREA),
    country(Category.GEOGRAPHICAL_AREA),
    locality(Category.GEOGRAPHICAL_AREA),
    postal_code(Category.GEOGRAPHICAL_AREA),
    school_district(Category.GEOGRAPHICAL_AREA),

    // Government
    city_hall(Category.GOVERNMENT),
    courthouse(Category.GOVERNMENT),
    embassy(Category.GOVERNMENT),
    fire_station(Category.GOVERNMENT),
    local_government_office(Category.GOVERNMENT),
    police(Category.GOVERNMENT),
    post_office(Category.GOVERNMENT),

    // Health and Wellness
    dental_clinic(Category.HEALTH_AND_WELLNESS),
    dentist(Category.HEALTH_AND_WELLNESS),
    doctor(Category.HEALTH_AND_WELLNESS),
    drugstore(Category.HEALTH_AND_WELLNESS),
    hospital(Category.HEALTH_AND_WELLNESS),
    medical_lab(Category.HEALTH_AND_WELLNESS),
    pharmacy(Category.HEALTH_AND_WELLNESS),
    physiotherapist(Category.HEALTH_AND_WELLNESS),
    spa(Category.HEALTH_AND_WELLNESS),

    // Lodging
    bed_and_breakfast(Category.LODGING),
    campground(Category.LODGING),
    camping_cabin(Category.LODGING),
    cottage(Category.LODGING),
    extended_stay_hotel(Category.LODGING),
    farmstay(Category.LODGING),
    guest_house(Category.LODGING),
    hostel(Category.LODGING),
    hotel(Category.LODGING),
    lodging(Category.LODGING),
    motel(Category.LODGING),
    private_guest_room(Category.LODGING),
    resort_hotel(Category.LODGING),
    rv_park(Category.LODGING),

    // Places of Worship
    church(Category.PLACES_OF_WORSHIP),
    hindu_temple(Category.PLACES_OF_WORSHIP),
    mosque(Category.PLACES_OF_WORSHIP),
    synagogue(Category.PLACES_OF_WORSHIP);

    private final Category category;

    PlaceType(Category category) {
        this.category = category;
    }

    public enum Category {
        SERVICES,
        SHOPPING,
        SPORTS,
        TRANSPORTATION,
        AUTOMOTIVE,
        BUSINESS,
        CULTURE,
        EDUCATION,
        ENTERTAINMENT_AND_RECREATION,
        FINANCE,
        FOOD_AND_DRINK,
        GEOGRAPHICAL_AREA,
        GOVERNMENT,
        HEALTH_AND_WELLNESS,
        LODGING,
        PLACES_OF_WORSHIP
    }
}
