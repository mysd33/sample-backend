# SpringBootのBackendアプリケーションサンプル

## 概要
* TODOを管理するREST APIを提供するSpringBootのサンプルアプリケーションである。
* TODOの取得、TODOの登録、TODOの完了、TODOの削除を行える。

## ソフトウェアフレームワーク
* 本サンプルアプリケーションでは、ソフトウェアフレームワーク実装例も同梱している。簡単のため、アプリケーションと同じプロジェクトでソース管理している。
    * 本格的な開発を実施する場合には、実装を参考に、別リポジトリとして管理し、CodeArtifactやSonatype NEXUSといったライブラリリポジトリサーバで管理すべきであるし、テスト等もちゃんとすべきであるが、ノウハウを簡単に参考にしてもらいやすいようあえてそうしている。

## REST API一覧
* todoテーブルで管理しているデータを操作するためのREST APIを作成している
* 別プロジェクト、別リポジトリで作成している、BFF(Backend For Frontend)のサンプルアプリケーションから利用している。
* APIを以下に示す。
    * パス内に含まれている{todoId}は、TodoリソースのIdを示すパス変数
    
    | API名 | HTTPメソッド | パス | ステータスコード | 説明 |
    | ---- | ---- | ---- | ---- | --- |
    | Todo一覧の取得 | GET | /api/v1/todos | 200(OK) | Todoリソースを全件取得する。 |
    | Todoの取得 | GET | /api/v1/todos/{todoId} | 200(OK) | Todoリソースを一件取得する。
    | Todoの登録 | POST | /api/v1/todos | 201(Created) | Todoリソースを新規作成する。 |
    | Todoの完了 | PUT  | /api/v1/todos/{todoId} | 200(OK) | Todoリソースを完了状態に更新する。 |
    | Todoの削除 | DELETE | /api/v1/todos/{todoId} | 204(No Content) | Todoリソースを削除する。 |

# 事前準備
* 以下のライブラリを用いているので、EclipseのようなIDEを利用する場合には、プラグインのインストールが必要
    * [Lombok](https://projectlombok.org/)
        * [Eclipseへのプラグインインストール](https://projectlombok.org/setup/eclipse)
        * [IntelliJへのプラグインインストール](https://projectlombok.org/setup/intellij)
    * [Mapstruct](https://mapstruct.org/)
        * [EclipseやIntelliJへのプラグインインストール](https://mapstruct.org/documentation/ide-support/)

# 動作確認
* APIの動作確認のため、PostmanやDHCのようなREST APIクライントツールが必要
    * [Postman API Client](https://www.postman.com/product/api-client/)
    * [Tarend REST Client(DHC REST Client)](https://chrome.google.com/webstore/detail/talend-api-tester-free-ed/aejoelaoggembcahagimdiliamlcdmfm)

* REST APIクライアントツールを使って、APIを呼び出し
    * TODOの登録1
        * POST http://localhost:8080/api/v1/todos
        * BODY 
        ```json
        {
            "todoTitle": "Hello World!"
        }        
        ```
    * TODOの登録2
        * POST http://localhost:8080/api/v1/todos
        * BODY 
        ```json
        {
            "todoTitle": "Good Afternoon!"
        }        
        ```
    * TODO一覧の取得
        * GET http://localhost:8080/api/v1/todos
            * 2件取得できる

    * TODO一覧の取得
        * GET http://localhost:8080/api/v1/todos/(TodoId)    
            * 登録時、一覧取得の応答を確認して、URLの（TodoId)を設定
    * TODOの完了
        * POST http://localhost:8080/api/v1/todos/(TodoId)    
        * finishedがtrueになる
    * TODOの削除
        * DELET http://localhost:8080/api/v1/todos/(TodoId)   
    * TODO一覧の取得
        * GET http://localhost:8080/api/v1/todos/(TodoId)    
            * 1件取得できる