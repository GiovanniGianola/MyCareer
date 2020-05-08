# MyCareer
=============

## Summary

This application was created only for the purpose of practicing google authentication and storage services provided by firebase. the app works as a digital university book and offers 3 main features:
- allows you to enter the courses supported by specifying: name, CFU, grade (if already achieved) and date of registration;
- allows you to view a summary of all the information of the courses entered showing: number of exams taken, weighted arithmetic average, possible graduation mark interval (considering a maximum of +7 points compared to the average in 110th) and some summary graphs of the grades and average score;
- allows to check the variation of the average of the votes speculating on the credits not yet incurred.

# Screnshot
=============

Here are some screenshots of the application:

Setup steps
===========

 1. First of all you need google-services.json. Create a Firebase project in the [Firebase console](https://console.firebase.google.com/), if you don't already have one. Go to your project and click ‘Add Firebase to your Android app’. Follow the setup steps. At the end, you'll download a google-services.json file which you should add to your project.

 ![google_service_json](https://user-images.githubusercontent.com/7821425/32899277-30da3374-caf3-11e7-86e0-58cb1bfd59e2.png)

 2. Setup realtime database. In firebase console go to DEVELOP->Database-> Get Started -> choose tab ‘RULES’ and past this:

 ```
 {
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null",
    "users": {
          "$user": {
              "name": {
                  ".validate": "newData.isString() && newData.val().length < 50"
              },
              "email": {
                  ".validate": "newData.isString() && newData.val().matches(/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$/i)"
              }
          }
      }
   }
}
 ```

 3. If you haven't yet specified your app's SHA-1 fingerprint, do so from the Settings page [Settings page](https://console.firebase.google.com/project/_/settings/general/) of the Firebase console. See [Authenticating Your Client](https://developers.google.com/android/guides/client-auth) for details on how to get your app's SHA-1 fingerprint.

 4. Enable the sign in method with google. Go to DEVELOP -> Authentication -> SIGN-IN METHODS. You will see Sign-in providers. Find Google and enable it.  Here you will see Web SDK configuration. Open it and copy Web client ID and put it in the project: /app/src/main/res/values/constants.xml to “google_web_client_id” property.

 ![google_web_client_id](https://user-images.githubusercontent.com/7821425/32899597-12302680-caf4-11e7-9169-650982c0334e.png)

