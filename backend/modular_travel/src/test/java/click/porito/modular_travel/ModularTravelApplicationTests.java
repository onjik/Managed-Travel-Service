package click.porito.modular_travel;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest
class ModularTravelApplicationTests {
	private final static String PLACE_PHOTO_URL = "https://places.googleapis.com/v1/{PHOTO_NAME}/media";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Gson gson;
	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		String replace = PLACE_PHOTO_URL.replace("{PHOTO_NAME}", "places/ChIJ94kl9BTnAGARgA4Z1NMnRdQ/photos/AcJnMuF-TJR8CT2ChgWvUxmNxnQgXed3R8JrULe5PNMBTd9DO0VvufutPQyJkcwLroi9DymaYgDjTVCpzoPC6x0vZelk1jIEHyfkPewcHcz1zdeUfqzRfBHVP-Jl9BULm8VTuSfOyK296XE0Og_1fkuwXFh7ukcatkwBneHI");

		var builder = new DefaultUriBuilderFactory(replace).builder();
		builder.queryParam("maxHeightPx", 1000);
		builder.queryParam("maxWidthPx", 1000);
		builder.queryParam("key", "AIzaSyAGZ5t8BF6W88LthcAstfI-_P2gtdrs1DQ");
		builder.queryParam("skipHttpRedirect", true);
		var uri = builder.build();

		String s = "https://places.googleapis.com/v1/places/ChIJ94kl9BTnAGARgA4Z1NMnRdQ/photos/AcJnMuE5oO1vvYFuy3GyMjHF25alKYjR6_yTPmhe0pL3mmWYXZn3nmWTVyqdcGE2-DrnTvMMmYpkZpNqgoCqrqh7E7GPXSxTqU30xtY1sEdwc4Qgk4Wd-P75rHaDa57OQLiJOwbJp-KXlrme4XDixDNoLLi-CsCjF4DFECiW/media?maxHeightPx=400&maxWidthPx=400&key=AIzaSyAGZ5t8BF6W88LthcAstfI-_P2gtdrs1DQ&skipHttpRedirect=true";

		String jsonResponse = restTemplate.getForObject(s, String.class);
		//photoUri 필드 꺼내오기
		Map map = gson.fromJson(jsonResponse, Map.class);
	}


}
