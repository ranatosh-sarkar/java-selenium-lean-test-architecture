#Author: Ranatosh Sarkar (ranatosh.qa.ie@gmail.com)
#Sample Feature Definition Template
@Sanity
Feature: Sanity - Perform One End to End flow

  @FlowBooking
  Scenario Outline: Check availability and book a single room
    Then I set the dates:
      | check_in  | <check_in>  |
      | check_out | <check_out> |
    When I click the "Check Availability" button
    And I click the "Room - Book Now" button
    Then I should be on the Single room page "<single room>" "<double room>" "<suite room>"
    #And the page URL should contain "<room_url_fragment>"
    Then I click the "Reserve Now" button
    And Enter my details "<firstname>" "<lastname>" "<email>" "<phone>"
    Then I verify "Booking Confirmed" message

    Examples:
      | check_in | check_out | single room | double room | suite room | room_url_fragment | firstname | lastname | email       | phone       |
      | today    | tomorrow  | Single Room | Double Room | Suite Room | tomorrow          | Rana      | Pratap   | r@gmail.com | 12345678910 | 
