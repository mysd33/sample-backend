# SpringBootのBackendアプリケーションサンプル

## 概要
* TODOを管理するREST APIを提供するSpringBootのサンプルアプリケーションである。
* TODOの取得、TODOの登録、TODOの完了、TODOの削除を行える。
* 別プロジェクト、別リポジトリで作成している、BFF(Backend For Frontend)のサンプルアプリケーション(sampe-bff)からAPIを利用している。
![実装イメージ](img/sample-backend.png)
## REST API一覧
* todoテーブルで管理しているデータを操作するためのREST APIを作成している
* APIを以下に示す。
    * パス内に含まれている{todoId}は、TodoリソースのIdを示すパス変数
    
    | API名 | HTTPメソッド | パス | ステータスコード | 説明 |
    | ---- | ---- | ---- | ---- | --- |
    | Todo一覧の取得 | GET | /api/v1/todos | 200(OK) | Todoリソースを全件取得する。 |
    | Todoの取得 | GET | /api/v1/todos/{todoId} | 200(OK) | Todoリソースを一件取得する。
    | Todoの登録 | POST | /api/v1/todos | 201(Created) | Todoリソースを新規作成する。 |
    | Todoの完了 | PUT  | /api/v1/todos/{todoId} | 200(OK) | Todoリソースを完了状態に更新する。 |
    | Todoの削除 | DELETE | /api/v1/todos/{todoId} | 204(No Content) | Todoリソースを削除する。 |

