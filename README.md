# Kotatsu Synchronization Server

[Kotatsu](https://github.com/KotatsuApp/Kotatsu) is a free and open source manga reader for Android platform. Supports a lot of online catalogues on different languages with filters and search, offline reading from local storage, favourites, bookmarks, new chapters notifications and more features.

### What is synchronization?
Synchronization is needed to store your collection of favorites, history and categories and have remote access to them. On a synchronized device, you can restore your manga collection in real time without loss. It also supports working across multiple devices. It is convenient for those who use several devices.

### How does synchronization work?
 - An account is created and configured in the application where it will store data;
 - Synchronization starts. The data selected by the user is saved on the service and stored there under protection;
 - Another device connects and syncs with the service;
 - The uploaded data appears on the device connected to the account.

### What data can be synchronized?
 - Favorites (with categories);
 - History.
 
### How do I sync my data?
Go to **Options** -> **Settings** -> **Content**, then select **Synchronization**. Enter your email address (even if you have not registered in the synchronization system, the authorization screen also acts as a registration screen), then come up with and enter a password. 

    ATTENTION: there is no password recovery from the account at the moment,
	if you forget the password, you will not be able to log into your account on other 
	devices, so we recommend that you do not forget the password or write it down somewhere. 

After the authorization/registration process, you will return back to the **Content** screen. To set up synchronization, select **Synchronization** again, and then you will go to system sync settings. Choose what you want to sync, history, favorites or all together, after which automatic synchronization to our server will begin.
 
### License

[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)

You may copy, distribute and modify the software as long as you track changes/dates in source files. Any modifications
to or software including (via compiler) GPL-licensed code must also be made available under the GPL along with build &
install instructions.
