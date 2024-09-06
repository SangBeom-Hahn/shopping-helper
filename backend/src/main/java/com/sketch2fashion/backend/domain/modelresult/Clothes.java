package com.sketch2fashion.backend.domain.modelresult;

import com.sketch2fashion.backend.domain.BaseEntity;
import com.sketch2fashion.backend.domain.message.Message;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sketch2fashion.backend.domain.modelresult.Status.WAIT;

@Getter
@Entity(name = "clothes_model_result")
@Table(name = "clothes_model_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clothes extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_result_clothes_message"), nullable = false)
    private Message message;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_message", length = 50, nullable = false)
    private String statusMessage;

    @Column(name = "store_file_path", length = 255, nullable = false)
    private String storeFilePath;

    @Column(name = "rating", length = 2, nullable = false)
    private Rating rating;

    @Column(name = "shared", nullable = false)
    private Boolean shared;

    public Clothes(Message message, String storeFilePath) {
        this.message = message;
        this.status = WAIT;
        this.statusMessage = WAIT.getMessage();
        this.storeFilePath = storeFilePath;
        this.rating = Rating.THIRD;
        this.shared = false;
    }
}
