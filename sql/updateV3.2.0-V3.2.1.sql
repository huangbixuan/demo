ALTER TABLE `yshop_shop_desk`
ADD COLUMN `book_status` tinyint(0) NULL DEFAULT 0 COMMENT '预约状态0-未预约 1-已预约' AFTER `last_order_status`,
ADD COLUMN `book_time` datetime(0) NULL DEFAULT NULL COMMENT '预约时间' AFTER `book_status`