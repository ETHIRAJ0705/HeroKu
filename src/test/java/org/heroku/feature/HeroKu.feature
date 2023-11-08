 Feature: Validating the dynamic table in Heroku application
   Scenario: Validate the table after adding the names
     Given launch the Heroku application
     When click the table data button
     And enter the data in the data field
     And click refresh table
     Then verify the table

