# Metro Roulette

### Build Instructions

1. Obtain a WMATA API key. 
    - Navigate to https://developer.wmata.com/ and create a new account
    - Once signed in, navigate to your profile (top right)
    - Add a new subscription. This will provide your API key.
    - Using your primary key, update the value of 'api_key' in application.properties (located in src/main/resources).

2. Install a JDK
    - If you do not already have a JDK, you can download one **here** https://www.oracle.com/java/technologies/downloads/#java19
    - Download and run the installer for Java 19 for your system

3. Install Maven
   - Follow the steps in the link provided to install Maven: https://maven.apache.org/install.html
  
4. To build and run, execute the following command in the 'metroroulette' directory:
   - ```mvn spring-boot:run```
     

### Building an HTTP Request
- An HTTP Request for a randomly generated route can be created with the following URL:
    http://localhost:8080/MetroRoulette/
- The required parameters in the query string are:
    - startCode: the code of the station the route will begin with (for demonstration purposes, use A01)
    - maxStops: The maximum number of stations the route can stop at
    - maxLength: The (approximate) maximum amount of time in minutes the route should take to travel
- The optional parameter is:
    - selectedLines: List of line codes (RD, GR, YL, etc) the route can end at (end station will be on one of these lines)
- An example HTTP request:
    - http://localhost:8080/MetroRoulette/?startCode=A01&maxStops=5&selectedLines=RD,YL,OR&maxLength=60
- To list all of the stations with their station codes, use http://localhost:8080/MetroRoulette/stations
