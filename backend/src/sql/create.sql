CREATE SCHEMA foobs;

CREATE TABLE foobs.Food (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    protein DOUBLE PRECISION NOT NULL,
    carbohydrate DOUBLE PRECISION NOT NULL,
    fat DOUBLE PRECISION NOT NULL,
    fiber DOUBLE PRECISION NOT NULL,
    calories DOUBLE PRECISION GENERATED ALWAYS AS (protein * 4 + carbohydrate * 4 + fat * 9) STORED
);

CREATE TABLE foobs.FoodUnit (
    id SERIAL PRIMARY KEY,
    food_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (food_id) REFERENCES foobs.Food(id) ON DELETE CASCADE
);

CREATE TABLE foobs.Meal (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(255)
);

CREATE TABLE foobs.MealFood (
    meal_id INT,
    food_id INT,
    unit_id INT,
    quantity DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (meal_id, food_id, unit_id),
    FOREIGN KEY (meal_id) REFERENCES foobs.Meal(id),
    FOREIGN KEY (food_id) REFERENCES foobs.Food(id),
    FOREIGN KEY (unit_id) REFERENCES foobs.FoodUnit(id)
);

CREATE VIEW foobs.MealDetails AS
SELECT
    m.id AS id,
    m.name AS name,
    m.url AS url,
    SUM(f.protein * (mf.quantity * COALESCE(fu.weight, 1))/ 100) AS protein,
    SUM(f.carbohydrate * (mf.quantity * COALESCE(fu.weight, 1)) / 100) AS carbohydrate,
    SUM(f.fat * (mf.quantity * COALESCE(fu.weight, 1)) / 100) AS fat,
    SUM(f.fiber * (mf.quantity * COALESCE(fu.weight, 1)) / 100) AS fiber,
    SUM(f.calories * (mf.quantity * COALESCE(fu.weight, 1)) / 100) AS calories
FROM foobs.Meal m
JOIN foobs.MealFood mf ON m.id = mf.meal_id
JOIN foobs.Food f ON mf.food_id = f.id
LEFT JOIN foobs.FoodUnit fu ON mf.unit_id = fu.id
GROUP BY m.id, m.name, m.url;



CREATE OR REPLACE FUNCTION foobs.add_default_food_unit()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO foobs.FoodUnit (food_id, name, weight)
VALUES (NEW.id, 'Grams', 1.0);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_after_food_insert
AFTER INSERT ON foobs.Food
FOR EACH ROW
EXECUTE FUNCTION foobs.add_default_food_unit();