# MoneyBank  

**Please read the readme file.**  
MoneyBank is a fictive Android App development project for ESIEA Paris.

This is a project developped to improve our skills in Java development.  
*This a fake banking app and does not represent a real product, service or company.*  

To launch the app, install Android Studio, and a device with APK 33, then open the project folder.

**Developped by "arcandys" & Romain**  


## Brainstorming

We chose to develop a banking application because this type of software requires a range of complex skills.
In fact, we have implemented a login system connected to a Firebase database.  
Moreover, we provide the ability to make transfers to a third-party account. All these actions are linked to a database that updates in real-time.


## Files

We began by choosing the essential features for a banking application. During this brainstorming session, we also collaboratively created the initial use case diagram. Following this exchange of ideas, we jointly worked on various diagrams. **That's why we uploaded the same files to our respective branches.**


## Features

In the application, we developped 6 functions:  
 - A login screen, where you *have to* use credentials to access the app (the the demo, as you will see later in the readme, we chose a very simple login/password combo)  
 - A "check balance" variable that is directly linked to the Firebase Database and updates in real time  
 - A transaction system where it is possible to send money to another account, and updates the Firebase Database in real time  
 - A loan system where the user can take a loan and add money to its account  
 - A section to have information about the IBAN, RIB, and Card details  
 - A function to block the Card in case of an incident (it blocks the card and the user can no longer take a loan or send money)  
 - A function to see the history of transactions


## Instructions

To log in, please enter:  
Username: user  
Password: 1234  
**As it is designed to be a demo in mind, any other input will result in an error message.**

In the application, and as detailled above, you will be able to make the following actions:  
- Login  
- Send money to another account (handeled by Firebase Database)  
- Take a loan (handeled by Firebase Database)  
- Block the Card  
- Watch the informations about the RIB, IBAN and the Card  
- Check your balance  
- Check the history of your transactions  

We really hope this fullfill the project and are happy with the result we came with this project!  
Please reach if there is any issue with this project.  
