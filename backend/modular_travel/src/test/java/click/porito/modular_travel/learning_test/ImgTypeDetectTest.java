package click.porito.modular_travel.learning_test;

import jakarta.activation.MimetypesFileTypeMap;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ImgTypeDetectTest {

    @Test
    void test(){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        String contentType = mimetypesFileTypeMap.getContentType("test.gif");
        System.out.println(contentType);
    }
}
