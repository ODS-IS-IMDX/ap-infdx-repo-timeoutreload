// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import com.spatialid.app.Common;
import com.spatialid.app.Const;
import com.spatialid.app.model.FacilityDataImportManagementTaskInfo;

/**
 * 設備データ取込管理ハンドラークラス
 * 設備データ取込管理テーブルに対する操作を行う
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class FacilityDataImportManagementHandler {
        /**
         * タイムアウトデータ検索SQL実行
         * 
         * @param connection 接続情報
         * @return タイムアウトデータ検索SQL実行結果
         * @throws SQLException
         */
        public static ResultSet executeSelectImportingTaskStatement(
                Connection connection)
                throws SQLException {
                PreparedStatement ps = null;
                ResultSet rs;

                try {
                        ps = prepareSelectImportingTaskStatement(connection);
                        rs = ps.executeQuery();
                } catch (SQLException e) {
                        Common.outputLogMessage("error.ImportData.00002",
                                Common.getCurrentMethodName(),
                                (ps == null ? "" : ps.toString()),
                                Common.getErrorInfoString(e));
                        throw e;
                }
                Common.outputLogMessage("info.ImportData.00005",
                        Common.getCurrentMethodName(), ps.toString());
                return rs;
        }

        /**
         * タイムアウトデータ検索SQL作成
         * 
         * @param connection 接続情報
         * @return タイムアウトデータ検索SQL
         * @throws SQLException
         */
        public static PreparedStatement prepareSelectImportingTaskStatement(
                Connection connection) throws SQLException {
                String sql =
                        "select highest_voxel_id,infra_company_id,pre_import_status,pre_sid_data_file_url,pre_link_data_file_url,pre_import_file_time,pre_three_d_viewer_file_url,"
                                + "import_status_id,sid_data_file_url,link_data_file_url,import_file_time,three_d_viewer_file_url"
                                + " from facility_data_import_management where import_status_id = ? for update";

                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setObject(1, Const.IMPORT_STATUS_IMPORTING.getId(),
                        java.sql.Types.INTEGER);

                return pstmt;
        }

        /**
         * 設備データ取込管理データ更新(取込処理エラー)SQL実行
         * 
         * @param connection 接続情報
         * @param taskInfo 取込タスク情報
         * @throws SQLException
         */
        public static void executeUpdateFacilityDataImportManagementImportingStatement(
                Connection connection,
                FacilityDataImportManagementTaskInfo taskInfo)
                throws SQLException {
                PreparedStatement ps = null;
                try {
                        ps = prepareUpdateFacilityDataImportManagementImportingStatement(
                                connection, taskInfo);
                        ps.executeUpdate();
                } catch (SQLException e) {
                        Common.outputLogMessage("error.ImportData.00009",
                                Const.IMPORT_STATUS_IMPORTING.getStatus(),
                                taskInfo.getHighestVoxelSid(),
                                taskInfo.getInfraCompanyId(),
                                Common.getErrorInfoString(e));
                        throw e;
                }
                Common.outputLogMessage("info.ImportData.00007",
                        Common.getCurrentMethodName(), ps.toString());
        }

        /**
         * 設備データ取込管理データ更新(取込処理エラー)SQL作成
         * 
         * @param connection 接続情報
         * @param taskInfo 取込タスク情報
         * @return 設備データ取込管理データ更新(取込処理エラー)SQL
         * @throws SQLException
         */
        public static PreparedStatement prepareUpdateFacilityDataImportManagementImportingStatement(
                Connection connection,
                FacilityDataImportManagementTaskInfo taskInfo)
                throws SQLException {
                String sql = "update facility_data_import_management "
                        + "set pre_import_status = ?, pre_sid_data_file_url = ?, pre_link_data_file_url = ?, pre_import_file_time = ?, pre_three_d_viewer_file_url = ?, "
                        + "import_status_id = ?, sid_data_file_url = null, link_data_file_url = null, import_file_time = null, three_d_viewer_file_url = null, error_info = ?, update_time = ? "
                        + "where highest_voxel_id = ? and infra_company_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setObject(1, Const.PRE_IMPORT_STATUS_APPROVED.getId(),
                        java.sql.Types.INTEGER);
                pstmt.setString(2, taskInfo.getSidDataFilePath().toString());
                pstmt.setString(3, taskInfo.getLinkDataFilePath().toString());
                pstmt.setTimestamp(4, taskInfo.getImportFileTime());
                pstmt.setString(5, taskInfo.getThreeDViewerFilePath().toString());

                pstmt.setObject(6, Const.IMPORT_STATUS_SYSTEM_ERROR.getId(),
                        java.sql.Types.INTEGER);
                Timestamp currentTimestamp =
                        new Timestamp(System.currentTimeMillis());
                pstmt.setString(7, new SimpleDateFormat(Const.ERROR_FORMAT)
                        .format(currentTimestamp));
                pstmt.setTimestamp(8, currentTimestamp);

                pstmt.setString(9, taskInfo.getHighestVoxelSid());
                pstmt.setString(10, taskInfo.getInfraCompanyId());

                return pstmt;
        }

        /**
         * 設備データ取込管理データ更新(取込処理エラー)SQL実行（更新済みデータあり）
         * 
         * @param connection 接続情報
         * @param taskInfo 取込タスク情報
         * @throws SQLException
         */
        public static void executeUpdateFacilityDataImportManagementImportingStatementWithLatestUpdate(
                Connection connection,
                FacilityDataImportManagementTaskInfo taskInfo)
                throws SQLException {
                PreparedStatement ps = null;
                try {
                        ps = prepareUpdateFacilityDataImportManagementImportingStatementWithLatestUpdate(
                                connection, taskInfo);
                        ps.executeUpdate();
                } catch (SQLException e) {
                        Common.outputLogMessage("error.ImportData.00009",
                                Const.IMPORT_STATUS_IMPORTING.getStatus(),
                                taskInfo.getHighestVoxelSid(),
                                taskInfo.getInfraCompanyId(),
                                Common.getErrorInfoString(e));
                        throw e;
                }
                Common.outputLogMessage("info.ImportData.00007",
                        Common.getCurrentMethodName(), ps.toString());
        }

        /**
         * 設備データ取込管理データ更新(取込処理エラー)SQL作成（更新済みデータあり）
         * 
         * @param connection 接続情報
         * @param taskInfo 取込タスク情報
         * @return 設備データ取込管理データ更新(取込処理エラー)SQL
         * @throws SQLException
         */
        public static PreparedStatement prepareUpdateFacilityDataImportManagementImportingStatementWithLatestUpdate(
                Connection connection,
                FacilityDataImportManagementTaskInfo taskInfo)
                throws SQLException {
                String sql = "update facility_data_import_management "
                        + "set import_status_id = ?, sid_data_file_url = null, link_data_file_url = null, import_file_time = null, three_d_viewer_file_url = null, error_info = ?, update_time = ? "
                        + "where highest_voxel_id = ? and infra_company_id = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);

                pstmt.setObject(1, Const.IMPORT_STATUS_SYSTEM_ERROR.getId(),
                        java.sql.Types.INTEGER);
                Timestamp currentTimestamp =
                        new Timestamp(System.currentTimeMillis());
                pstmt.setString(2, new SimpleDateFormat(Const.ERROR_FORMAT)
                        .format(currentTimestamp));
                pstmt.setTimestamp(3, currentTimestamp);

                pstmt.setString(4, taskInfo.getHighestVoxelSid());
                pstmt.setString(5, taskInfo.getInfraCompanyId());

                return pstmt;
        }
}
