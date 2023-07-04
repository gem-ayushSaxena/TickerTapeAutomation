Feature: TickerTape Page Automation

  @Search
  Scenario Outline: Search Company's name
    Given I am on Chrome browser
    When Navigate to the website "https://www.tickertape.in/"
    When In the search box type company name "<companies>" navigate to it
    Then Select the period from timeline and fetch "<companies>" info
    And convert excel data to json array
    Examples:
    | companies |
    | RELIANCE |
    | TATAMOTORS |
    | HDFCBANK |
    | ITC |



