# Android-MVVM-RX3-Dagger2-NavigationComponent
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)


A Tiny Mobile Application
<br>
This is a simple app which implemented using Navigation Component, MVVM architecture, Room, Retrofit2, RX3, Coil, Dagger2 and View Binding.
Also I used the free API `https://www.themealdb.com/api.php` the content of app.
IT CONTAINS A SINGLE ACTIVITY WITH A SIMPLE UI.
And the goal of sharing this code is to have a better understanding of Navigation Component basic concepts for newbies.

##### The App at a Glance:

- Recipes guide
- Step-by-step instructions
- Favorites and categories
- Search by query, tag and title of category
 
<br>
<p align="center">
  <img src="https://github.com/ZahraHeydari/Android-MVVM-RX3-Dagger2-NavComponent/blob/master/main_page.png" width="250"/>
  <img src="https://github.com/ZahraHeydari/Android-MVVM-RX3-Dagger2-NavComponent/blob/master/detail_page.png" width="250"/>
  <img src="https://github.com/ZahraHeydari/Android-MVVM-RX3-Dagger2-NavComponent/blob/master/categories_page.png" width="250"/>
</p>
<br>
<p align="center">
  <img src="https://github.com/ZahraHeydari/Android-MVVM-RX3-Dagger2-NavComponent/blob/master/favorites_page.png" width="250"/>
  <img src="https://github.com/ZahraHeydari/Android-MVVM-RX3-Dagger2-NavComponent/blob/master/search_page.png" width="250"/>
</p>
<br>
<br>

### Advantages Of Using Single Activity Architecture

 1. From the name only we can understand that only one Activity will exist in the whole architecture. So, no need to update the Manifest every time. Just once, we have to declare the Navigation graph XML file in the Manifest.
 2.  No need to declare the boilerplate method like startActivityForResult() every time as we will navigate between the fragments now and the navigation between the fragments get also simplified with the use of the Navigation Component library included in JetPack. 
 3. The transition Animation problem has also been resolved after using the fragments.
 4. As all the Fragments will be bounded inside the Activity, we can easily share the data between the fragments. 
 5. We might counter incidents like we need to pass information to the fragments. It may consist of nullable data. Manually, if we have to resolve this problem then we have to write several lines of code. But in Navigation Component, Android introduced a feature called “Safe Args Gradle Plug-in”. Builder classes will be generated from this plug-in to assure type-safe access to the arguments for the particular action.

### BENIFITS OF NAVIGATION COMPONENT

[Navigation Architecture Component](https://developer.android.com/codelabs/android-navigation#0) provides a number of benefits, including:

- Automatic handling of fragment transactions
- Correctly handling up and back by default
- Default behaviors for animations and transitions
- Deep linking as a first class operation
- Implementing navigation UI patterns (like navigation drawers and bottom nav**)** with little additional work
- Type safety when passing information while navigating
- Android Studio tooling for visualizing and editing the navigation flow of an app


### Used libraries

1. [AndroidDevelopers](https://developer.android.com/guide/navigation/navigation-getting-started#Set-up) - NavigationComponent
2. [GitHub](http://square.github.io/retrofit/) - retrofit
3. [GitHub](https://github.com/coil-kt/coil) - Coil
4. [GitHub](https://github.com/square/moshi) - Moshi
5. [GitHub](https://github.com/google/dagger) - Dagger
6. [Github](https://github.com/square/retrofit) - Retrofit
7. [Github](https://github.com/ReactiveX/RxAndroid) - RxAndroid
8. [Github](https://github.com/ReactiveX/RxJava) - RxJava


### Author

@ZARA
