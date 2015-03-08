LifestyleCoach
==============

Introduction
-------

LifestyleCoach is designed to help fatty lazy guys, like [Matteo Matteovich](https://docs.google.com/document/d/1kcRTSzxkvRPT5Lp3A1eqqR51NNqHHHbmHFrjZKqs4Kc/edit?usp=sharing), to get in shape! Our solution is service oriented and opened for integration with other datasources.

####Features
    - login with Facebook account to import personal data and post to the wall.
    - login with Google account to browse the calendar and create new event.
    - create Measure, Goal and MeasureType
    - view Goal, Awareness info, Motivation and Workout tips

####Frameworks
    - Spring Framework
    - Hibernate
    - JPA
    - Jackson
    - H2 in-memory database
    - Log4J
    - Embedded Tomcat
    - JSF
    
####External datasources
    - Google Calendar
    - Facebook
    - MashApe
    - and much more!

Installation
-------
Requirements: *JDK 7*, *ANT*, *IVY*

Configure service parameters in **service.properties** file.

####Run inside of IDE
    - ant compile
    - run main StandaloneServerLauncher.java
    - (optional test) run main StandaloneClientLauncher.java
    - http://{host}:{port} (example: http://localhost:9915)
    
####Run inside of Application Server
    - ant create.war
    - deploy to Application Server
    - http://{host}:{port} (example: http://localhost:9915)

Use Case Diagram
-------
![Diagram](/diagrams/Use_Case_Diagram.png)

Entity Relationship Diagram
-------
![Diagram](/diagrams/Entity_Relationship_Diagram.png)

Architecture Diagram
-------
![Diagram](/diagrams/Architecture_Diagram.png)

Documentation
-------
Specification: https://docs.google.com/document/d/1kcRTSzxkvRPT5Lp3A1eqqR51NNqHHHbmHFrjZKqs4Kc/edit?usp=sharing
Apiary: http://docs.lifestylecoach1.apiary.io/

Authors
-------
[David Nyaika](https://github.com/DNyaika) and [Victor Ekimov](https://github.com/NorthernDemon)