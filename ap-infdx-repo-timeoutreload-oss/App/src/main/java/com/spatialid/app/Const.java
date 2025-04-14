// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import com.spatialid.app.model.ImportStatusInfo;

/**
 * 定数定義クラス
 * プロジェクト内で利用する定数を定義する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class Const {
        // プロパティファイル情報
        // プロパティファイルパス
        public static final String PROPERTY_FILE_PATH =
                "src/main/resources/application.properties";
        // プロパティ名(SecretManager接続情報(シークレットID))
        public static final String PROPERTY_NAME_SECLET_MANAGER_NAME =
                "secretManagerName";
        // プロパティ名(SecretManager接続情報(AWSリージョン))
        public static final String PROPERTY_NAME_SECRET_MANAGER_REGION =
                "secretManagerRegion";

        // 設備データ取込管理テーブル
        // カラム名(データセットの空間ID)
        public static final String COLUMN_NAME_HIGHEST_VOXCEL_ID =
                "highest_voxel_id";
        // カラム名(インフラ事業者ID)
        public static final String COLUMN_NAME_INFRA_COMPANY_ID =
                "infra_company_id";
        // カラム名(取込状態(取込前))
        public static final String COLUMN_NAME_PRE_IMPORT_STATUS =
                "pre_import_status";
        // カラム名(SidDataファイルURL)
        public static final String COLUMN_NAME_SID_DATA_FILE_URL =
                "sid_data_file_url";
        // カラム名(LinkDataファイルURL)
        public static final String COLUMN_NAME_LINK_DATA_FILE_URL =
                "link_data_file_url";
        // カラム名(取込ファイル登録日時)
        public static final String COLUMN_NAME_IMPORT_FILE_TIME =
                "import_file_time";
        // カラム名(3DViewerファイルURL)
        public static final String COLUMN_NAME_THREE_D_VIEWER_FILE_URL =
                "three_d_viewer_file_url";
        // 取込状態(ID, 状態名)
        public static final ImportStatusInfo PRE_IMPORT_STATUS_APPROVED =
                new ImportStatusInfo(13, "承認済データあり");
        public static final ImportStatusInfo IMPORT_STATUS_IMPORTING =
                new ImportStatusInfo(21, "取込処理中");
        public static final ImportStatusInfo IMPORT_STATUS_SYSTEM_ERROR =
                new ImportStatusInfo(29, "取込処理エラー");
        // エラー情報のフォーマット
        public static final String ERROR_FORMAT = "M月d日 タイムアウト発生";

        // SecretManager情報
        // YugabyteDBのホスト名
        public static final String SECRET_KEY_YDB_HOST = "YDB-HOST";
        // YugabyteDBのポート
        public static final String SECRET_KEY_YDB_PORT = "YDB-PORT";
        // YugabyteDBのDB名
        public static final String SECRET_KEY_YDB_NAME = "YDB-NAME";
        // YugabyteDBのユーザ名
        public static final String SECRET_KEY_YDB_USER = "YDB-B1-USER";
        // YugabyteDBのユーザパスワード
        public static final String SECRET_KEY_YDB_PASS = "YDB-B1-USER-PASSWORD";
        // YugabyteDB接続情報のフォーマット
        public static final String YDB_URL_FORMAT =
                "jdbc:postgresql://{0}:{1}/{2}";

        // ログメッセージ
        // バッチ名称
        public static final String FUNCTION_NAME = "タイムアウトデータ処理バッチ";
}
