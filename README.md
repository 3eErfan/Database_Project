# Database Project
This is our Database Course Project, a website for managing tree-cutting for a contractor
David Smit.

## Entity Relationship Diagram
Our Database design is as follows:
[![PDF Name](ERD.png)](ERD.pdf)



# Installation Guide

Follow these steps to set up your environment. Please ensure you select the correct instructions corresponding to your operating system.

## Prerequisites

1. **Java JDK**: You will need to install [Java Version 20 JDK](https://www.oracle.com/java/technologies/javase/jdk20-archive-downloads.html).

   After installation, set up the environment variable as follows:

   ### Windows

   1. Search for 'Environment Variables' in your start menu and select 'Edit the system environment variables'
   2. In the System Properties window, click on 'Environment Variables...'
   3. In the 'System variables' section, click on 'New' and add:
      - Variable name: `JAVA_HOME`
      - Variable value: `path to your JDK installation`
   4. Select 'OK' and close all remaining windows, including the System Properties window.

   > Replace 'path to your JDK installation' with the actual path where the Java JDK was installed on your machine.

2. **Apache Tomcat (Version 9)**: [Apache Tomcat v9](https://tomcat.apache.org/download-90.cgi) required to serve your web applications.

3. **MySQL**: You need [MySQL 8.0](https://dev.mysql.com/downloads/mysql/) to manage your application's database.

4. **Eclipse IDE**: This is the recommended IDE for development.

   - [Download Eclipse](https://www.eclipse.org/downloads/)

## Project Setup

1. **Download Project**: Click [here](https://github.com/3eErfan/Database_Project/archive/refs/heads/main.zip) to download the repository as a ZIP file.

2. **Import Project in Eclipse**:

   1. Open Eclipse IDE.
   2. Go to 'File > Import'.
   3. In the import wizard, select 'Existing Projects into Workspace' under the 'General' category.
   4. Click 'Next'.
   5. Click 'Select archive file' and browse to locate the downloaded ZIP file.
   6. Select the project and click 'Finish'.

You are now ready to start working on your project!

## Contributions of the two team members 
Team member1:
1.Database Design (ER Diagram): Designed the Entity-Relationship (ER) diagram, which defines the structure and relationships of the database tables. This includes defining tables like "User," "Requests," "Trees," and "Chats," as well as specifying their attributes and relationships.
2.Database Initialization: Implemented the code for initializing the database. This includes creating the necessary tables and populating the initial data.
3.User Class Implementation: Wrote the user class, which represents the attributes and behavior of a user. This class includes constructors, getter and setter methods, and SQL queries to interact with the database.
4.JSP Pages: Developed the JSP pages for user registration and login. Created the registration form and implemented the logic for users to register and log in. This includes handling form submissions, data validation, and database interactions.

Team member 2:
1.Servlets and DAO Implementation: Implemented the servlets and data access objects (DAOs) for handling user login and registration. This includes creating the userDAO class to interact with the database and validate user credentials during login.
2.Login JSP Page: Developed the JSP page for user login. Designed the login form and implemented the logic for authenticating users based on their email and password. Integrated this with the userDAO class.
3.JSP Page for Viewing Data: Created the JSP page for viewing data from the database. This includes displaying a list of users, requests, trees, and chat history. Integrated the JSTL (JavaServer Pages Standard Tag Library) for iterating through data collections and displaying information.
4.Collaboration: Worked with Team Member 1 to ensure that the database structure and data initialization aligned with the requirements of the application. This required close coordination to ensure that the data presented in the JSP pages accurately reflected what was stored in the database.

Collaborative Work:
- Integration: Collaboratively integrated the user registration and login functionality with the database. This involved ensuring that user data could be inserted into the database during registration and validated during login.
- Data Display: Collaboratively developed the JSP pages to display data from the database. This required ensuring that the JSP pages could retrieve data from the database through the userDAO and present it to the user.
- User Experience: Ensured a cohesive and user-friendly experience by working together on the front-end pages, including the registration and login forms.




