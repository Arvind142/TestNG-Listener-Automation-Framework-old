# Resources folder!

### Resources folder holds 3 different Objects.
1. Environment Folder(as you can see we have Prod folder in current directory).
   1. Environment folders should have below files for proper execution.
      1. Test Data(ApplicationName.xlsx) - holds test data to be used for script execution.
      2. Test Property(ApplicationName.properties) - Holds environment related details like urls, db details,etc.
2. ApplicationLevelConfig.properties holds all global framework parameters.
3. extent-config.xml holds configuration for extent html report.

**NOTE:** application name is case-sensitive and should be same as to what you put for **ApplicationName** key from ApplicationLevelConfig.properties.
