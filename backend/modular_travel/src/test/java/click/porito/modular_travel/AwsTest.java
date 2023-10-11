package click.porito.modular_travel;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.s3.S3Client;

@ActiveProfiles("test")
@SpringBootTest
public class AwsTest {
    @Autowired
    S3Client s3Client;
    @Autowired
    private S3Template s3Template;

    @Test
    @DisplayName("s3Client 주입 테스트")
    void s3ClientInjection(){
        Assertions.assertNotNull(s3Client);
    }

    @Test
    @DisplayName("s3Template 주입 테스트")
    void s3TemplateInjection(){
        Assertions.assertNotNull(s3Template);
    }
}
