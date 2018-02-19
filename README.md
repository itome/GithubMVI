# GithubMVI
[Git push Hackthon](https://github.com/CyberAgent/git-push-hackathon)のためのGithubクライアントアプリ

## 実行方法
プロジェクトルートの`local.proverties`に下記のようにGithubのclientIdとclientSecretを入力してください。
```
...
CLIENT_ID=xxxxxxxxxxxxxxxxxxxxxxxxxx
CLIENT_SECRET=xxxxxxxxxxxxxxxxxxxxxxxxxx
...
```

## 仕様について
画面数は4で、それぞれ
- ログイン画面(ログイン以降はスプラッシュ画面として機能)
- イベント一覧画面
- レポジトリ詳細画面(レポジトリに対するスターの登録/削除)
- ユーザー詳細画面(ユーザーに対するフォロー/フォロー解除)


## 設計
今回は[こちら](https://github.com/oldergod/android-architecture)のMVIパターンを採用しました。Ankoを使ったのでViewの部分にUI層が加わっています。

![](https://raw.githubusercontent.com/oldergod/android-architecture/todo-mvi-rxjava-kotlin/art/MVI_detail.png)


### それぞれのレイヤーについて
Repositroy層、ViewModel層、Activity層、UI層に分けて設計しています。
#### Repository層
Realmを使ったLocalDataStoreと、Retrofitを使ったRemoteDataStoreをRepositoryでまとめています。LocalDataStoreはログインデータを保存するのみでキャッシュ的な使い方はしていません。
#### ViewModel層
アプリのロジックを担当する層で、Intentの入力に対して、ViewStateを返します。Android固有のロジックから切り離されているので簡単にユニットテストが書けます。
#### Activity層
ViewModel層とUI層の橋渡しとActivity遷移などのAndroid固有のロジックのみを担当します。ビューの管理を後述のUI層が行うので他のアーキテクチャに比べて役割はかなり少ないです。
#### UI層
ankoLayoutライブラリを使ってkotlinでビューを作成しています。PublishSubject経由でイベントをActivity層に通知し、ビューの更新はViewStateの変更通知のみによって行うので、レイアウトが複雑になっても入力と出力をシンプルに保てます。



## UI/UXについて
デザインには自信がないのでサンプルアプリっぽい感じにしました。イベント一覧画面は最後までスクロールしたら追加のイベントを取得する実装にして、一覧性と操作性をあげています。
レポジトリ詳細、ユーザー詳細画面はほとんど同じで、CoordinatorLayoutを使って実装しています。



### 使用したライブラリ
- [realm](https://github.com/realm/realm-java) ログインデータなどを保存するデータベース用に使っています。
- [rxjava](https://github.com/ReactiveX/RxJava) アプリの中核を担うライブラリで、ViewModel層の入力から出力まで全てをrxのストリームとして扱っています。
- [anko](https://github.com/Kotlin/anko) Androidのレイアウトをkotlinで書くためのライブラリ
- [retrofit](https://github.com/square/retrofit) API通信用
- [dagger2](https://github.com/google/dagger) DI用、主にViewModel層とActivity層をつなぐために使っています。
- [glide](https://github.com/bumptech/glide) 画像表示用
- [MarkdownView](https://github.com/tiagohm/MarkdownView) README表示用
- [mockito](https://github.com/mockito/mockito) テスト用
