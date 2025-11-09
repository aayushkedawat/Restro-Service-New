# Restaurant-Menu-Service

PHASE 1:

Entity	Attributes (and Types)	Relationships
Restaurant	restaurant_id (PK), name, cuisine, city (Important for delivery check ), rating, is_open (Boolean - Critical for validation ), created_at	1 Restaurant : M Menu_Items
Menu_Item	item_id (PK), restaurant_id (FK), name, category, price, is_available (Boolean - Critical for validation )	M Menu_Items : 1 Restaurant


PHASE 2:

HTTP Method	Endpoint	Description	Used by
GET	/v1/restaurants	List all restaurants with pagination/filtering.	Client, Order Service
GET	/v1/restaurants/{id}/menu	Retrieve the full menu for a specific restaurant.	Client, Order Service
GET	/v1/restaurants/{id}	Retrieve restaurant details by ID.	Client
POST	/v1/restaurants/validate-order	Critical: Check if restaurant is open and all requested items are available/priced (Synchronous call from Order Service ).	Order Service
POST	/v1/restaurants	Admin: Create a new restaurant.	Admin/Client
PUT	/v1/menu-items/{id}	Admin: Update an item's price or availability (is_available).	Admin/Client


RULES:

Validation Check	Business Rule	
Restaurant Status	Reject order if restaurant is closed (is_open=false).	
Item Availability	Reject order if any requested item is not available (is_available=false).	
Pricing and City	Calculate total based on current prices and return restaurantCity for the Delivery Service constraint.	



Detailed API Contract for Order Validation (Synchronous):

"{
  ""restaurantId"": ""UUID_OR_ID_HERE"",
  ""items"": [
    {""itemId"": ""UUID_ITEM_A"", ""quantity"": 2},
    {""itemId"": ""UUID_ITEM_B"", ""quantity"": 1}
  ]
}"		
		
"{
  ""isValid"": true,
  ""restaurantCity"": ""Bengaluru"",
  ""calculatedItemsTotal"": 450.00, // Used by Order Service to compare totals 
  ""validatedItems"": [
    {""itemId"": ""UUID_ITEM_A"", ""price"": 150.00, ""quantity"": 2},
    {""itemId"": ""UUID_ITEM_B"", ""price"": 150.00, ""quantity"": 1}
  ]
}"		
