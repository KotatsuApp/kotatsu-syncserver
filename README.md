# Kotatsu Synchronization Server

[Kotatsu](https://github.com/KotatsuApp/Kotatsu) is a free and open source manga reader for Android platform. Supports a lot of online catalogues on different languages with filters and search, offline reading from local storage, favourites, bookmarks, new chapters notifications and more features.

List of official servers:
|Domain|Location|Status|
|---|---|---|
|sync.kotatsu.app|Belarus|![[Uptime](https://health.kotatsu.app/api/badge/1/status)](https://status.kotatsu.app/api/badge/1/status)|
|moe.shirizu.org|Netherlands|![[Uptime](https://health.kotatsu.app/api/badge/7/status)](https://status.kotatsu.app/api/badge/7/status)|

## What is synchronization?

Synchronization is needed to store your collection of favorites, history and categories and have remote access to them. On a synchronized device, you can restore your manga collection in real time without loss. It also supports working across multiple devices. It is convenient for those who use several devices.

## How does synchronization work?

- An account is created and configured in the application where it will store data;
- Synchronization starts. The data selected by the user is saved on the service and stored there under protection;
- Another device connects and syncs with the service;
- The uploaded data appears on the device connected to the account.

## What data can be synchronized?

- Favorites (with categories);
- History.

## How do I sync my data?

Go to `Options -> Settings -> Services`, then select **Synchronization**. Enter your email address (even if you have not registered in the synchronization system, the authorization screen also acts as a registration screen), then come up with and enter a password.

> [!WARNING]
> There is no password recovery from the account at the moment, if you forget the password, you will not be able to log into your account on other devices, so we recommend that you do not forget the password or write it down somewhere.

After the authorization/registration process, you will return back to the **Content** screen. To set up synchronization, select **Synchronization** again, and then you will go to system sync settings. Choose what you want to sync, history, favorites or all together, after which automatic synchronization to our server will begin.

## Can I use a synchronization server on my hosting?

Yes, you can use your synchronization server in the application by specifying its address (`Options -> Settings -> Services -> Synchronization settings -> Server address`). Instructions for deploying the server are below.

## Installation

### Docker

#### Build image container:

```shell
docker build github.com/KotatsuApp/kotatsu-syncserver.git -t kotatsuapp/syncserver
```

#### Run container:

```shell
docker run -d -p 8081:8080 \
  -e DATABASE_HOST=your_mysql_db_host \
  -e DATABASE_USER=your_mysql_db_user \
  -e DATABASE_PASSWORD=your_mysql_db_password \
  -e DATABASE_NAME=your_mysql_db_name \
  -e DATABASE_PORT=your_mysql_db_port \
  -e JWT_SECRET=your_secret \
  -e ALLOW_NEW_REGISTER=true \
  --restart always \
  --name kotatsu-sync kotatsuapp/syncserver
```

In case you don't want others to use your instance, register your accounts then set `ALLOW_NEW_REGISTER` to `false`.

### Docker compose

#### Clone the repository:

```shell
git clone https://github.com/KotatsuApp/kotatsu-syncserver.git \
  && cd kotatsu-syncserver
```

#### Specify your settings (optional)

When the project is launched, the default settings will be used. You can override them via the .env file.

> [!IMPORTANT]
> If you want to override the database settings, you must do so before starting the `db` service, otherwise you will have to delete the database files and restart it.

```shell
cp .env.example .env
```

#### Start services:

```shell
docker compose up -d
```

### Systemd

Requirements:

1. JDK 21+
2. Gradle 9.0+

Commands:

```shell
git clone https://github.com/KotatsuApp/kotatsu-syncserver.git \
  && cd kotatsu-syncserver \
  && ./gradlew shadowJar
```

Then edit file `kotatsu-sync.service`, change `replaceme` fields with your values (MySQL is used for database) and specify the `kotatsu-syncserver-0.0.1.jar` file location (it can be found in `build/libs` directory after building)

```shell
cp kotatsu-sync.service /etc/systemd/system \
  && systemctl enable kotatsu-sync \
  && systemctl daemon-reload \
  && systemctl start kotatsu-sync
```

That's all :)
For any questions, please, contact us in [Telegram group](https://t.me/kotatsuapp) or write an issue, thanks.

## License

[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)

You may copy, distribute and modify the software as long as you track changes/dates in source files. Any modifications to or software including (via compiler) GPL-licensed code must also be made available under the GPL along with build & install instructions.
