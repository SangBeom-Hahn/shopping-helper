package com.sketch2fashion.backend.domain.modelresult;

import com.sketch2fashion.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "search")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id")
    private Long id;

    @Column(name = "thumbnail_url", length = 255, nullable = true)
    private String thumbnailUrl;

    @Column(name = "web_search_url", length = 255, nullable = true)
    private String webSearchUrl;

    @Column(name = "host_page_url", length = 255, nullable = true)
    private String hostPageUrl;

    @Column(name = "site", length = 50, nullable = false)
    private String site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = false, foreignKey = @ForeignKey(name = "fk_search_result"))
    private ClothesResult clothes;

    public Search(String thumbnailUrl, String webSearchUrl, String hostPageUrl, String site, ClothesResult clothes) {
        this.thumbnailUrl = thumbnailUrl;
        this.webSearchUrl = webSearchUrl;
        this.hostPageUrl = hostPageUrl;
        this.site = site;
        this.clothes = clothes;
    }
}
