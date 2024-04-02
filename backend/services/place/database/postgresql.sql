CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    CONSTRAINT category_name_unique UNIQUE (category_name)
);
CREATE INDEX category_name_index ON category (category_name);

CREATE TABLE user_principal (
    user_id BIGINT PRIMARY KEY,
    deleted_at TIMESTAMP default NULL,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    roles TEXT[] default ARRAY[]::TEXT[] NOT NULL,
    CONSTRAINT user_id_unique UNIQUE (user_id)
);
CREATE INDEX user_principal_deleted_at_index ON user_principal(deleted_at);
CREATE INDEX user_principal_updated_at_index ON user_principal(updated_at);

CREATE TABLE place (
    place_id BIGSERIAL PRIMARY KEY ,
    name TEXT NOT NULL ,
    keywords TEXT[] default ARRAY[]::TEXT[] NOT NULL,
    address TEXT default NULL ,
    postal_code TEXT default NULL ,
    phone_number TEXT default NULL ,
    website TEXT default NULL ,
    summary TEXT default NULL ,
    location GEOMETRY(POINT, 4326) NOT NULL ,
    boundary GEOMETRY(POLYGON, 4326) default NULL ,
    created_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL ,
    google_place_id TEXT default NULL,
    is_public BOOLEAN default TRUE NOT NULL,
    version BIGINT default 0 NOT NULL,
    CONSTRAINT google_place_id_unique UNIQUE (google_place_id),
    CONSTRAINT location_boundary_check CHECK (boundary IS NULL OR ST_Contains(boundary, location))
);
CREATE INDEX place_location_index ON place USING GIST (location);
CREATE INDEX place_boundary_index ON place USING GIST (boundary);

CREATE TABLE place_category (
    place_id BIGINT NOT NULL ,
    category_id INT NOT NULL ,
    FOREIGN KEY (place_id) REFERENCES place(place_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id),
    PRIMARY KEY (place_id, category_id)
);
CREATE INDEX place_category_place_id_index ON place_category (place_id);
CREATE INDEX place_category_category_id_index ON place_category (category_id);

CREATE TABLE place_article (
    place_article_id BIGSERIAL PRIMARY KEY ,
    title VARCHAR(100) NOT NULL ,
    content TEXT NOT NULL ,
    user_id BIGINT NOT NULL ,
    place_id BIGINT NOT NULL,
    created_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    is_public BOOLEAN default TRUE NOT NULL,
    is_temp BOOLEAN default FALSE NOT NULL,
    version BIGINT default 0 NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_principal(user_id),
    FOREIGN KEY (place_id) REFERENCES place(place_id)
);
CREATE INDEX place_article_user_id_index ON place_article (user_id);
CREATE INDEX place_article_place_id_index ON place_article (place_id);

CREATE TABLE review (
    review_id BIGSERIAL PRIMARY KEY,
    rate SMALLINT NOT NULL , -- 1 - 10
    content TEXT default NULL ,
    created_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL ,
    place_id BIGINT NOT NULL ,
    user_id BIGINT NOT NULL ,
    version BIGINT default 0 NOT NULL,
    FOREIGN KEY (place_id) REFERENCES place(place_id),
    FOREIGN KEY (user_id) REFERENCES user_principal(user_id),
    CONSTRAINT rate_check CHECK (rate >= 1 AND rate <= 10),
    CONSTRAINT place_user_unique UNIQUE (place_id, user_id)
);
CREATE INDEX review_place_id_index ON review (place_id);
CREATE INDEX review_user_id_index ON review (user_id);
CREATE INDEX review_updated_at_index ON review (updated_at);

CREATE TABLE operation_time (
    operation_time_id BIGSERIAL PRIMARY KEY ,
    start_date DATE default NULL,
    end_date DATE default NULL,
    place_id BIGINT NOT NULL,
    version BIGINT default 0 NOT NULL,
    FOREIGN KEY (place_id) REFERENCES place(place_id)
);
CREATE INDEX operation_time_place_id_index ON operation_time (place_id);
CREATE INDEX operation_time_start_date_index ON operation_time (start_date);
CREATE INDEX operation_time_end_date_index ON operation_time (end_date);

CREATE TABLE day_operation_time (
    operation_time_id BIGSERIAL NOT NULL,
    day_of_week SMALLINT NOT NULL , -- 0 - 6 : 월 - 일, Java WeekOfDays ordinal
    start_time TIME NOT NULL ,
    end_time TIME NOT NULL ,
    next_day_linked BOOLEAN NOT NULL default FALSE,
    version BIGINT default 0 NOT NULL,
    FOREIGN KEY (operation_time_id) REFERENCES operation_time(operation_time_id),
    CONSTRAINT day_of_week_check CHECK (day_of_week >= 0 AND day_of_week <= 6),
    CONSTRAINT start_time_end_time_check CHECK (start_time < end_time)
);
CREATE INDEX day_operation_time_operation_time_id_index ON day_operation_time (operation_time_id);


CREATE TABLE content_type (
    content_type_id SERIAL PRIMARY KEY ,
    content_type_name VARCHAR(200) NOT NULL ,
    CONSTRAINT content_type_name_unique UNIQUE (content_type_name)
);
CREATE INDEX content_type_name_index ON content_type (content_type_name);

CREATE TABLE place_media (
    media_id UUID PRIMARY KEY DEFAULT gen_random_uuid() ,
    width_px INTEGER default NULL ,
    height_px INTEGER default NULL ,
    source_ref JSON NOT NULL ,
    created_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL ,
    content_type_id INT NOT NULL ,
    place_id BIGINT NOT NULL ,
    user_id BIGINT NOT NULL ,
    FOREIGN KEY (content_type_id) REFERENCES content_type(content_type_id),
    FOREIGN KEY (place_id) REFERENCES place(place_id),
    FOREIGN KEY (user_id) REFERENCES user_principal(user_id)
);
CREATE INDEX place_media_place_id_index ON place_media (place_id);
CREATE INDEX place_media_user_id_index ON place_media (user_id);
CREATE INDEX place_media_created_at_index ON place_media (created_at);

CREATE TABLE review_place_media (
    media_id UUID NOT NULL ,
    review_id BIGINT NOT NULL ,
    FOREIGN KEY (media_id) REFERENCES place_media(media_id),
    FOREIGN KEY (review_id) REFERENCES review(review_id),
    PRIMARY KEY (media_id, review_id)
);
CREATE INDEX review_place_media_media_id_index ON review_place_media (media_id);
CREATE INDEX review_place_media_review_id_index ON review_place_media (review_id);


INSERT INTO category (category_name) VALUES ('TOURIST_ATTRACTION'), ('RESTAURANT'), ('ACCOMMODATION'), ('SHOPPING'), ('TRANSPORTATION');
INSERT INTO content_type (content_type_name) VALUES ('image/jpeg'), ('image/png'), ('image/gif'), ('video/mp4'), ('video/webm'), ('video/quicktime');
