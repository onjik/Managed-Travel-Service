package click.porito.managed_travel.place.place_service.api.command.application;

import click.porito.managed_travel.place.domain.PlaceArticle;
import click.porito.managed_travel.place.domain.api.command.PlaceArticleApi;
import click.porito.managed_travel.place.domain.api.request.PlaceArticleUpsertCommand;
import click.porito.managed_travel.place.place_service.util.ValidatedArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceArticleApiImpl implements PlaceArticleApi {


    @Override
    @ValidatedArgs
    public PlaceArticle putPlaceArticle(PlaceArticleUpsertCommand command) {

        //여기서 부터 시작, 지금 전체적으로 API 구현체 만들고 있었음

        return null;
    }

    @Override
    public void deletePlaceArticle(String articleId) {

    }
}
