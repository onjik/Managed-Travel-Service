package click.porito.modular_travel.place;

public interface PlaceType {
    enum Services {
        barber_shop,
        beauty_salon,
        cemetery,
        child_care_agency,
        consultant,
        courier_service,
        electrician,
        florist,
        funeral_home,
        hair_care,
        hair_salon,
        insurance_agency,
        laundry,
        lawyer,
        locksmith,
        moving_company,
        painter,
        plumber,
        real_estate_agency,
        roofing_contractor,
        storage,
        tailor,
        telecommunications_service_provider,
        travel_agency,
        veterinary_care
    }

    enum Shopping {
        auto_parts_store,
        bicycle_store,
        book_store,
        cell_phone_store,
        clothing_store,
        convenience_store,
        department_store,
        discount_store,
        electronics_store,
        furniture_store,
        gift_shop,
        grocery_store,
        hardware_store,
        home_goods_store,
        home_improvement_store,
        jewelry_store,
        liquor_store,
        market,
        pet_store,
        shoe_store,
        shopping_mall,
        sporting_goods_store,
        store,
        supermarket,
        wholesaler
    }

    enum Sports {
        athletic_field,
        fitness_center,
        golf_course,
        gym,
        playground,
        ski_resort,
        sports_club,
        sports_complex,
        stadium,
        swimming_pool
    }

    enum Transportation {
        airport,
        bus_station,
        bus_stop,
        ferry_terminal,
        heliport,
        light_rail_station,
        park_and_ride,
        subway_station,
        taxi_stand,
        train_station,
        transit_depot,
        transit_station,
        truck_stop
    }

    enum Automotive {
        car_dealer,
        car_rental,
        car_repair,
        car_wash,
        electric_vehicle_charging_station,
        gas_station,
        parking,
        rest_stop
    }

    enum Business {
        farm
    }

    enum Culture {
        art_gallery,
        museum,
        performing_arts_theater
    }

    enum Education {
        library,
        preschool,
        primary_school,
        school,
        secondary_school,
        university
    }

    enum EntertainmentAndRecreation {
        amusement_center,
        amusement_park,
        aquarium,
        banquet_hall,
        bowling_alley,
        casino,
        community_center,
        convention_center,
        cultural_center,
        dog_park,
        event_venue,
        hiking_area,
        historical_landmark,
        marina,
        movie_rental,
        movie_theater,
        national_park,
        night_club,
        park,
        tourist_attraction,
        visitor_center,
        wedding_venue,
        zoo
    }

    enum Finance {
        accounting,
        atm,
        bank
    }

    enum FoodAndDrink {
        american_restaurant,
        bakery,
        bar,
        barbecue_restaurant,
        brazilian_restaurant,
        breakfast_restaurant,
        brunch_restaurant,
        cafe,
        chinese_restaurant,
        coffee_shop,
        fast_food_restaurant,
        french_restaurant,
        greek_restaurant,
        hamburger_restaurant,
        ice_cream_shop,
        indian_restaurant,
        indonesian_restaurant,
        italian_restaurant,
        japanese_restaurant,
        korean_restaurant,
        lebanese_restaurant,
        meal_delivery,
        meal_takeaway,
        mediterranean_restaurant,
        mexican_restaurant,
        middle_eastern_restaurant,
        pizza_restaurant,
        ramen_restaurant,
        restaurant,
        sandwich_shop,
        seafood_restaurant,
        spanish_restaurant,
        steak_house,
        sushi_restaurant,
        thai_restaurant,
        turkish_restaurant,
        vegan_restaurant,
        vegetarian_restaurant,
        vietnamese_restaurant
    }

    enum GeographicalArea {
        administrative_area_level_1,
        administrative_area_level_2,
        country,
        locality,
        postal_code,
        school_district
    }

    enum Government {
        city_hall,
        courthouse,
        embassy,
        fire_station,
        local_government_office,
        police,
        post_office
    }

    enum HealthAndWellness {
        dental_clinic,
        dentist,
        doctor,
        drugstore,
        hospital,
        medical_lab,
        pharmacy,
        physiotherapist,
        spa
    }

    enum Lodging {
        bed_and_breakfast,
        campground,
        camping_cabin,
        cottage,
        extended_stay_hotel,
        farmstay,
        guest_house,
        hostel,
        hotel,
        lodging,
        motel,
        private_guest_room,
        resort_hotel,
        rv_park
    }

    enum PlacesOfWorship {
        church,
        hindu_temple,
        mosque,
        synagogue
    }
}