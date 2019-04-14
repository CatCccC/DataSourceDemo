CREATE TABLE `t_user` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `user_name` varchar(20) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `t_log` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `Flog_desc` varchar(100) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
