Feature: Product Search

  Scenario: Search for cotton
    Given I am on the products page
    Then Search for "Cotton"
    And Product Search contains product "Cotton Mull Embroidered Dress"
    And Product Search contains product "Pure Cotton V-Neck T-Shirt"
    Then Product Search does not contain product "Premium Polo T-Shirts"

  Scenario: Search for men (needs to contain "Men" in the product name,
    not just be the category "Men")
    Given I am on the products page
    Then Search for "Men"
    And Product Search contains product "Men Tshirt"
    And Product Search does not contain product "Premium Polo T-Shirts"
    Then Product Search does not contain product "Pure Cotton Neon Green Tshirt"