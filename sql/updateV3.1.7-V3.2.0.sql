ALTER TABLE `yshop_store_shop` 
ADD COLUMN `delivery_type` tinyint(1) NULL DEFAULT 1 COMMENT '1-自配送 2-第三方' AFTER `lat`,
ADD COLUMN `province` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '收货人所在省' AFTER `images`,
ADD COLUMN `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '收货人所在市' AFTER `province`,
ADD COLUMN `district` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '收货人所在区' AFTER `city`,
ADD COLUMN `weight` varchar(50) NULL COMMENT '物品总重量' AFTER `district`;



ALTER TABLE `yshop_express` 
ADD COLUMN `type` tinyint(1) NULL DEFAULT 0 COMMENT '0-普通 1-同城' AFTER `sort`,
ADD COLUMN `is_main` tinyint(1) NULL DEFAULT 0 COMMENT '0-否 1-是' AFTER `type`;

ALTER TABLE `yshop_store_order` 
ADD COLUMN `same_city_task_id` varchar(50) NULL DEFAULT '' COMMENT '同城配送任务ID' AFTER `order_id`,
ADD COLUMN `same_city_order_id` varchar(100) NULL DEFAULT '' COMMENT '同城配送订单ID' AFTER `same_city_task_id`,
ADD COLUMN `same_city_delivery_distance` varchar(50) NULL DEFAULT '' COMMENT '同城配送距离' AFTER `same_city_order_id`,
ADD COLUMN `same_city_delivery_time` varchar(50) NULL DEFAULT '' COMMENT '同城预计配送时间' AFTER `same_city_delivery_distance`,
ADD COLUMN `same_city_delivery_status` int(5) NULL COMMENT '同城配送订单状态' AFTER `same_city_delivery_time`,
ADD COLUMN `same_city_delivery_status_des` varchar(50) NULL COMMENT '同城配送订单状态描述' AFTER `same_city_delivery_status`,
ADD COLUMN `same_city_delivery_courier_name` varchar(50) NULL COMMENT '同城配送订单骑手名称' AFTER `same_city_delivery_status_des`,
ADD COLUMN `same_city_delivery_courier_mobile` varchar(50) NULL COMMENT '同城配送订单骑手电话' AFTER `same_city_delivery_courier_name`,
ADD COLUMN `same_city_delivery_expect_finish_time` varchar(50) NULL COMMENT '同城配送订单送达时间' AFTER `same_city_delivery_courier_mobile`;


