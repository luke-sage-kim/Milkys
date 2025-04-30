package org.milkys.domain.mediaFile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mediaFile_table")
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "med_id")
    private Long id;
    @Column(name = "med_originalFileName")
    private String originalFileName;  // 업로드한 원본 파일명
    @Column(name = "med_storedFilePath")
    private String storedFilePath;    // 서버에 저장된 경로
    @Column(name = "med_fileType")
    private String fileType;          // IMAGE, VIDEO, AUDIO
    @Column(name = "med_fileSize")
    private Long fileSize;            // 파일 크기 (byte)
    @Column(name = "med_domainType")
    private String domainType;        // Gallery, Recording 구분용
    @Column(name = "med_parentId")
    private Long parentId;            // 연결된 글 ID (GalleryId, RecordingId 등)
}