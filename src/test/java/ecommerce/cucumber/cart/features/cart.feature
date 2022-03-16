Feature: Cart

  Scenario: Simple Cart -- logged in, add two items and then checkout
    Given I login with email "tomsawyer@gmail.com" and password "abcd123"
    And Search for "Cotton" on the products page
    And View Product Details for "Pure Cotton Neon Green Tshirt"
    And Add to Cart
    And Search for "Blue Top" on the products page
    And View Product Details for "Blue Top"
    And Add to Cart
    And I navigate to the cart page
    And Verify 1 "Blue Top" at Rs. 500 each
    And Verify 1 "Pure Cotton Neon Green Tshirt" at Rs. 850 each
    And Proceed to Checkout
    And Verify total amount is Rs. 1350
    And Place Order
    And Enter in dummy credit card
    Then Verify Order has been placed

  Scenario: Simple Cart -- delete item and set quantity to 3
    Given I login with email "tomsawyer@gmail.com" and password "abcd123"
    And Search for "Cotton" on the products page
    And View Product Details for "Pure Cotton Neon Green Tshirt"
    And Add to Cart
    And I navigate to the cart page
    And Verify 1 "Pure Cotton Neon Green Tshirt" at Rs. 850 each
    And Remove from cart "Pure Cotton Neon Green Tshirt"
    And Verify No "Pure Cotton Neon Green Tshirt"
    And Search for "Blue Top" on the products page
    And View Product Details for "Blue Top"
    And Set quantity to 3
    And Add to Cart
    And I navigate to the cart page
    And Verify 3 "Blue Top" at Rs. 500 each
    And Proceed to Checkout
    And Verify total amount is Rs. 1500
    And Place Order
    And Enter in dummy credit card
    Then Verify Order has been placed

  Scenario: First add to cart, then login
    Given Search for "Cotton" on the products page
    And View Product Details for "Pure Cotton Neon Green Tshirt"
    And Add to Cart
    And I navigate to the cart page
    And Verify 1 "Pure Cotton Neon Green Tshirt" at Rs. 850 each
    And Proceed to Checkout
    And Click on login popup
    And I login with email "tomsawyer@gmail.com" and password "abcd123"
    And I navigate to the cart page
    And Verify 1 "Pure Cotton Neon Green Tshirt" at Rs. 850 each
    And Proceed to Checkout
    And Verify total amount is Rs. 850
    And Place Order
    And Enter in dummy credit card
    Then Verify Order has been placed

  Scenario: Negative Scenario -- Try to go to checkout when nothing in cart
    Given I login with email "tomsawyer@gmail.com" and password "abcd123"
    And I navigate to the cart page
    Then Verify checkout button does not exist

  Scenario: Negative Scenario -- Set quantity to 0 and try to checkout
    Given I login with email "tomsawyer@gmail.com" and password "abcd123"
    And Search for "Blue Top" on the products page
    And View Product Details for "Blue Top"
    And Set quantity to 0
    And Add to Cart
    And I navigate to the cart page
    Then Verify checkout button does not exist

  Scenario Outline: Add two items to cart and checkout
    Given I login with email "tomsawyer@gmail.com" and password "abcd123"
    When Search for "<item1>" on the products page
    And View Product Details for "<item1>"
    And Set quantity to <quantity1>
    And Add to Cart
    And Search for "<item2>" on the products page
    And View Product Details for "<item2>"
    And Set quantity to <quantity2>
    And Add to Cart
    And I navigate to the cart page
    And Verify <quantity1> "<item1>"
    And Verify <quantity2> "<item2>"
    And Proceed to Checkout
    And Verify total amount is Rs. <totalPrice>
    And Place Order
    And Enter in dummy credit card
    Then Verify Order has been placed

    Examples:
    | item1 | quantity1 | item2 | quantity2 | totalPrice |
    | Blue Top      | 1 | Regular Fit Straight Jeans | 1 | 1700 |
    | Blue Top      | 2 | Regular Fit Straight Jeans | 3 | 4600 |
    | Blue Top      | 2 | Regular Fit Straight Jeans | 0 | 1000 |
    | Men Tshirt    | 3 | Fancy Green Top | 1 | 1900 |