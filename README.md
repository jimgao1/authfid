# authfid
## ./a_better_way_to_authenticate
Project on DevPost: http://devpost.com/software/authfid

This project was built at HackWestern2 (hackwestern.com), where
everyone gets 5 hours of sleep :D

This project was awarded the `Best End to End Implementation Prize` in HackWestern 2

### Introduction
AuthFID is a hardware implementation of 2 factor authencation activated using a RFID card. It provides fast and secure access to your codes, and you only need to carry a RFID card that is registered in the system. 

![Image 1](http://challengepost-s3-challengepost.netdna-ssl.com/photos/production/software_photos/000/328/335/datas/gallery.jpg)

An overview of the unit

![Image 2](http://challengepost-s3-challengepost.netdna-ssl.com/photos/production/software_photos/000/328/042/datas/gallery.jpg)

The unit shows the current 2-factor-authentication code when activated by a RFID card

### Team Members and Roles

 - **Jim Gao** (jimgao1) (Hashing algorithm, interface programming)
 - **Benjamin Cheng** (lolzballs) (Team Coordination, JNI Programming, Hardware Programming)
 - **Jacky Liao** (jackyliao123) (Hardware programming, Linux shell programming, GUI design and programming)

### Inspiration

When using 2 factor authentication, it is inconvenient to use a phone when the TOTP code is needed. 
This device is secured by RFID, and provides fast code access at your desk. 

### Implementation

We used the Raspberry Pi for majority of this project, so everything is linux based. Further
implementation information can be found below under 'Technology Used'. 

The code generation algorithm is using standard TOTP algorithm. 

### Technology Used
 - Hardware
   - Raspberry Pi (TOTP Code Generation, Display, Networking, Camera)
   - Arduino (RFID Module, Communicate with R-Pi with Serial)
   - Arduino RFID module (controlled by Arduino)
   - Raspberry Pi RF Module (GPIO)
   - Raspberry Pi Camera (Using R-Pi camera connector)
 - Software
   - Linux (Raspbian, Raspberry Pi)
   - Git (source management)
   - C (Raspberry Pi touch screen driver)
   - C++ (Java Hardware Access + Autotype program on Linux host)
   - Java (TOTP Code Gen + Swing Interface + Socket Networking)
   - Bash (Linux shell control + Simple scripting)

