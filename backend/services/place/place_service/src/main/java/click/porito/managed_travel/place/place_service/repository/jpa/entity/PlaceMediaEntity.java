package click.porito.managed_travel.place.place_service.repository.jpa.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.id.uuid.UuidGenerator;

import java.util.Map;
import java.util.UUID;

@Table(name = "place_media")
@Entity
@Getter
@Setter
public class PlaceMediaEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            type = UuidGenerator.class
    )
    @Column(name = "media_id")
    private UUID mediaId;

    @Column(name = "width_px")
    private Integer widthPx;

    @Column(name = "height_px")
    private Integer heightPx;

    @Column(name = "source_ref")
    @Type(JsonType.class)
    private Map<String,String> sourceRef;

    @Column(name = "created_at")
    private Long createdAt;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "content_type_id")
    private ContentTypeEntity contentTypeEntity;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private OfficialPlaceEntity placeEntity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountSnapshotEntity accountSnapshotEntity;

}
