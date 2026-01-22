/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : localhost:3306
 Source Schema         : msb

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 22/01/2026 22:10:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for table_user
-- ----------------------------
DROP TABLE IF EXISTS `table_user`;
CREATE TABLE `table_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `birthdate` datetime NULL DEFAULT NULL,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `filetype` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_user
-- ----------------------------
INSERT INTO `table_user` VALUES (1, 'lisan', 23, '2003-12-12 00:00:00', '0189b18f-2e1b-4628-9f6c-690896b5f6b2.png', 'image/png');
INSERT INTO `table_user` VALUES (2, 'zhangsan', 20, '2026-12-12 00:00:00', '51f00337-418a-43da-8c3c-664205d172f6.jpg', 'image/jpeg');
INSERT INTO `table_user` VALUES (3, 'zhaoliu', 21, '2005-12-12 00:00:00', 'd5dda18f-a288-4ffb-922c-d16cd8e0f573.jpg', 'image/jpeg');
INSERT INTO `table_user` VALUES (4, 'wangwu', 30, '1995-12-12 00:00:00', '28e1b25a-450d-4187-87a5-6e75d03c059a.png', 'image/png');
INSERT INTO `table_user` VALUES (5, '菲菲', 18, '2008-12-12 00:00:00', '1d034d96-950f-49c6-be4f-45e894baee3e.txt', 'text/plain');
INSERT INTO `table_user` VALUES (6, 'lihai', 20, '2006-12-12 00:00:00', 'e8a11b7a-09f8-4b64-b276-27fb72c28153.png', 'image/png');
INSERT INTO `table_user` VALUES (7, '大海', 50, '1976-12-21 00:00:00', 'cb43d12a-d052-46cc-bf17-2bdad4398e32.png', 'image/png');
INSERT INTO `table_user` VALUES (8, '刘亦菲', 20, '2006-03-09 00:00:00', 'd686e5c4-6ec5-489a-a333-3ca2a288a42f.png', 'image/png');

SET FOREIGN_KEY_CHECKS = 1;
