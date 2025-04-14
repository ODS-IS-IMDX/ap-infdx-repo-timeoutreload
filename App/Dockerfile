# © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

# ベースイメージを指定
FROM amazoncorretto:21

# 必要なパッケージをインストール
RUN yum update -y 
RUN yum install -y \
    unzip \
    tar \
    git \
    tzdata
RUN yum clean all

# JAVA設定環境変数
ENV JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
ENV PATH=$JAVA_HOME/bin:$PATH
ENV JAVA_TOOL_OPTIONS="-Xms512m -Xmx1024m"

# 作業ディレクトリの設定
WORKDIR /App

# プロジェクトコピー
COPY META-INF/ ./META-INF/
COPY src/ ./src/
COPY pom.xml ./

# 設定ファイルを本番用に上書き
RUN mv ./src/main/resources/application_prod.properties \
 ./src/main/resources/application.properties

# アプリケーションの実行に必要なファイルをコピー
COPY target/*jar-with-dependencies.jar ./app.jar

# タイムゾーンを日本時間(JST)に設定
RUN ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime && \
    echo "Asia/Tokyo" > /etc/timezone
ENV TZ=Asia/Tokyo

# アプリケーションを起動
CMD ["java", "-jar", "app.jar"]
