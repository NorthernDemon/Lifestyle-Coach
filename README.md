LifestyleCoach
==============

Introduction
-------

SDE project 2014/2015

LifestyleCoach is designed to help fatty lazy guys, like Matteo Matteovich, to get in shape! Our solution is a web service oriented and opened for integration with other datasources.

We have designed an N-tier architecture with Client layer, Process layer, Service layer, Local and External datasources.

Application is done using Spring Framework, Hibernate, JSF, JPA, Log4J, Jackson, H2 in-memory database and embedded Tomcat. External datasource include Google, Facebook, Mashape and much more! 

User can log in with his Facebook account to import personal data and post to the wall.
User can log in with his Google account to browse the calendar and create new event.

Installation
-------
Requirements: *JDK 7*, *ANT*, *IVY*

Configure service parameters in **service.properties** file.

###To run inside of IDE:
    - ant compile
    - run main StandaloneServerLauncher.java
    - (optional test) run main StandaloneClientLauncher.java
    - http://{host}:{port} (example: http://127.0.0.1:9915)
    
###To run inside of Application Server:
    - ant create.war
    - deploy to Application Server
    - http://{host}:{port} (example: http://127.0.0.1:9915)

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
Apiary: http://docs.lifestylecoach1.apiary.io/

Authors
-------
David Nyaika and Victor Ekimov