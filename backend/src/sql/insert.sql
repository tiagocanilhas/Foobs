INSERT INTO foobs.Food (id, name, protein, carbohydrate, fat, fiber) VALUES
(1, 'Whole Wheat Bread', 9.0, 45.0, 4.0, 6.0),
(2, 'Egg', 13.0, 1.1, 10.5, 0.0),
(3, 'Chicken Breast', 31.0, 0.0, 3.6, 0.0),
(4, 'White Rice', 2.7, 28.0, 0.3, 0.4);

INSERT INTO foobs.FoodUnit (food_id, name, weight) VALUES
(1, 'Slice', 28.0),
(2, 'Large Unit', 55.0),
(4, 'Cup', 160.0);

INSERT INTO foobs.Meal (id, name) VALUES
(1, 'Bread with Egg'),
(2, 'Chicken with Rice');

INSERT INTO foobs.MealFood (meal_id, food_id, unit_id, quantity) VALUES
(1, 1, (SELECT id FROM foobs.FoodUnit WHERE food_id = 1 AND name = 'Slice'), 2),
(1, 2, (SELECT id FROM foobs.FoodUnit WHERE food_id = 2 AND name = 'Large Unit'), 1),
(2, 3, (SELECT id FROM foobs.FoodUnit WHERE food_id = 3 AND name = 'Grams'), 150),
(2, 4, (SELECT id FROM foobs.FoodUnit WHERE food_id = 4 AND name = 'Cup'), 1);

SELECT setval('foobs.food_id_seq', (SELECT MAX(id) FROM foobs.Food));
SELECT setval('foobs.foodunit_id_seq', (SELECT MAX(id) FROM foobs.FoodUnit));
SELECT setval('foobs.meal_id_seq', (SELECT MAX(id) FROM foobs.Meal));