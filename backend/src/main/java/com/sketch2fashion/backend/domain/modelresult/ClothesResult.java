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
public class ClothesResult extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_result_clothes_message"), nullable = false)
    private Message message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "status_message", length = 50, nullable = false)
    private String statusMessage;

    @Column(name = "store_file_path", length = 255)
    private String storeFilePath;

    @Column(name = "rating", length = 2)
    private int rating;

    @Column(name = "review", length = 2000)
    private String review;

    @Column(name = "shared", nullable = false)
    private Boolean shared;

    public ClothesResult(Message message) {
        this.message = message;
        this.status = WAIT;
        this.statusMessage = WAIT.getMessage();
        this.shared = false;
    }

    public void changeRate(final int rating) {
        this.rating = rating;
    }

    public void changeReview(final String review) {
        this.review = review;
    }

    public void changeShared(final Boolean shared) {
        this.shared = shared;
    }
}
