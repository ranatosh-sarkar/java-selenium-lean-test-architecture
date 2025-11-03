#Author: Ranatosh Sarkar (ranatosh.qa.ie@gmail.com)
#Sample Feature Definition Template
@HomePage
Feature: As a guest perform Home page validations for Shady Meadows B&B

  Background:
    Given I am on the home page

  @smoke
  Scenario Outline: Core UI elements are visible and correct
    Then the page title should be "<expected_title>"
    And the welcome text should be "<welcome_text>"
    And the buttons should be present:
      | btn_book_now           | <btn_book_now>        |
      | btn_check_availability | <btn_check_availability> |
      | btn_single_book_now    | <btn_single_book_now> |
      | btn_double_book_now    | <btn_double_book_now> |
      | btn_suite_book_now     | <btn_suite_book_now>  |
      | btn_form_submit        | <btn_form_submit>     |

    Examples:
      | expected_title               | welcome_text                 | btn_book_now | btn_check_availability | btn_single_book_now | btn_double_book_now | btn_suite_book_now | btn_form_submit |
      | Restful-booker-platform demo | Welcome to Shady Meadows B&B | Book Now     | Check Availability     | Book Now            | Book Now            | Book Now           | Submit          |

  @nav
  Scenario Outline: Header and footer navigation sections are present
    Then the page title should be "<expected_title>"
    And the welcome text should be "<welcome_text>"
    And the header should show:
      | header_main | <header_main> |
      | rooms       | <rooms>       |
      | booking     | <booking>     |
      | amenities   | <amenities>   |
      | location    | <location>    |
      | contact     | <contact>     |
      | admin       | <admin>       |
    And the footer should show:
      | footer_brand | <footer_brand> |
      | contact_us   | <contact_us>   |
      | quick_links  | <quick_links>  |

    Examples:
      | expected_title               | welcome_text                 | header_main       | rooms | booking | amenities | location | contact | admin | footer_brand      | contact_us  | quick_links |
      #| Restful-booker-platform demo | Welcome to Shady Meadows B&B | Shady Meadows B&B | Rooms | Booking | Amenities | Location | Contact | Admin | Shady Meadows B&B | Contact Us  | Quick Links |

  @dates
  Scenario Outline: Default dates are populated
    Then the page title should be "<expected_title>"
    And the welcome text should be "<welcome_text>"
    And the default dates should be:
      | check_in  | <check_in>  |
      | check_out | <check_out> |

    Examples:
      | expected_title               | welcome_text                 | check_in | check_out |
      | Restful-booker-platform demo | Welcome to Shady Meadows B&B | today    | tomorrow  |

  @booking
  Scenario Outline: Check availability and book a single room
    Then I set the dates:
      | check_in  | <check_in>  |
      | check_out | <check_out> |
    When I click the "Check Availability" button
    And I click the "Room - Book Now" button
    Then I should be on the Single room page "<single room>" "<double room>" "<suite room>"
    And the page URL should contain "<room_url_fragment>"

    Examples:
      | check_in    | check_out | single room | double room | suite room | room_url_fragment |
      #| today       | tomorrow  | Single Room | Double Room | Suite Room | tomorrow          |
      #| tomorrow    | plus_2    | Single Room | Double Room | Suite Room | plus_2            |
      #| plus_2      | plus_5    | Single Room | Double Room | Suite Room | plus_5            |

