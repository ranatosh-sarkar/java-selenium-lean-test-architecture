Feature: Sanity - Open app and validate title

  Scenario: Application title matches expected
    Given I open the application
    Then the page title should match config
