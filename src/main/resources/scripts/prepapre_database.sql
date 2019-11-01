#Create database in docker container
docker run --name falcon-mysql  -v F:\mysqlData:/var/lib/mysql  -e MYSQL_ROOT_PASSWORD=password123 -p 3306:3306  -d mysql:latest
#Login to MySQL bash
docker exec -it falcon-mysql bash
#Login to MySQL
mysql --user root -p

CREATE USER 'recipe_dev_user'@'localhost' IDENTIFIED BY 'ziemniak1';
CREATE USER 'recipe_prod_user'@'localhost' IDENTIFIED BY 'ziemniak1';
CREATE USER 'recipe_dev_user'@'%' IDENTIFIED BY 'ziemniak1';
CREATE USER 'recipe_prod_user'@'%' IDENTIFIED BY 'ziemniak1';

CREATE DATABASE recipe_dev;
CREATE DATABASE recipe_prod;

GRANT SELECT ON recipe_dev.* to 'recipe_dev_user'@'localhost';
GRANT INSERT ON recipe_dev.* to 'recipe_dev_user'@'localhost';
GRANT UPDATE ON recipe_dev.* to 'recipe_dev_user'@'localhost';
GRANT DELETE ON recipe_dev.* to 'recipe_dev_user'@'localhost';

GRANT SELECT ON recipe_dev.* to 'recipe_dev_user'@'%';
GRANT INSERT ON recipe_dev.* to 'recipe_dev_user'@'%';
GRANT UPDATE ON recipe_dev.* to 'recipe_dev_user'@'%';
GRANT DELETE ON recipe_dev.* to 'recipe_dev_user'@'%';

GRANT SELECT ON recipe_prod.* to 'recipe_dev_user'@'localhost';
GRANT INSERT ON recipe_prod.* to 'recipe_dev_user'@'localhost';
GRANT UPDATE ON recipe_prod.* to 'recipe_dev_user'@'localhost';
GRANT DELETE ON recipe_prod.* to 'recipe_dev_user'@'localhost';

GRANT SELECT ON recipe_prod.* to 'recipe_dev_user'@'%';
GRANT INSERT ON recipe_prod.* to 'recipe_dev_user'@'%';
GRANT UPDATE ON recipe_prod.* to 'recipe_dev_user'@'%';
GRANT DELETE ON recipe_prod.* to 'recipe_dev_user'@'%';