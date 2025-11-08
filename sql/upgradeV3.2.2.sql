ALTER TABLE `merchant_details`
    MODIFY COLUMN `key_cert` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'key证书,附加证书使用，如SSL证书，或者银联根级证书方面' AFTER `key_public`,
    ADD COLUMN `wechat_pay_public_key` varchar(100) NULL COMMENT '微信支付公钥文件路径' AFTER `sub_mch_id`,
    ADD COLUMN `wechat_pay_public_key_id` varchar(100) NULL COMMENT '微信支付公钥ID' AFTER `wechat_pay_public_key`,
    ADD COLUMN `private_key` varchar(200) NULL COMMENT '商户API证书私钥文件路径' AFTER `wechat_pay_public_key_id`,
    ADD COLUMN `certificate_serial_no` varchar(200) NULL COMMENT '商户API证书序列号' AFTER `private_key`;

ALTER TABLE `yshop_store_withdrawal`
    ADD COLUMN `out_bill_no` varchar(32) NULL DEFAULT '' COMMENT '提现编号唯一' AFTER `month`,
ADD COLUMN `remark` varchar(32) NULL DEFAULT '' COMMENT '备注' AFTER `out_bill_no`,
ADD COLUMN `package_info` varchar(255) NULL COMMENT '跳转领取页面的package信息' AFTER `remark`,
ADD COLUMN `state` varchar(50) NULL COMMENT 'WAIT_USER_CONFIRM1-待收款用户确认SUCCESS-成功' AFTER `remark`;



