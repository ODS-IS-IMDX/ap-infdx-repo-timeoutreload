// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * 共通処理クラス
 * プロジェクト内で共通的に利用される処理を行う
 *
 * @author Ishii Yuki
 * @version 1.0
 */
public class Common {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static String secretManagerResponse;

    private static String secretManagerName;
    private static Region secretManagerRegion;

    private static String dbPath;
    private static String dbUser;
    private static String dbPassword;

    public static Logger getLogger() {
        return logger;
    }

    public static String getDburl() {
        return dbPath;
    }

    public static String getDbuser() {
        return dbUser;
    }

    public static String getDbpassword() {
        return dbPassword;
    }

    /**
     * プロパティファイルを読み込む
     *
     * @throws Exception
     */
    public static void readProperty() throws Exception {
        Properties properties = new Properties();

        try (FileReader reader = new FileReader(Const.PROPERTY_FILE_PATH)) {
            properties.load(reader);

            secretManagerName = properties.getProperty(Const.PROPERTY_NAME_SECLET_MANAGER_NAME);
            secretManagerRegion = Region.of(properties
                    .getProperty(Const.PROPERTY_NAME_SECRET_MANAGER_REGION));
        } catch (Exception e) {
            // プロパティファイル読込失敗ログ出力
            Common.outputLogMessage("error.ImportData.00013",
                    Common.getErrorInfoString(e));
            throw e;
        }
    }

    /**
     * SecretManagerを読み込む
     *
     * @throws JsonProcessingException
     */
    public static void readSecretManager() throws JsonProcessingException {

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(secretManagerRegion)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretManagerName)
                .build();

        GetSecretValueResponse getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        secretManagerResponse = getSecretValueResponse.secretString();

        dbPath = MessageFormat.format(Const.YDB_URL_FORMAT,
                getSecretValue(Const.SECRET_KEY_YDB_HOST),
                getSecretValue(Const.SECRET_KEY_YDB_PORT),
                getSecretValue(Const.SECRET_KEY_YDB_NAME));
        dbUser = getSecretValue(Const.SECRET_KEY_YDB_USER);
        dbPassword = getSecretValue(Const.SECRET_KEY_YDB_PASS);
    }

    /**
     * SecretManagerから取得したJSONより、キーに対応する値を取得する
     *
     * @param secretKey SecretManagerキー
     * @return キーに対応する値
     * @throws JsonProcessingException
     */
    public static String getSecretValue(String secretKey)
            throws JsonProcessingException {

        String secretValue = null;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(secretManagerResponse);
        secretValue = jsonNode.get(secretKey).asText();

        return secretValue;
    }

    /**
     * IDと対応したログメッセージを出力する
     *
     * @param messageId メッセージID
     * @param param     メッセージに必要なパラメータ
     */
    public static void outputLogMessage(String messageId, String... param) {
        String messageFormat = LogMessage.getLogMessageMap().get(messageId);

        Object[] idAndParam = new Object[param.length + 1];
        idAndParam[0] = messageId;
        System.arraycopy(param, 0, idAndParam, 1, param.length);

        String logMessage = MessageFormat.format(messageFormat, idAndParam);

        switch (messageId.split("\\.")[0]) {
            case "info":
                logger.info(logMessage);
                break;
            case "warn":
                logger.warn(logMessage);
                break;
            case "error":
                logger.error(logMessage);
                break;
        }
    }

    /**
     * 現在実行中のメソッド名を取得する
     *
     * @return 現在実行中のメソッド名
     */
    public static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * e.messageとe.printStackTraceを文字列として取得する
     *
     * @param e エラー情報
     * @return e.messageとe.printStackTrace文字列
     */
    public static String getErrorInfoString(Exception e) {
        String errorMessage = e.getMessage();
        String stackTrace = getPrintStackTraceString(e);

        return errorMessage + ":" + stackTrace;
    }

    /**
     * e.printStackTraceを文字列として取得する
     *
     * @param e エラー情報
     * @return e.printStackTraceをつなげた文字列
     */
    public static String getPrintStackTraceString(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append(element.toString())
                    .append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    public static Integer getIntegerResult(ResultSet resultSet,
            String colmnLabel) throws SQLException {
        // カラム名を指定して整数値を取得
        int intValue = resultSet.getInt(colmnLabel);

        // 取得した値をInteger型に変換
        Integer integerValue = resultSet.wasNull() ? null : intValue;
        return integerValue;
    }
}
