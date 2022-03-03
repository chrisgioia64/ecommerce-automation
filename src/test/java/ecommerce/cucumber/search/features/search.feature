Feature: Product Search

  Scenario: Search for cotton
    Given I am on the products page
    When Search for "Cotton"
    And Product Search contains product "Cotton Mull Embroidered Dress"
    And Product Search contains product "Pure Cotton V-Neck T-Shirt"
    Then Product Search does not contain product "Premium Polo T-Shirts"

  Scenario: Search for men (needs to contain "Men" in the product name,
    not just be the category "Men")
    Given I am on the products page
    When Search for "Men"
    And Product Search contains product "Men Tshirt"
    And Product Search does not contain product "Premium Polo T-Shirts"
    Then Product Search does not contain product "Pure Cotton Neon Green Tshirt"

  Scenario: Search for gibberish ("foo")
    Given I am on the products page
    When Search for "Foo"
    And Product Search does not contain product "Men Tshirt"
    Then Product Search does not contain product "Premium Polo T-Shirts"

  Scenario: Search for empty string which returns everything
    Given I am on the products page
    When Search for ""
    And Product Search contains product "Blue Top"
    And Product Search contains product "Men Tshirt"
    Then Product Search contains product "Lace Top For Women"
    Then Product Search does not contain product "Some foo clothing object"