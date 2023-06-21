CREATE TABLE orders (
                         id SERIAL NOT NULL PRIMARY KEY,
                         customerId INTEGER NOT NULL,
                         sellerId INTEGER NOT NULL,
                         merchandiseId INTEGER NOT NULL,
                         quantity INTEGER NOT NULL
);

CREATE TABLE merchandises (
                          id SERIAL NOT NULL PRIMARY KEY,
                          `name` VARCHAR(256) NOT NULL,
                          price DECIMAL NOT NULL,
                          seller INTEGER NOT NULL,
                          quantity INTEGER NOT NULL
);