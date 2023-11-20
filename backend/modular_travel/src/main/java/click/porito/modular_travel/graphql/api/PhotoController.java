package click.porito.modular_travel.graphql.api;

import click.porito.modular_travel.place.PlaceService;
import click.porito.modular_travel.image.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
public class PhotoController {

    private final PlaceService placeService;
    @SchemaMapping(typeName = "Photo", field = "photoUri")
    public CompletableFuture<String> photoUri(Photo photo) {
        return CompletableFuture.supplyAsync(() -> {
            return placeService.getPhotoUri(photo.getName(), 1600, 900);
        });
    }
}
