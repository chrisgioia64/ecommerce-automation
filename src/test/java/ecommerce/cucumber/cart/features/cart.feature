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

