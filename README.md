# ProRata Server app #

This project currently serves as a template spring server app, build with gradle, with the following properties:

- Building with Circle CI (see circle.yml).
- Deployment with Heroku (see Procfile).

It can be forked and refactored for use as the basis of other projects.

## Developer Setup ##
This is a quickstart guide to help you get up and running with development of ProRata-Server.

### Prerequisites ###
You will need to have set up the ProRata-Database, which currently uses PostgreSQL 9.4.

- I recommend using [IntelliJ IDEA](https://download.jetbrains.com/idea/ideaIU-2016.2.4.exe) for development. 
- Import the project into IDEA, and you should be ready to go!

#### IntelliJ IDEA database connection ####
One of the benefits of IntelliJ IDEA is easy database management from the IDE. This can be achieved in the following way:

1. Select View > Tool Windows > Databases. This will pin the Databases tool window to the right hand side of the IDE main screen.
2. In the tool window, selet New (green plus symbol) > Data Source > PostgreSQL.
3. Connect to your local development database using your local ProRata database credentials. For example, the following may be used (although please be secure with your local database credentials):
  * __Host:__ localhost
  * __Database:__ ProRata
  * __User:__ postgres
  * __Password:__ postgres
  * __URL:__ jdbc:postgresql://localhost:5432/postgres
4. Ensure that you have downloaded the PostgreSQL driver JAR for use with IntelliJ IDEA. This can be achieved from within the same new database connection window, by clicking the "PostgreSQL" link, and in the new dialogue clicking the "Download" button under the "Driver files" section.
5. It's a good idea to test your connection before saving the new connection. This can be done in the credentials dialogue by using the "Test Connection" button.

## Dependencies ##
At present, this project has two dependencies which are not managed by gradle. These are as follows:

1. [Generic-SpringBoot-API](https://github.com/VGNBug/Generic-SpringBoot-API)
2. [ProRata-Model](https://github.com/VGNBug/ProRata-Model)

These should be added manually to the project dependencies.

## Deployment ##
Currently, all that is needed to deploy to Heroku is to push to master! Github will push new commits out to CircleCI, which will push successful builds to Heroku.

- The progress of the build can be seen at [the API's CircleCI dashboard](https://circleci.com/dashboard).
- The testing deployment of the API is online at [The project's Heroku dashboard](https://dashboard.heroku.com/apps).

Naturally, you will need log-in information for these sites. Please contact the project lead, Daniel Pawsey, at daniel.pawsey@live.co.uk for these.