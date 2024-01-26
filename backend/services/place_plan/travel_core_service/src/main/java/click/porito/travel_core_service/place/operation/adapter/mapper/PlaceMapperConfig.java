package click.porito.travel_core_service.place.operation.adapter.mapper;

import click.porito.common.util.Mapper;
import click.porito.place_common.domain.AuthorAttribution;
import click.porito.place_common.domain.PhotoReference;
import click.porito.place_common.domain.Place;
import click.porito.travel_core_service.place.operation.adapter.google_api.model.GooglePhotoReference;
import click.porito.travel_core_service.place.operation.adapter.google_api.model.GooglePlace;
import click.porito.travel_core_service.place.operation.adapter.persistence.entity.AuthorAttributionEntity;
import click.porito.travel_core_service.place.operation.adapter.persistence.entity.PhotoEntity;
import click.porito.travel_core_service.place.operation.adapter.persistence.entity.PlaceEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Configuration
public class PlaceMapperConfig {

    @Bean
    public Mapper<GooglePlace, Place> googlePlaceToPlaceViewMapper(Mapper<GooglePhotoReference, PhotoReference> photoMapper) {
        return new Mapper<GooglePlace, Place>() {
            @Override
            protected Place mapInternal(GooglePlace source) {
                String id = source.getId();
                String name = source.getDisplayName() != null ? source.getDisplayName().getText() : null;
                List<String> tags = source.getTypes();
                String address = source.getFormattedAddress();
                Double latitude = source.getLocation() != null ? source.getLocation().getLatitude() : null;
                Double longitude = source.getLocation() != null ? source.getLocation().getLongitude() : null;
                String summary = source.getEditorialSummary() != null ? source.getEditorialSummary().getText() : null;
                var photos = photoMapper.map(source.getPhotos());
                return Place.builder()
                        .id(id)
                        .name(name)
                        .tags(tags)
                        .address(address)
                        .latitude(latitude)
                        .longitude(longitude)
                        .summary(summary)
                        .photos(photos)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<PlaceEntity, Place> placeMapper(Mapper<PhotoEntity, PhotoReference> photoMapper) {
        return new Mapper<PlaceEntity, Place>() {
            @Override
            protected Place mapInternal(PlaceEntity source) {
                String id = source.getId();
                String name = source.getName();
                List<String> tags = source.getTags();
                String address = source.getAddress();
                Double latitude = source.getLocation() != null ? source.getLocation().getY() : null;
                Double longitude = source.getLocation() != null ? source.getLocation().getX() : null;
                String summary = source.getSummary();
                var photos = photoMapper.map(source.getPhotos());

                return Place.builder()
                        .id(id)
                        .name(name)
                        .tags(tags)
                        .address(address)
                        .latitude(latitude)
                        .longitude(longitude)
                        .summary(summary)
                        .photos(photos)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<Place, PlaceEntity> placeViewToPlaceMapper(Mapper<PhotoReference, PhotoEntity> photoMapper) {
        return new Mapper<Place, PlaceEntity>() {
            @Override
            protected PlaceEntity mapInternal(Place source) {
                String id = source.id();
                String name = source.name();
                List<String> tags = source.tags();
                String address = source.address();
                Double latitude = source.latitude();
                Double longitude = source.longitude();
                final GeoJsonPoint point;
                if (latitude != null && longitude != null) {
                    point = new GeoJsonPoint(longitude, latitude);
                } else {
                    point = null;
                }
                String summary = source.summary();
                var photos = photoMapper.map(source.photos());
                return PlaceEntity.builder()
                        .id(id)
                        .name(name)
                        .tags(tags)
                        .address(address)
                        .location(point)
                        .summary(summary)
                        .photos(photos)
                        .build();
            }
        };
    }

    @Bean
    public Mapper<PhotoEntity, PhotoReference> photoMapper(Mapper<AuthorAttributionEntity, AuthorAttribution> authorMapper) {
        return new Mapper<PhotoEntity, PhotoReference>() {
            @Override
            protected PhotoReference mapInternal(PhotoEntity source) {
                String photoId = source.getPhotoId();
                Integer heightPx = source.getHeightPx();
                Integer widthPx = source.getWidthPx();
                var authorAttributionViews = authorMapper.map(source.getAuthorAttributions());
                return new PhotoReference(photoId, heightPx, widthPx, authorAttributionViews);
            }
        };
    }

    @Bean
    public Mapper<PhotoReference, PhotoEntity> photoViewMapper(Mapper<AuthorAttribution, AuthorAttributionEntity> authorMapper) {
        return new Mapper<PhotoReference, PhotoEntity>() {
            @Override
            protected PhotoEntity mapInternal(PhotoReference source) {
                String photoId = source.photoId();
                Integer heightPx = source.heightPx();
                Integer widthPx = source.widthPx();
                var authorAttributions = authorMapper.map(source.authorAttributions());
                return new PhotoEntity(photoId, heightPx, widthPx, authorAttributions);
            }
        };
    }

    @Bean
    public Mapper<GooglePhotoReference, PhotoReference> googlePhotoMapper(Mapper<GooglePhotoReference.GoogleAuthorAttribution, AuthorAttribution> authorMapper) {
        return new Mapper<GooglePhotoReference, PhotoReference>() {
            @Override
            protected PhotoReference mapInternal(GooglePhotoReference source) {
                String photoId = source.getName();
                int widthPx = source.getWidthPx();
                int heightPx = source.getHeightPx();
                var authorAttributions = authorMapper.map(source.getAuthorAttributions());
                return new PhotoReference(photoId, heightPx, widthPx, authorAttributions);
            }
        };
    }

    @Bean
    public Mapper<GooglePhotoReference.GoogleAuthorAttribution, AuthorAttribution> googleauthorMapper(){
        return new Mapper<GooglePhotoReference.GoogleAuthorAttribution, AuthorAttribution>() {
            @Override
            protected AuthorAttribution mapInternal(GooglePhotoReference.GoogleAuthorAttribution source) {
                String displayName = source.getDisplayName();
                String uri = source.getUri();
                String photoUri = source.getPhotoUri();
                return new AuthorAttribution(displayName, uri, photoUri);
            }
        };
    }

    @Bean
    public Mapper<AuthorAttribution, AuthorAttributionEntity> authorViewMapper() {
        return new Mapper<AuthorAttribution, AuthorAttributionEntity>() {
            @Override
            protected AuthorAttributionEntity mapInternal(AuthorAttribution source) {
                String displayName = source.displayName();
                String uri = source.uri();
                String photoUri = source.photoUri();
                return new AuthorAttributionEntity(displayName, uri, photoUri);
            }
        };
    }

    @Bean
    public Mapper<AuthorAttributionEntity, AuthorAttribution> authorMapper() {
        return new Mapper<AuthorAttributionEntity, AuthorAttribution>() {
            @Override
            protected AuthorAttribution mapInternal(AuthorAttributionEntity source) {
                String displayName = source.getDisplayName();
                String uri = source.getUri();
                String photoUri = source.getPhotoUri();
                return new AuthorAttribution(displayName, uri, photoUri);
            }
        };
    }
}
