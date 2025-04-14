// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.model;

import java.sql.Timestamp;
import com.amazonaws.services.s3.AmazonS3URI;
import lombok.Data;

/**
 * 設備データ取込管理タスク情報クラス
 * 設備データ取込管理テーブルに登録する情報を格納する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
@Data
public class FacilityDataImportManagementTaskInfo {

    /**
     * データセットの空間ID
     */
    private String highestVoxelSid;

    /**
     * インフラ事業者ID
     */
    private String infraCompanyId;

    /**
     * 取込状態ID(取込前)
     */
    private Integer preImportStatus;

    /**
     * SidDataファイルURL
     */
    private AmazonS3URI sidDataFilePath;

    /**
     * LinkDataファイルURL
     */
    private AmazonS3URI linkDataFilePath;

    /**
     * 取込ファイル登録日時
     */
    private Timestamp importFileTime;

    /**
     * 3DViewerファイルURL
     */
    private AmazonS3URI threeDViewerFilePath;

    public FacilityDataImportManagementTaskInfo(String highestVoxelSid,
        String infraCompanyId, Integer preImportStatus,
        AmazonS3URI sidDataFilePath, AmazonS3URI linkDataFilePath, Timestamp importFileTime,
        AmazonS3URI threeDViewerFilePath) {
        this.highestVoxelSid = highestVoxelSid;
        this.infraCompanyId = infraCompanyId;
        this.preImportStatus = preImportStatus;

        this.sidDataFilePath = sidDataFilePath;
        this.linkDataFilePath = linkDataFilePath;
        this.importFileTime = importFileTime;
        this.threeDViewerFilePath = threeDViewerFilePath;
    }
}
