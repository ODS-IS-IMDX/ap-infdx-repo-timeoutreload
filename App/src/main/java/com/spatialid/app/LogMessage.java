// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import java.util.HashMap;
import java.util.Map;

/**
 * ログメッセージクラス
 * ログIDとログメッセージの組を定義する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class LogMessage {
        private static Map<String, String> logMessageMap =
                new HashMap<String, String>() {
                        {
                                put("info.ImportData.00001",
                                        "{1}を開始しました。メッセージID：[{0}]");
                                put("info.ImportData.00002",
                                        "{1}が正常終了しました。 メッセージID：[{0}]");
                                put("info.ImportData.00005",
                                        "{1}でレコード検索に成功しました。SQL：[{2}], メッセージID：[{0}]");
                                put("info.ImportData.00007",
                                        "{1}でレコード更新に成功しました。SQL：[{2}], メッセージID：[{0}]");
                                put("info.ImportData.00009",
                                        "{1}でトランザクションを開始しました。 メッセージID：[{0}], CSVファイル：[{2}]");
                                put("info.ImportData.00010",
                                        "{1}でトランザクションをコミットしました。 メッセージID：[{0}], CSVファイル：[{2}]");
                                put("info.ImportData.00011",
                                        "{1}でトランザクションをロールバックしました。 メッセージID：[{0}], CSVファイル：[{2}]");
                                put("warn.ImportData.00010",
                                        "空間ID取込処理がタイムアウトしました。データセットの空間ID：[{1}], インフラ事業者ID：[{2}], メッセージID：[{0}]");
                                put("error.ImportData.00001",
                                        "システムエラーが発生したため、{1}が異常終了しました。エラー情報：[{2}], メッセージID：[{0}]");
                                put("error.ImportData.00002",
                                        "{1}でレコード検索に失敗しました。SQL：[{2}] , エラー情報：[{3}], メッセージID：[{0}]");
                                put("error.ImportData.00009",
                                        "取込状態を「{1}」に変更できませんでした。空間ID：[{2}], インフラ事業者ID：[{3}], エラー情報：[{4}], メッセージID：[{0}]");
                                put("error.ImportData.00013",
                                        "プロパティファイルの読込に失敗しました。エラー情報：[{1}], メッセージID：[{0}]");
                }
        };

        public static Map<String, String> getLogMessageMap() {

                return logMessageMap;
        }
}
