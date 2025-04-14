// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.amazonaws.services.s3.AmazonS3URI;
import com.spatialid.app.dao.FacilityDataImportManagementHandler;
import com.spatialid.app.model.FacilityDataImportManagementTaskInfo;

/**
 * メインクラス
 * タイムアウトデータ処理バッチのメイン処理を行う
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {

        // バッチ開始ログの出力
        Common.outputLogMessage("info.ImportData.00001", Const.FUNCTION_NAME);

        // 設定読込(SecretManager)
        try {
            // 設定読込(プロパティファイル)
            Common.readProperty();
            // 設定読込(SecretManager)
            Common.readSecretManager();
        } catch (Exception e) {
            // バッチ終了ログの出力(システムエラー)
            Common.outputLogMessage("error.ImportData.00001",
                Const.FUNCTION_NAME, Common.getErrorInfoString(e));
            return;
        }

        try (Connection connection =
            DriverManager.getConnection(Common.getDburl(), Common.getDbuser(),
                Common.getDbpassword())) {

            ResultSet resultSet = null;
            FacilityDataImportManagementTaskInfo taskInfo = null;

            Common.outputLogMessage("info.ImportData.00009",
                Common.getCurrentMethodName(), "-");
            connection.setAutoCommit(false);
            try {
                // タイムアウトデータ検索
                resultSet = FacilityDataImportManagementHandler
                    .executeSelectImportingTaskStatement(connection);
                while (resultSet.next()) {
                    Integer preImportStatus = Common.getIntegerResult(resultSet,
                        Const.COLUMN_NAME_PRE_IMPORT_STATUS);
                    String highestVoxelSid = resultSet
                        .getString(Const.COLUMN_NAME_HIGHEST_VOXCEL_ID);
                    String infraCompanyId =
                        resultSet.getString(Const.COLUMN_NAME_INFRA_COMPANY_ID);

                    // 既に新規タスクが作成されていた場合、pre_xxx のカラムは変更しない
                    if (preImportStatus != null) {
                        taskInfo = new FacilityDataImportManagementTaskInfo(
                            highestVoxelSid, infraCompanyId,
                            preImportStatus, null, null, null, null);

                        // 設備データ取込管理データ更新(承認済データありにする)
                        FacilityDataImportManagementHandler
                            .executeUpdateFacilityDataImportManagementImportingStatementWithLatestUpdate(
                                connection, taskInfo);
                    } else {
                        AmazonS3URI sidDataFilePath = new AmazonS3URI(
                            resultSet
                                .getString(Const.COLUMN_NAME_SID_DATA_FILE_URL));
                        AmazonS3URI linkDataFilePath = new AmazonS3URI(
                            resultSet
                                .getString(Const.COLUMN_NAME_LINK_DATA_FILE_URL));
                        Timestamp importFileTime = resultSet
                            .getTimestamp(Const.COLUMN_NAME_IMPORT_FILE_TIME);
                        AmazonS3URI threeDViewerFilePath = new AmazonS3URI(
                            resultSet
                                .getString(Const.COLUMN_NAME_THREE_D_VIEWER_FILE_URL));

                        taskInfo = new FacilityDataImportManagementTaskInfo(
                            highestVoxelSid, infraCompanyId,
                            preImportStatus, sidDataFilePath, linkDataFilePath, importFileTime,
                            threeDViewerFilePath);

                        // 設備データ取込管理データ更新(承認済データありにする)
                        FacilityDataImportManagementHandler
                            .executeUpdateFacilityDataImportManagementImportingStatement(
                                connection, taskInfo);
                    }

                    connection.commit();
                    Common.outputLogMessage("warn.ImportData.00010",
                        highestVoxelSid, infraCompanyId);
                    Common.outputLogMessage("info.ImportData.00010",
                        Common.getCurrentMethodName(), "-");
                }
            } catch (SQLException e) {
                connection.rollback();
                Common.outputLogMessage("info.ImportData.00011",
                    Common.getCurrentMethodName(), "-");
                throw e;
            }

            // 更新チェック
        } catch (SQLException e) {
            // バッチ終了ログの出力(システムエラー)
            Common.outputLogMessage("error.ImportData.00001",
                Const.FUNCTION_NAME, Common.getErrorInfoString(e));
            return;
        }

        // バッチ終了ログの出力
        Common.outputLogMessage("info.ImportData.00002", Const.FUNCTION_NAME);
    }
}
