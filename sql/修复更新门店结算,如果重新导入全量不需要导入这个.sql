ALTER TABLE `yshop_store_revenue`
ADD COLUMN `order_id` varchar(50) NULL DEFAULT '' COMMENT '来源订单ID' AFTER `id`;
ALTER TABLE `yshop_store_revenue`
ADD COLUMN `order_type` varchar(20) NULL DEFAULT 'order' COMMENT '订单类型 order-普通订单 withdrawal提现订单' AFTER `id`