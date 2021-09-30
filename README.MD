# Testing Framework:
## Folder Structure 
 1. com.prac.framework.utils - holds **Framework** related classes and should not be edited/updated.
 2. com.prac.utils - It is an utility package which holds classes like Web.java(to work with web based automation - tool: selenium), Windows.java( to work with windows based application - tool: winium), API.java( API based automation - libraries: RestAssured) which will aid users in creation of scripts faster
 3. com.prac.**applicaitonName** here you have your main TestNG runner class which should extend TestNGBase.java from framework package and you can also have OR.java and/or BusinessFunctions.java which would be object repo / custom methods specifics to your application

## Things to Remember
1. Framework can test same set of script in different environment, so to do that we should first specify our environment name in src/test/resources/ApplicationLevelConfig.properties file and same environment folder should exist in src/test/resources and in that folder we should have our applicationName.properties(holds creds - generic creds, queries, url) and applciationName.xlsx(to hold Test Case related to application).
2. Same applicationName should be used as your package, ref FolderStrcuter->point 2.
3. You should always pass testName parameter in log which in turn should be your classname_methodName/classname_methodName_prameterName(if applicable)
4. listeners are used so it is suggested to always perform execution from testng.xml with listener specified above your test tag. 

## Release details:
[Release details Attached](https://github.com/Arvind142/Temp-Testing/blob/master/Releases.properties)





#####Quick Edit
