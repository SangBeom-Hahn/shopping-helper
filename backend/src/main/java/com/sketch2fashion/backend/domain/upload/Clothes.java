package com.sketch2fashion.backend.domain.upload;

import com.sketch2fashion.backend.domain.BaseEntity;
import com.sketch2fashion.backend.domain.message.Message;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sketch2fashion.backend.domain.upload.Status.SUCCESS;

@Getter
@Entity
@Table(name = "clothes_upload_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clothes extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_upload_clothes_message"), nullable = false)
    private Message message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "status_message", length = 50, nullable = false)
    private String statusMessage;

    @Column(name = "upload_file_name", length = 50, nullable = false)
    private String uploadFileName;

    @Column(name = "store_file_Path", length = 255, nullable = false)
    private String storeFilePath;

    public Clothes(Message message, String uploadFileName, String storeFilePath) {
        this.message = message;
        this.status = SUCCESS;
        this.statusMessage = SUCCESS.getMessage();
        this.uploadFileName = uploadFileName;
        this.storeFilePath = storeFilePath;
    }

    public Long getMessageId() {
        return message.getId();
    }
}
