Feature: TickerTape Page Automation
Background: Open Chrome Browser
  Given Verify Chrome Browser is Open

  @Search
  Scenario Outline: Search Company's name "<companies>"
    When Navigate to the website "https://www.tickertape.in/"
    When In the search box type company name "<companies>" navigate to it
    Then Select the period from timeline and fetch "<companies>" info
    Examples:
      | companies  |
      | RELIANCE   |
#      | TATAMOTORS |
#      | HDFCBANK   |
#      | ITC        |
#      | INDIGO     |
#      | MARUTI     |
#      | SBIN       |
#      | TCS        |
#      | BAJFINANCE |
#      | BHARTIARTL |
#      | HINDUNILVR |



