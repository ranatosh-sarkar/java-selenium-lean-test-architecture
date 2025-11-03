@SmokeTestHomePage
Feature: Perform Smoke Test of Home page validations for Shady Meadows B&B

  Background:
    Given I am on the home page

  @SmokeTestA
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

  @SmokeTestB
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
      