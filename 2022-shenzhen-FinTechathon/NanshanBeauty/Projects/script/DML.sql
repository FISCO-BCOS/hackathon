-- 角色信息
INSERT INTO `role` VALUES (1, 'customer', '消费者');
INSERT INTO `role` VALUES (2, 'car', '车企');
INSERT INTO `role` VALUES (3, 'productor', '电池生产商');
INSERT INTO `role` VALUES (4, 'rent', '电池租赁商');
INSERT INTO `role` VALUES (5, 'recycle', '回收商');
INSERT INTO `role` VALUES (6, 'stored', '储能企业');
INSERT INTO `role` VALUES (7, 'safe', '安全评估');
INSERT INTO `role` VALUES (8, 'supervision', '监管机构');
INSERT INTO `role` VALUES (9, 'admin', '平台管理员');
INSERT INTO `role` VALUES (10, 'material', '原料商');
INSERT INTO `role` VALUES (11, 'audit', '企业审批角色');

-- DAO账户
INSERT INTO `user` VALUES (2, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'b6dd2a7e80df598dae042e9c79e4878a9dd3b8ca0fda2d8dbfc6ab63a62fae9a', NULL, '1', '0x3962720138d4e4906db63cc869362f5409810261', '44123', '18812356', '测试管理员', '2', '测试管理员地址');
INSERT INTO `user_role` VALUES (2, 11, 9);

-- 监管账户
INSERT INTO `user` VALUES (3, 'safe1', 'e10adc3949ba59abbe56e057f20f883e', 'ca70069f6396e9cc4edeefb3d0e5f114e76fd647bd706ed3e9c77e1094a72093', NULL, '1', '0x19a1fc072130d0c583e46290605558a20c180eec', '14455111222', '1331111', '测试安全认证机构1', '2', '随便测试地址');
INSERT INTO `user_role` VALUES (3, 10, 7);

-- 安全认证账户
INSERT INTO `user` VALUES (4, 'supervision1', 'e10adc3949ba59abbe56e057f20f883e', 'cbf69e65158c23027acbff3bf153a0ca2350e53d3e7bea1dc251a8ead1e76dd1', NULL, '1', '0x534485a14acd3b46789ecbafd625d8d21a52583b', '14455111', '1331111', '测试监管机构1', '2', '随便测试地址');
INSERT INTO `user_role` VALUES (4, 9, 8);
