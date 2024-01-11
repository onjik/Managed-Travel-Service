package click.porito.travel_core.place.mapper;

import click.porito.travel_core.Mapper;
import click.porito.travel_core.place.cache.model.AuthorAttribution;
import click.porito.travel_core.place.cache.model.PhotoReference;
import click.porito.travel_core.place.cache.model.Place;
import click.porito.travel_core.place.dao.google_api.model.GooglePhotoReference;
import click.porito.travel_core.place.dao.google_api.model.GooglePlace;
import click.porito.travel_core.place.dto.AuthorAttributionView;
import click.porito.travel_core.place.dto.PhotoReferenceView;
import click.porito.travel_core.place.dto.PlaceView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Configuration
public class PlaceMapperConfig {

    @Bean
    public Mapper<GooglePlace, PlaceView> googlePlaceToPlaceViewMapper(Mapper<GooglePhotoReference, PhotoReferenceView> photoMapper) {
        return new Mapper<GooglePlace, PlaceView>() {
            @Override
            protected PlaceView mapInternal(GooglePlace source) {
                String id = source.getId();
                String name = source.getDisplayName() != null ? source.getDisplayName().getText() : null;
                List<String> tags = source.getTypes();
                String address = source.getFormattedAddress();
                Double latitude = source.getLocation() != null ? source.getLocation().getLatitude() : null;
                Double longitude = source.getLocation() != null ? source.getLocation().getLongitude() : null;
                String summary = source.getEditorialSummary() != null ? source.getEditorialSummary().getText() : null;
                var photos = photoMapper.map(source.getPhotos());
                return PlaceView.builder()
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
    public Mapper<Place, PlaceView> placeMapper(Mapper<PhotoReference, PhotoReferenceView> photoMapper) {
        return new Mapper<Place, PlaceView>() {
            @Override
            protected PlaceView mapInternal(Place source) {
                String id = source.getId();
                String name = source.getName();
                List<String> tags = source.getTags();
                String address = source.getAddress();
                Double latitude = source.getLocation() != null ? source.getLocation().getY() : null;
                Double longitude = source.getLocation() != null ? source.getLocation().getX() : null;
                String summary = source.getSummary();
                var photos = photoMapper.map(source.getPhotos());

                return PlaceView.builder()
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
    public Mapper<PlaceView,Place> placeViewToPlaceMapper(Mapper<PhotoReferenceView, PhotoReference> photoMapper) {
        return new Mapper<PlaceView,Place>() {
            @Override
            protected Place mapInternal(PlaceView source) {
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
                return Place.builder()
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
    public Mapper<PhotoReference,PhotoReferenceView> photoMapper(Mapper<AuthorAttribution, AuthorAttributionView> authorMapper) {
        return new Mapper<PhotoReference,PhotoReferenceView>() {
            @Override
            protected PhotoReferenceView mapInternal(PhotoReference source) {
                String photoId = source.getPhotoId();
                Integer heightPx = source.getHeightPx();
                Integer widthPx = source.getWidthPx();
                var authorAttributionViews = authorMapper.map(source.getAuthorAttributions());
                return new PhotoReferenceView(photoId, heightPx, widthPx, authorAttributionViews);
            }
        };
    }

    @Bean
    public Mapper<PhotoReferenceView, PhotoReference> photoViewMapper(Mapper<AuthorAttributionView,AuthorAttribution> authorMapper) {
        return new Mapper<PhotoReferenceView, PhotoReference>() {
            @Override
            protected PhotoReference mapInternal(PhotoReferenceView source) {
                String photoId = source.photoId();
                Integer heightPx = source.heightPx();
                Integer widthPx = source.widthPx();
                var authorAttributions = authorMapper.map(source.authorAttributionViews());
                return new PhotoReference(photoId, heightPx, widthPx, authorAttributions);
            }
        };
    }

    @Bean
    public Mapper<GooglePhotoReference, PhotoReferenceView> googlePhotoMapper(Mapper<GooglePhotoReference.GoogleAuthorAttribution, AuthorAttributionView> authorMapper) {
        return new Mapper<GooglePhotoReference, PhotoReferenceView>() {
            @Override
            protected PhotoReferenceView mapInternal(GooglePhotoReference source) {
                String photoId = source.getName();
                int widthPx = source.getWidthPx();
                int heightPx = source.getHeightPx();
                var authorAttributions = authorMapper.map(source.getAuthorAttributions());
                return new PhotoReferenceView(photoId, heightPx, widthPx, authorAttributions);
            }
        };
    }

    @Bean
    public Mapper<GooglePhotoReference.GoogleAuthorAttribution, AuthorAttributionView> googleauthorMapper(){
        return new Mapper<GooglePhotoReference.GoogleAuthorAttribution, AuthorAttributionView>() {
            @Override
            protected AuthorAttributionView mapInternal(GooglePhotoReference.GoogleAuthorAttribution source) {
                String displayName = source.getDisplayName();
                String uri = source.getUri();
                String photoUri = source.getPhotoUri();
                return new AuthorAttributionView(displayName, uri, photoUri);
            }
        };
    }

    @Bean
    public Mapper<AuthorAttributionView,AuthorAttribution> authorViewMapper() {
        return new Mapper<AuthorAttributionView,AuthorAttribution>() {
            @Override
            protected AuthorAttribution mapInternal(AuthorAttributionView source) {
                String displayName = source.displayName();
                String uri = source.uri();
                String photoUri = source.photoUri();
                return new AuthorAttribution(displayName, uri, photoUri);
            }
        };
    }

    @Bean
    public Mapper<AuthorAttribution, AuthorAttributionView> authorMapper() {
        return new Mapper<AuthorAttribution, AuthorAttributionView>() {
            @Override
            protected AuthorAttributionView mapInternal(AuthorAttribution source) {
                String displayName = source.getDisplayName();
                String uri = source.getUri();
                String photoUri = source.getPhotoUri();
                return new AuthorAttributionView(displayName, uri, photoUri);
            }
        };
    }
}