## 事前準備
* 以下のライブラリを用いているので、EclipseのようなIDEを利用する場合には、プラグインのインストールが必要
    * [Lombok](https://projectlombok.org/)
        * [Eclipseへのプラグインインストール](https://projectlombok.org/setup/eclipse)
        * [IntelliJへのプラグインインストール](https://projectlombok.org/setup/intellij)
    * [Mapstruct](https://mapstruct.org/)
        * [EclipseやIntelliJへのプラグインインストール](https://mapstruct.org/documentation/ide-support/)

## EclipseやIntelliJ等での動作確認
* APIの動作確認のため、PostmanやTarend REST ClientのようなREST APIクライントツールが必要
    * [Postman API Client](https://www.postman.com/product/api-client/)
    * [Tarend REST Client(DHC REST Client)](https://chrome.google.com/webstore/detail/talend-api-tester-free-ed/aejoelaoggembcahagimdiliamlcdmfm)

* ブラウザまたは、REST APIクライアントツールを使って、ヘルスチェックポイントエンドポイントをAPIを呼び出す。ローカル実行の場合8000でポートで起動する。
    * GET http://localhost:8000/actuator/health
        * 正常に起動していれば、以下返却する。        
        ```json
        {
            status: "UP"
        }    
        ``` 
* REST APIクライアントツールを使って、APIを呼び出す。
    * TODOの登録1
        * POST http://localhost:8000/api/v1/todos
        * BODY 
        ```json
        {
            "todo_title": "Hello World!"
        }        
        ```
        * HTTPステータス201で返却 
    * TODOの登録2
        * POST http://localhost:8000/api/v1/todos
        * BODY 
        ```json
        {
            "todo_title": "Good Afternoon!"
        }        
        ```
    * TODO一覧の取得
        * GET http://localhost:8000/api/v1/todos
        * HTTPステータス200で2件取得できる

    * TODO一件取得
        * GET http://localhost:8000/api/v1/todos/(TodoId)    
            * 登録時、一覧取得の応答を確認して、URLの（TodoId)を設定
        * HTTPステータス200で該当のTODOを取得できる
    * TODOの完了
        * PUT http://localhost:8000/api/v1/todos/(TodoId)    
        * HTTPステータス200で該当のTODOのfinishedプロパティがtrueになる
    * TODOの削除
        * DELETE http://localhost:8000/api/v1/todos/(TodoId)   
        * HTTPステータス204で返却
    * TODO一覧の取得
        * GET http://localhost:8000/api/v1/todos/(TodoId)    
        * HTTPステータス200で1件取得できる
    * TODOを6件以上登録する
        * HTTPステータス400でエラーレスポンスを返却
        ```json
        {
            "code": "w.ex.2002",
            "message": "5件以上のTodoが未完了のため新しいTodoは作成できません。"
        }    
        ```
    * 完了済のTODOを再度完了する
        * HTTPステータス400でエラーレスポンスを返却
        ```json
        {
            "code": "w.ex.2003",
            "message": "Todo(ID=670f32da-021a-46c0-9871-99b8d9c8b149)はすでに完了しています。"
        }
        ```
    * 存在しないIDのTODOを取得または削除する
        * HTTPステータス400でエラーレスポンスを返却
        ```json
        {
          "code": "w.ex.2001",
          "message": "対象のTodoがありません。"
        }        
        ```


## PostgreSQLのローカル起動
* Profileが「production」に切り替えてSpringBootアプリケーションを実行する場合、DBがPostgreSQLで動作する設定になっているため、事前にPostgreSQLを起動する必要がある。
```sh
#Postgres SQLの起動
docker run --name test-postgres -p 5432:5432 -e POSTGRES_PASSWORD=password -d postgres
#Postgresのコンテナにシェルで入って、psqlコマンドで接続
docker exec -i -t test-postgres /bin/bash
> psql -U postgres

# psqlで、testdbデータベースを作成
postgres> CREATE DATABASE testdb;
```
## Dockerでのアプリ起動
* Mavenビルド
```sh
#Windows
.\mvnw.cmd package
#Linux/Mac
./mvnw package
```
* ローカルでDockerビルド
```sh
docker build -t XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/sample-backend:latest .
```

* ローカルでDocker実行（Profileを「dev」でSpringBoot実行する場合）
```sh
docker run -d -p 8000:8000 --name samplebackend --env SPRING_PROFILES_ACTIVE=dev,log_default XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/sample-backend:latest

#logをjson形式に変更する場合
docker run -d -p 8000:8000 --name samplebackend --env SPRING_PROFILES_ACTIVE=dev,log_container XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/sample-backend:latest
```

* ローカルでDocker実行（Profileを「production」でSpringBoot実行する場合）
    * ※Redisのローカル起動、PostgreSQLのローカル起動も必要
```sh
docker run -d -p 8000:8000 --name samplebackend --env SPRING_PROFILES_ACTIVE=production,log_default --env SERVER_PORT=8000 --env SPRING_DATASOURCE_URL=jdbc:postgresql://(ローカルPCのプライベートIP):5432/testdb XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/sample-backend:latest

#logをjson形式に変更する場合
docker run -d -p 8000:8000 --name samplebackend --env SPRING_PROFILES_ACTIVE=production,log_container --env SERVER_PORT=8000 --env SPRING_DATASOURCE_URL=jdbc:postgresql://(ローカルPCのプライベートIP):5432/testdb XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/sample-backend:latest
```

* ECRプッシュ
```sh
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com
docker push XXXXXXXXXXXX.dkr.ecr.ap-northeast-1.amazonaws.com/sample-backend:latest
```
## ソフトウェアフレームワーク
* 本サンプルアプリケーションでは、ソフトウェアフレームワーク実装例も同梱している。簡単のため、アプリケーションと同じプロジェクトでソース管理している。
* ソースコードはcom.example.fwパッケージ配下に格納されている。    
    * 本格的な開発を実施する場合には、業務アプリケーションと別のGitリポジトリとして管理し、CodeArtifactやSonatype NEXUSといったライブラリリポジトリサーバでjarを管理し、pom.xmlから参照するようにすべきであるし、テストやCI/CD等もちゃんとすべきであるが、ここでは、あえて同じプロジェクトに格納してノウハウを簡単に参考にしてもらいやすいようにしている。
* 各機能と実現方式は、以下の通り。

| 分類 | 機能 | 機能概要と実現方式 | 拡張実装 | 拡張実装の格納パッケージ |
| ---- | ---- | ---- | ---- | ---- |
| オンライン | オンラインAP制御 | SpringMVCの機能を利用し、ユーザからの要求受信、ビジネスロジック実行、応答返却まで一連の定型的な処理を実行を制御する。 | - | - |
| | 入力チェック| Java BeanValidationとSpringのValidation機能を利用し、単項目チェックや相関項目チェックといった画面の入力項目に対する形式的なチェックを実施する。 | ○ | com.example.fw.common.validation |
|  | 認証・認可| Spring Securityを利用し、DBで管理するユーザ情報をもとに認証、認可を行う。 | - | - |
| | 集約例外ハンドリング | SpringMVCのControllerAdviceやAOPを利用し、エラー（例外）発生時、エラーログの出力、DBのロールバック、エラー画面やエラー電文の返却といった共通的なエラーハンドリングを実施する。 | ○ | com.example.fw.web.aspect |
| | トランザクション管理機能 | Spring Frameworkのトランザクション管理機能を利用して、@Transactionalアノテーションによる宣言的トランザクションを実現する機能を提供する。 | - | - |
| | 分散トレーシング | Spring Cloud Sleathを利用して、トレースIDやスパンIDをAP間でのREST API呼び出しで引継ぎログに記録することで、分散トレーシングを実現する。 | - | - |
| | ヘルスチェック | Spring Boot Actuatorを利用して、ヘルスチェックエンドポイントを提供する。その他、Micrometerメトリックの情報提供も行う。 | - | - |
| | グレースフルシャットダウン | SpringBootの機能で、Webサーバ（組み込みTomcat）のグレースフルシャットダウン機能を提供する 。 | - | - |
| オン・バッチ共通 | RDBアクセス | MyBatisやSpringとの統合機能を利用し、DBコネクション取得、SQLの実行等のRDBへのアクセスのため定型的な処理を実施し、ORマッピングやSQLマッピングと呼ばれるドメイン層とインフラ層のインピーダンスミスマッチを吸収する機能を提供する。 | - | - |
| | HTTPクライアント | WebClientやRestTemplateを利用してREST APIの呼び出しやサーバエラー時の例外の取り扱いを制御する。 | ○ | com.example.fw.common.httpclient |
| | 非同期実行依頼 | Spring JMS、AWS SQS Java Messaging Libraryを利用し、SQSの標準キューを介した非同期実行依頼のメッセージを送信する。 | ○ | com.example.fw.common.async |
| | メッセージ管理 | MessageResourceで画面やログに出力するメッセージを管理する。 | ○ | com.example.fw.common.message |
| | 例外 | RuntimeExceptionを継承し、エラーコード（メッセージID）やメッセージを管理可能な共通的なビジネス例外、システム例外を提供する。 | ○ | com.example.fw.common.exception |
| | ロギング | Slf4jとLogback、SpringBootのLogback拡張の機能を利用し、プロファイルによって動作環境に応じたログレベルや出力先（ファイルや標準出力）、出力形式（タブ区切りやJSON）に切替可能とする。またメッセージIDをもとにログ出力可能な汎用的なAPIを提供する。 | ○ | com.example.fw.common.logging |
| | プロパティ管理 | SpringBootのプロパティ管理を使用して、APから環境依存のパラメータを切り出し、プロファイルによって動作環境に応じたパラメータ値に置き換え可能とする。 | - | - |
| | オブジェクトマッピング | MapStructを利用し、類似のプロパティを持つリソースオブジェクトやDTOとドメインオブジェクト間で、値のコピーやデータ変換処理を簡単にかつ高速に行えるようにする。 | - | - |
| | DI | Springを利用し、DI（依存性の注入）機能を提供する。 | - | - |
| | AOP | SpringとAspectJAOPを利用し、AOP機能を提供する。 | - | - |
| | ボイラープレートコード排除 | Lombokを利用し、オブジェクトのコンストラクタやGetter/Setter等のソースコードを自動生成し、ボイラープレートコードを排除する。 | - | - |

* 以下は、今後追加適用を検討中。

| 分類 | 機能 | 機能概要と実現方式 | 拡張実装 | 拡張実装の格納パッケージ |
| ---- | ---- | ---- | ---- | ---- |
| オンライン | OIDC認証 | Spring Securityの機能でOIDCの認証を行う。 | - | - |
| | 分散トレーシング（X-Ray） | AWS X-Rayを利用して、サービス間の分散トレーシング・可視化を実現する。 | - | - |
| オン・バッチ共通 | リトライ・サーキットブレーカ | Spring Cloud Circuit Breaker（Resillience4j）を利用し、REST APIの呼び出しでの一時的な障害に対するリトライやフォールバック処理等を制御する。なお、AWSリソースのAPI呼び出しは、AWS SDKにてエクスポネンシャルバックオフによるリトライ処理を提供済。 | - | - |
| | プロパティ管理（SSM） | Spring Cloud for AWS機能により、APから環境依存のパラメータをAWSのSSMパラメータストアに切り出し、プロファイルによって動作環境に応じたパラメータ値に置き換え可能とする。 | - | - |
| | テストコード作成支援 | JUnit、Mockito、Springのテスト機能を利用して、単体テストコードや結合テストコードの実装を支援する機能を提供する。 | - | - |