/*
Navicat MySQL Data Transfer

Source Server         : localhost-mysql
Source Server Version : 50703
Source Host           : localhost:3306
Source Database       : minireportdb

Target Server Type    : MYSQL
Target Server Version : 50703
File Encoding         : 65001

Date: 2021-10-12 15:19:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_test
-- ----------------------------
DROP TABLE IF EXISTS `t_test`;
CREATE TABLE `t_test` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `memo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test
-- ----------------------------
INSERT INTO `t_test` VALUES ('1', '张三', '1');
INSERT INTO `t_test` VALUES ('2', '李四', '2');
INSERT INTO `t_test` VALUES ('3', '王五', '3');
INSERT INTO `t_test` VALUES ('4', '赵六', '4');
INSERT INTO `t_test` VALUES ('5', null, null);
INSERT INTO `t_test` VALUES ('6', null, null);
INSERT INTO `t_test` VALUES ('7', null, null);
