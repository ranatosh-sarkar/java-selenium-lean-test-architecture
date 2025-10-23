#Author: Ranatosh Sarkar (ranatosh.qa.ie@gmail.com)
#Sample Feature Definition Template

Feature: Login
  As a valid user
  I want to login
  So that I can see the home page

  Scenario Outline: Successful login
    Given I am on the login page
    When I login with "<username>" and "<password>"
    Then I should land on the home page

    Examples:
      | username | password |
      | ${username} | ${password} |
