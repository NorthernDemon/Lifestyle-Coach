LifestyleCoach
==============
====eidted by david=====
SDE project 2014/2015

1. Application is done with Spring Framework, Hibernate, JPA, H2 in-memory database and Jackson

2. For JSON array list unwrapping issue I have used header parameter on the server side
    - @RequestHeader(value = "Accept") String accept
    to differentiate between wrapper for XML and collection for JSON
    Client is reading array [] for JSON and wrapper object for XML
    I made a switch JSON_UNWRAP_LIST to toggle between two conventions

3. Run:
    - *(Optional for JSON array list wrapper) in StandaloneClientLauncher.java
        JSON_UNWRAP_LIST = true (default) : DO NOT PRINT ARRAY PARENT ELEMENT (follow lab7)
        JSON_UNWRAP_LIST = false : DO PRINT ARRAY PARENT ELEMENT (follow common sense)
    - *(Optional for different server) Modify IP, PORT and SERVER_STUDENT_NAME in StandaloneClientLauncher.java
        values are picked up by both server and client, therefore after each change project needs to be recompiled
    - Apache ANT task: compile
    - Run main StandaloneServerLauncher.java
    - Run main StandaloneClientLauncher.java
    - inspect "*_result.txt" in the root folder (on the same level as "ReadMe.txt")

4. When starting the server you get exception
    - java.lang.ClassNotFoundException: org.apache.jasper.servlet.JspServlet
    Do not worry, that the situation is under control