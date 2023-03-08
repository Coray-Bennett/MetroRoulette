# Metro Roulette

### How to Run (Backend)

1. Obtain a WMATA API key. 
    - Navigate to https://developer.wmata.com/ and create a new account
    - Once signed in, navigate to your profile (top right)
    - Add a new subscription. This will provide your API key.
    - Using your primary key, create a new USER environment variable on your PC
    named 'metro_api_key' with the primary key as its value

#### Adding a User Environment Variable (Windows10/11)
- Press the windows key, search "env" and click the first result (edit the system environment variables)
- In the System Properties window (Advanced Tab), select "Environment Variables"
- Under User Variables, select "New"
- Variable name should be "metro_api_key" and variable value should be your primary key
- Click OK and confirm that your changes were saved


2. Install a JDK
    - If you do not already have a JDK, you can download one **here** https://www.oracle.com/java/technologies/downloads/#java19
    - Download and run the installer for Java 19 for your system

3. Running using VS Code (Recommended)
    - Download a copy of the repository onto your PC
    - Open the project in VS Code
    - Navigate to the MetrorouletteApplication.java file located in the src/main/java directory
    - Run the file using CTRL+F5

### Building an HTTP Request
- An HTTP Request for a randomly generated route can be created with the following endpoint:
    http://localhost:8080/MetroRoulette/
- The required parameters in the query string are:
    - startCode: the code of the station the route will begin with (for demonstration purposes, use A01)
    - maxStops: The maximum number of stations the route can stop at
    - maxLength: The (approximate) maximum amount of time in minutes the route should take to travel
- The optional parameter is:
    - selectedLines: List of line codes (RD, GR, YL, etc) the route can end at (end station will be on one of these lines)
- An example HTTP request:
    - http://localhost:8080/MetroRoulette/?startCode=A01&maxStops=5&selectedLines=RD,YL,OR&maxLength=60
- To list all of the stations with their station codes, use http://localhost:8080/MetroRoulette/stations (software like Insomnia might help format this https://insomnia.rest/download)
