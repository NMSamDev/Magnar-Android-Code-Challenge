Magnar Android Coding Challenge
==============================
This is a Card Validity Checker app for Android. It is a coding challenge for the Android developer position at Magnar.

To build the app, clone the repository and open it in Android Studio. The app is built with Gradle, so you can build it from the command line with:


`./gradlew assembleRelease`


`./gradlew assembleDebug`

The app will download a file with a credential revocation list, and check the validity of the card number you enter against the list.
The app refresh the data with a new version of the list if this one is more than 24 hours old.

The app is written in Kotlin, and uses an MVVM architecture. The app is tested with JUnit.

