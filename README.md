# 🗒 CatatanKu

CatatanKu helps to create your notes. You can edit and delete notes too. All Changes in database is pushed to Firebase Realtime Database.
App respects its Mvvm architecture. Android Architecture Components Part of Android Jetpack.
Android architecture components are a collection of libraries that help you design robust, testable, and maintainable apps.

Made in Kotlin

## Features
- Google Sign In
- Add Note
- Delete Note
- Edit Note
- Push Changes to Firebase
- Dark Mode

## Screenshots
<table>
<thead>
<tr>
<th align="center">Add Notes</th>
<th align="center">Edit Notes</th>
<th align="center">Delete Notes</th>
<th align="center">Google Sign In</th>
<th align="center">Firestore Database</th>
</tr>
</thead>
<tbody>
<tr>
<td> <img src="screenshot/add.gif" width="150" ></td>
<td> <img src="screenshot/edit.gif" width="150" ></td>
<td> <img src="screenshot/delete.gif" width="150" ></td>
<td> <img src="screenshot/Screenshot_20220103-012433_CatatanKu.jpg" width="150"</td>
<td> <img src="screenshot/firestore-database.png" width="150" ></td>

</tr>
</tbody>
</table>

## Architecture
MVVM is one of the architectural patterns which enhances separation of concerns, it allows separating the user interface logic from the business (or the back-end) logic. Its target (with other MVC patterns goal) is to achieve the following principle “Keeping UI code simple and free of app logic in order to make it easier to manage”.

- Lifecycles: It manages activity and fragment lifecycles of our app, survives configuration changes, avoids memory leaks and easily loads data into our UI.
- LiveData: It notifies views of any database changes. Use LiveData to build data objects that notify views when the underlying database changes.
- Room: It is a SQLite object mapping library. Use it to Avoid boilerplate code and easily convert SQLite table data to Java objects. Room provides compile time checks of SQLite statements and can return RxJava, Flowable and LiveData observables.
- ViewModel: It manages UI-related data in a lifecycle-conscious way. It stores UI-related data that isn't destroyed on app rotations.
- Repository: The repository depends on a persistent data model and a remote backend data source.

## Library used
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Livedata](https://developer.android.com/topic/libraries/architecture/livedata)
- [Material Library](https://material.io/develop/android/docs/getting-started/)
- [Safe Args Plugin](https://developer.android.com/guide/navigation/navigation-pass-data)
- [Firebase Authentication](https://firebase.google.com/docs/auth/?gclid=CjwKCAiAhc7yBRAdEiwAplGxXxl-B9A4ZbUFSdwERC9l-8m5z4Ln74f8gn0PN7fl0D_Ljw321cBXNhoCDwYQAvD_BwE)
- [Firebase Realtime Database](https://firebase.google.com/docs/database)