Deliverables

Below deliverables are provided in this repository.

    1. Codebase o this assignment can be located in QRCodeScanner folder.
    2. Application APK file can be installed using the file located in APK folder.
    3. Sample valid QR codes can be located in QR Codes folders
    4. Sample invalid QR codes can be located in QR Codes folders

Steps to Compile Code

    1. Prerequisites: List of all tools and software which are mandatory for the code to run.
            a. Android Studio 3.1 needs to be installed.
            b. Java 8 needs to be installed.
    2. Download the code from this GitHub repo https://github.com/etsindiainternal/gistcommenter 
    3. Launch the Android studio 3.1 and select Import Projects
    4. Import the project codebase from QRCodeScanner folder available in point 2.
    5. Run the project using the “Run” command
    
UI Flow

    1. On launch of application the “Home Screen” will be displayed to the user.
    2. If the user does not click on the scan code icon within 3 seconds an audio voice will be played to assist the user to perform action.
    3. On clicking the scan icon user will be redirected to the “Scan QR Screen”.
    4. User can scan a valid QR code and upon successful QR scan user will be redirected the respective “Gist Info Screen”.
    5. User can view the gist information along with all the comments on this gist.
    6. User can comment on this gist using the “Leave a comment” option. The user needs to provide his GitHub login credentials as this is mandatory for commenting on any gist.
    7. On successful authentication the comment will be posted and displayed on the gist.
