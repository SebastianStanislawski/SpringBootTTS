# TTS app

Web application created with Java Spring Boot framework that allows you to conver written text into audio output. It uses [Piper](https://github.com/rhasspy/piper) and pretrained voices model exported to [onnxruntime](https://onnxruntime.ai/).

![Main View](/src/main/resources/static/others/main.png?raw=true)

## Features
- Simple permissions system: Application request user to be logged in before main content is revealed. It is based on conection with database that stores users information. 
Register Page requires user to provide the necessary information to create an account (adding to database), while te Login Page requites the user to provide existing data in the database in order to log in (reading from database).

- Text-to-speech conversion: Application's main functionality. Available for logged in users. It is based on [Piper](https://github.com/rhasspy/piper) - local neural text to speech system and uses pretrained voice models. 
To work propertly, application requires models to be placed in `/static/onnx` folder. Avaliable models are displayed to the user on web page. User types in text that need to be converted, chooses one model and generates .wav file on button click. 
After generation file is placed on the page.

## Database
Pretty much any relational database will work properly. Application was tested on local MySQL database. Connection to the database is defined in `application.properties` file and contains url to database, username and password:

```
spring.datasource.url=jdbc:mysql://localhost:3306/springbootdemo
spring.datasource.username=username
spring.datasource.password=password
```

## TO DO
Application stores generated .wav file inside `/static/output` directory. It would be reasonable to add special table to the database and storage all the previous files along with additional informations (e.g. who created them). 
It would also help with streaming generated file to the user.
