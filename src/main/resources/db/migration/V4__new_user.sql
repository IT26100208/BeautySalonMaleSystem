CREATE TABLE `SaloonManagement`.`user` (
                                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                                           `first_name` VARCHAR(100) NOT NULL,
                                           `last_name` VARCHAR(100) NOT NULL,
                                           `email` VARCHAR(150) NOT NULL,
                                           `password` VARCHAR(255) NULL,
                                           `phone_number` VARCHAR(45) NULL,
                                           `user_role` VARCHAR(45) NULL,
                                           `employee_id` VARCHAR(45) NULL,
                                           `address` VARCHAR(255) NULL,
                                           `loyalty_number` VARCHAR(45) NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);
