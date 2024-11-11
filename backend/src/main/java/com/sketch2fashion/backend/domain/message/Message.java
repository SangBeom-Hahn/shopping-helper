package com.sketch2fashion.backend.domain.message;

import com.sketch2fashion.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type", nullable = false)
    private ObjectType objectType;

    @Column(name = "store_file_path", length = 255, nullable = false)
    private String storeFilePath;

    @Column(name = "refine", nullable = false)
    private Boolean refine;

    public Message(ObjectType objectType, String storeFilePath, Boolean refine) {
        this.objectType = objectType;
        this.storeFilePath = storeFilePath;
        this.refine = refine;
    }
}
