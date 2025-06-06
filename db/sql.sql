-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: shousi_picture
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category`
(
    `id`           bigint                                  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `categoryName` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
    `totalNum`     int                                              DEFAULT '0' COMMENT '分类使用次数',
    `createTime`   datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editTime`     datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `updateTime`   datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_categoryName` (`categoryName`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1905816196589150266
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK
    TABLES `category` WRITE;
/*!40000 ALTER TABLE `category`
    DISABLE KEYS */;
INSERT INTO `category`
VALUES (1, '默认', 115, '2025-03-29 16:18:37', '2025-03-29 16:18:37', '2025-04-13 15:37:54', 0),
       (1905815752248778753, '热门', 0, '2025-03-29 10:54:18', '2025-03-29 10:54:18', '2025-04-07 22:22:51', 0),
       (1905816009393168385, '搞笑', 15, '2025-03-29 10:55:19', '2025-03-29 10:55:19', '2025-04-13 20:21:18', 0),
       (1905816036106690562, '生活', 10, '2025-03-29 10:55:26', '2025-03-29 10:55:26', '2025-04-13 20:25:29', 0),
       (1905816072257396738, '高清', 1, '2025-03-29 10:55:34', '2025-03-29 10:55:34', '2025-04-06 15:36:27', 0),
       (1905816096496279553, '校园', 1, '2025-03-29 10:55:40', '2025-03-29 10:55:40', '2025-04-01 23:02:56', 0),
       (1905816125743161346, '艺术', 0, '2025-03-29 10:55:47', '2025-03-29 10:55:47', '2025-03-29 15:41:15', 0),
       (1905816162330075138, '创意', 0, '2025-03-29 10:55:56', '2025-03-29 10:55:56', '2025-03-29 10:55:56', 0),
       (1905816179300229122, '美女', 0, '2025-03-29 10:56:00', '2025-03-29 10:56:00', '2025-03-29 10:56:00', 0),
       (1905816196589150209, '帅哥', 0, '2025-03-29 10:56:04', '2025-03-29 10:56:04', '2025-03-29 10:56:04', 0),
       (1905816196589150210, '图文教程', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150211, '短视频', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150212, '长视频', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150213, '问答互动', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150214, '直播频道', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150215, '电子书', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150216, '播客节目', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150217, '行业报告', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150218, '数码家电', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150219, '服饰鞋包', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150220, '美妆个护', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150221, '食品生鲜', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150222, '家居日用', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150223, '图书音像', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150224, '运动户外', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150225, '母婴玩具', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150226, '珠宝首饰', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150227, '办公用品', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150228, '本地餐饮', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150229, '旅游服务', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150230, '家政服务', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150231, '教育培训', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150232, '医疗健康', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150233, '金融服务', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150234, '房产租售', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150235, '汽车服务', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150236, '婚庆服务', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150237, '法律服务', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150238, '手作工艺', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150239, '收藏鉴赏', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150240, '动漫二次元', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150241, '游戏电竞', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150242, '明星娱乐', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150243, '艺术创作', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150244, '植物萌宠', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150245, '户外探险', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150246, '传统文化', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150247, '科技创新', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150248, '人工智能', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150249, '区块链', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150250, '生物医药', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150251, '环境保护', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150252, '建筑工程', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150253, '机械制造', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150254, '农业养殖', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150255, '物流运输', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150256, '市场营销', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150257, '人力资源', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150258, '限时折扣', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150259, '新品首发', 1, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 20:25:43', 0),
       (1905816196589150260, '二手闲置', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150261, '公益活动', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 20:25:43', 0),
       (1905816196589150262, '政府民生', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150263, '宗教哲学', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150264, '军事历史', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0),
       (1905816196589150265, '元宇宙虚拟', 0, '2025-04-13 18:42:34', '2025-04-13 18:42:34', '2025-04-13 18:42:34', 0);
/*!40000 ALTER TABLE `category`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `picture`
(
    `id`            bigint                                  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `originUrl`     varchar(512) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '原始地址',
    `thumbnailUrl`  varchar(512) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '缩略图 url',
    `url`           varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片 url',
    `name`          varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片名称',
    `introduction`  varchar(512) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '简介',
    `picSize`       bigint                                           DEFAULT NULL COMMENT '图片体积',
    `picWidth`      int                                              DEFAULT NULL COMMENT '图片宽度',
    `picHeight`     int                                              DEFAULT NULL COMMENT '图片高度',
    `picScale`      double                                           DEFAULT NULL COMMENT '图片宽高比例',
    `picFormat`     varchar(32) COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '图片格式',
    `userId`        bigint                                  NOT NULL COMMENT '创建用户 id',
    `createTime`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editTime`      datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `updateTime`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`      tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    `reviewStatus`  int                                     NOT NULL DEFAULT '0' COMMENT '审核状态：0-待审核; 1-通过; 2-拒绝',
    `reviewMessage` varchar(512) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '审核信息',
    `reviewerId`    bigint                                           DEFAULT NULL COMMENT '审核人 ID',
    `reviewTime`    datetime                                         DEFAULT NULL COMMENT '审核时间',
    `spaceId`       bigint                                           DEFAULT NULL COMMENT '空间 id（为空表示公共空间）',
    `picColor`      varchar(16) COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '图片主色调',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_introduction` (`introduction`),
    KEY `idx_userId` (`userId`),
    KEY `idx_reviewStatus` (`reviewStatus`),
    KEY `idx_spaceId` (`spaceId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1911322894426746883
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='图片';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture`
--

LOCK
    TABLES `picture` WRITE;
/*!40000 ALTER TABLE `picture`
    DISABLE KEYS */;
INSERT INTO `picture`
VALUES (1907079632056823810,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_5Y586hjzqX6usPh3.jpg',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_5Y586hjzqX6usPh3.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_5Y586hjzqX6usPh3.webp', 'logo',
        '少女', 7716, 200, 200, 1, 'webp', 1903709990332473345, '2025-04-01 22:36:30', '2025-04-01 23:02:57',
        '2025-04-01 23:02:56', 0, 0, NULL, NULL, NULL, 1, NULL),
       (1907082316956635138,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_xzWZsfx7vqObPNDa.jpg',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_xzWZsfx7vqObPNDa.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_xzWZsfx7vqObPNDa.webp', 'logo', '',
        7716, 200, 200, 1, 'webp', 1903709990332473345, '2025-04-01 22:47:10', '2025-04-01 22:47:24',
        '2025-04-01 23:00:50', 1, 0, NULL, NULL, NULL, 1, NULL),
       (1907086353705771009,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_Xg1XrPowJv8GFUJn.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_Xg1XrPowJv8GFUJn.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-01_Xg1XrPowJv8GFUJn.webp', '雪花', '',
        5036, 300, 300, 1, 'webp', 1903709990332473345, '2025-04-01 23:03:13', '2025-04-01 23:03:24',
        '2025-04-01 23:05:43', 1, 0, NULL, NULL, NULL, 1, NULL),
       (1907822413846949889,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-03_vMeeV5334nXfZgfb.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-03_vMeeV5334nXfZgfb_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-03_vMeeV5334nXfZgfb.webp', 'shousi2', '',
        40692, 834, 902, 0.92, 'webp', 1903709990332473345, '2025-04-03 23:48:03', '2025-04-03 23:48:08',
        '2025-04-03 23:48:08', 0, 0, NULL, NULL, NULL, 1, NULL),
       (1907822493840715777,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-03_1QJ9x2wBMO7FoIKz.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-03_1QJ9x2wBMO7FoIKz_thumbnail.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-03_1QJ9x2wBMO7FoIKz.webp', '美女',
        '美女一枚', 247472, 762, 762, 1, 'webp', 1903709990332473345, '2025-04-03 23:48:22', '2025-04-03 23:48:39',
        '2025-04-03 23:48:39', 0, 0, NULL, NULL, NULL, 1, NULL),
       (1908091838676369409,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-06_1lSpcu1dv3YHpKxt.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-06_1lSpcu1dv3YHpKxt_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-06_1lSpcu1dv3YHpKxt.webp', '不是美女了',
        '纯情少女', 123396, 3048, 3048, 1, 'webp', 1903709990332473345, '2025-04-04 17:38:39', '2025-04-06 22:13:28',
        '2025-04-06 22:13:27', 0, 0, NULL, NULL, NULL, 1, '0x000000'),
       (1908092609119723521,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_WyxAXPrbuWdEL3va.jpg',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_WyxAXPrbuWdEL3va.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_WyxAXPrbuWdEL3va.webp', '热门榜单1',
        '一只小黄鸭', 7716, 200, 200, 1, 'webp', 1903709990332473345, '2025-04-04 17:41:43', '2025-04-04 17:42:01',
        '2025-04-04 22:35:07', 0, 0, NULL, NULL, NULL, 1, '0xd8be71'),
       (1908131495241900033,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_cV6EJtUmtu7dFArB.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_cV6EJtUmtu7dFArB.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_cV6EJtUmtu7dFArB.webp', '绿绿绿', '',
        1394, 474, 355, 1.34, 'webp', 1903709990332473345, '2025-04-04 20:16:14', '2025-04-04 20:16:24',
        '2025-04-04 20:16:23', 0, 0, NULL, NULL, NULL, 1, '0x008000'),
       (1908132009371295746,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_XmYqb0qvwqfcZoFP.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_XmYqb0qvwqfcZoFP_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_XmYqb0qvwqfcZoFP.webp', '浅绿色',
        '浅绿', 31512, 4961, 3543, 1.4, 'webp', 1903709990332473345, '2025-04-04 20:18:17', '2025-04-04 20:18:27',
        '2025-04-04 20:18:27', 0, 0, NULL, NULL, NULL, 1, '0x00c000'),
       (1908132194293964802,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_Wu1yMQOHvScJ33gB.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_Wu1yMQOHvScJ33gB.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_Wu1yMQOHvScJ33gB.webp', '热门榜单2',
        '红红红', 31514, 3543, 4961, 0.71, 'webp', 1903709990332473345, '2025-04-04 20:19:01', '2025-04-04 22:33:51',
        '2025-04-04 22:35:07', 0, 0, NULL, NULL, NULL, 1, '0xe00000'),
       (1908132399810666498,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_TGZFooelwgehKTFL.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_TGZFooelwgehKTFL_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1/2025-04-04_TGZFooelwgehKTFL.webp', '纯蓝色',
        '蓝蓝蓝', 4594, 1125, 2000, 0.56, 'webp', 1903709990332473345, '2025-04-04 20:19:50', '2025-04-04 20:20:04',
        '2025-04-04 20:20:04', 0, 0, NULL, NULL, NULL, 1, '0x004080'),
       (1910174209311584257,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171393268461569/2025-04-10_oDwzKpcPGODBniP0.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171393268461569/2025-04-10_oDwzKpcPGODBniP0_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171393268461569/2025-04-10_oDwzKpcPGODBniP0.webp',
        'shousi2', '小雪人', 40692, 834, 902, 0.92, 'webp', 1910171180218789889, '2025-04-10 11:33:15',
        '2025-04-10 11:09:03', '2025-04-10 19:09:04', 0, 0, NULL, NULL, NULL, 1910171393268461569, '0xe0e0e0'),
       (1910303805046566913,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_oDRC2gQrYLY6YYPi.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_oDRC2gQrYLY6YYPi_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_oDRC2gQrYLY6YYPi.webp',
        '漫威1', NULL, 25770, 474, 701, 0.68, 'webp', 1910171180218789889, '2025-04-10 20:08:13', '2025-04-10 20:08:13',
        '2025-04-10 20:08:50', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:51', NULL, '0xd3b33b'),
       (1910303810251698177,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_lhpUbIQ3vI1lpiUu.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_lhpUbIQ3vI1lpiUu_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_lhpUbIQ3vI1lpiUu.webp',
        '漫威2', NULL, 48228, 474, 351, 1.35, 'webp', 1910171180218789889, '2025-04-10 20:08:14', '2025-04-10 20:08:14',
        '2025-04-10 20:08:49', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:49', NULL, '0x314145'),
       (1910303814223704066,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_FmZ5KyG3xGz7F2yd.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_FmZ5KyG3xGz7F2yd_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_FmZ5KyG3xGz7F2yd.webp',
        '漫威3', NULL, 22388, 474, 266, 1.78, 'webp', 1910171180218789889, '2025-04-10 20:08:15', '2025-04-10 20:08:15',
        '2025-04-10 20:08:47', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:48', NULL, '0x746120'),
       (1910303818715803650,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_ZR0Nde89pXvgqiAs.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_ZR0Nde89pXvgqiAs_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_ZR0Nde89pXvgqiAs.webp',
        '漫威4', NULL, 26470, 474, 702, 0.68, 'webp', 1910171180218789889, '2025-04-10 20:08:16', '2025-04-10 20:08:16',
        '2025-04-10 20:08:46', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:46', NULL, '0x795920'),
       (1910303822427762689,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_egn6j3Y0y6OvOJJ6.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_egn6j3Y0y6OvOJJ6_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_egn6j3Y0y6OvOJJ6.webp',
        '漫威5', NULL, 15784, 400, 592, 0.68, 'webp', 1910171180218789889, '2025-04-10 20:08:17', '2025-04-10 20:08:17',
        '2025-04-10 20:08:45', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:45', NULL, '0xbd7e20'),
       (1910303826013892610,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_sNYn9EEXZVz4faqK.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_sNYn9EEXZVz4faqK.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_sNYn9EEXZVz4faqK.webp',
        '漫威6', NULL, 12670, 474, 131, 3.62, 'webp', 1910171180218789889, '2025-04-10 20:08:18', '2025-04-10 20:08:18',
        '2025-04-10 20:08:43', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:43', NULL, '0x2d4044'),
       (1910303830300471297,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_VusNdXTZB9riDnjc.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_VusNdXTZB9riDnjc_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_VusNdXTZB9riDnjc.webp',
        '漫威7', NULL, 71660, 474, 685, 0.69, 'webp', 1910171180218789889, '2025-04-10 20:08:19', '2025-04-10 20:08:19',
        '2025-04-10 20:08:41', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:42', NULL, '0x192f44'),
       (1910303833945321473,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_fkDMBc2p8HfwC0Kg.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_fkDMBc2p8HfwC0Kg_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_fkDMBc2p8HfwC0Kg.webp',
        '漫威8', NULL, 42110, 474, 266, 1.78, 'webp', 1910171180218789889, '2025-04-10 20:08:20', '2025-04-10 20:08:20',
        '2025-04-10 20:08:40', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:41', NULL, '0x8b6c30'),
       (1910303837736972289,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_eSA19sCj72alyhO3.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_eSA19sCj72alyhO3_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_eSA19sCj72alyhO3.webp',
        '漫威9', NULL, 26760, 474, 701, 0.68, 'webp', 1910171180218789889, '2025-04-10 20:08:21', '2025-04-10 20:08:21',
        '2025-04-10 20:08:39', 0, 1, '管理员操作通过', 1910171180218789889, '2025-04-10 20:08:39', NULL, '0xbf8737'),
       (1910303841520234497,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_aV7aXYNbF1LzDJFf.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_aV7aXYNbF1LzDJFf_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_aV7aXYNbF1LzDJFf.webp',
        '漫威10', NULL, 27010, 474, 266, 1.78, 'webp', 1910171180218789889, '2025-04-10 20:08:22',
        '2025-04-10 20:08:22', '2025-04-10 20:08:37', 0, 1, '管理员操作通过', 1910171180218789889,
        '2025-04-10 20:08:37', NULL, '0x1a186a'),
       (1910304059552739329,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_heUoaH8nxgjcvzCQ.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_heUoaH8nxgjcvzCQ_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_heUoaH8nxgjcvzCQ.webp',
        '四月是你的谎言1', NULL, 54428, 474, 664, 0.71, 'webp', 1910171180218789889, '2025-04-10 20:09:14',
        '2025-04-10 20:09:14', '2025-04-10 20:09:26', 0, 1, '管理员操作通过', 1910171180218789889,
        '2025-04-10 20:09:27', NULL, '0xa0dcdc'),
       (1910304063524745218,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_An9hWMAaXzYvwJ3u.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_An9hWMAaXzYvwJ3u_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_An9hWMAaXzYvwJ3u.webp',
        '四月是你的谎言2', NULL, 54494, 474, 664, 0.71, 'webp', 1910171180218789889, '2025-04-10 20:09:15',
        '2025-04-10 20:09:15', '2025-04-10 20:09:23', 0, 1, '管理员操作通过', 1910171180218789889,
        '2025-04-10 20:09:24', NULL, '0xa0dcdc'),
       (1910304067047960577,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_qI6o4eBBWnZLOhLm.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_qI6o4eBBWnZLOhLm_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_qI6o4eBBWnZLOhLm.webp',
        '四月是你的谎言3', NULL, 25484, 474, 266, 1.78, 'webp', 1910171180218789889, '2025-04-10 20:09:15',
        '2025-04-10 20:09:15', '2025-04-10 20:09:25', 0, 1, '管理员操作通过', 1910171180218789889,
        '2025-04-10 20:09:25', NULL, '0xa1dcdc'),
       (1910304069837172737,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_I4Ii9g3r78ivTqH1.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_I4Ii9g3r78ivTqH1_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_I4Ii9g3r78ivTqH1.webp',
        '四月是你的谎言4', NULL, 31546, 474, 675, 0.7, 'webp', 1910171180218789889, '2025-04-10 20:09:16',
        '2025-04-10 20:09:16', '2025-04-10 20:09:22', 0, 1, '管理员操作通过', 1910171180218789889,
        '2025-04-10 20:09:22', NULL, '0x7b97b3'),
       (1910304073435885570,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-10_SvpuYCLeWuP0Rz0S.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_SvpuYCLeWuP0Rz0S_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1910171180218789889/2025-04-10_SvpuYCLeWuP0Rz0S.webp',
        '四月是你的谎言5', NULL, 30298, 474, 266, 1.78, 'webp', 1910171180218789889, '2025-04-10 20:09:17',
        '2025-04-10 20:09:17', '2025-04-10 20:09:20', 0, 1, '管理员操作通过', 1910171180218789889,
        '2025-04-10 20:09:21', NULL, '0x936654'),
       (1910352861634617346,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171837726273537/2025-04-10_zsIOGwcR4tPmS0cC.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171837726273537/2025-04-10_zsIOGwcR4tPmS0cC.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171837726273537/2025-04-10_zsIOGwcR4tPmS0cC.webp',
        '图标', '', 1126, 300, 90, 3.33, 'webp', 1903710021580038146, '2025-04-10 23:23:09', '2025-04-10 23:40:26',
        '2025-04-10 23:40:26', 0, 0, NULL, NULL, NULL, 1910171837726273537, '0xe0e0e0'),
       (1910356792926298113,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171837726273537/2025-04-10_TbeB762oYScTNtvE.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171837726273537/2025-04-10_TbeB762oYScTNtvE_thumbnail.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910171837726273537/2025-04-10_TbeB762oYScTNtvE.webp',
        '有多美我不说', '有多美我不说', 247472, 762, 762, 1, 'webp', 1903710021580038146, '2025-04-10 23:38:46',
        '2025-04-10 23:39:01', '2025-04-10 23:39:00', 0, 0, NULL, NULL, NULL, 1910171837726273537, '0x3e3c42'),
       (1910974326331494402,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_B5dUnaKCRi6Wa01M.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_B5dUnaKCRi6Wa01M_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_B5dUnaKCRi6Wa01M.webp',
        'XqT3Nsto-psc', NULL, 14002, 400, 400, 1, 'webp', 1903710021580038146, '2025-04-12 16:32:38',
        '2025-04-12 22:47:00', '2025-04-12 22:47:00', 0, 0, NULL, NULL, NULL, 1910973767276908545, '0x44393e'),
       (1910974729236336642,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_0nq00KXYTWGxyfIc.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_0nq00KXYTWGxyfIc.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_0nq00KXYTWGxyfIc.webp',
        'r2X9jsoT-horoscope2', NULL, 5036, 300, 300, 1, 'webp', 1910676648250912770, '2025-04-12 16:34:14',
        '2025-04-12 16:34:14', '2025-04-12 16:34:14', 0, 0, NULL, NULL, NULL, 1910973767276908545, '0x4fc8e0'),
       (1910975278287507458,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_TFNvBVSPQBLjEJ8t.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_TFNvBVSPQBLjEJ8t_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_TFNvBVSPQBLjEJ8t.webp',
        'logo', '小鸡小鸡咯咯哒', 1488, 200, 200, 1, 'webp', 1903710021580038146, '2025-04-12 16:36:25',
        '2025-04-12 22:25:05', '2025-04-12 22:25:05', 0, 0, NULL, NULL, NULL, 1910973767276908545, '0xc99f62'),
       (1911019182936346626,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1911019101789147137/2025-04-12_u6JGpVqPDOdTIhkb.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1911019101789147137/2025-04-12_u6JGpVqPDOdTIhkb_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1911019101789147137/2025-04-12_u6JGpVqPDOdTIhkb.webp',
        '寿司{2}', '', 13514, 335, 345, 0.97, 'webp', 1910676648250912770, '2025-04-12 19:30:52', '2025-04-13 20:21:32',
        '2025-04-13 20:25:43', 0, 0, NULL, NULL, NULL, 1911019101789147137, '0xe0e0e0'),
       (1911058579769606146,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_oY0aW0PBDYj1jofb.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_oY0aW0PBDYj1jofb_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1910973767276908545/2025-04-12_oY0aW0PBDYj1jofb.webp',
        'shousi2', NULL, 15388, 370, 400, 0.93, 'webp', 1903710021580038146, '2025-04-12 22:07:25',
        '2025-04-12 22:08:00', '2025-04-12 22:07:59', 0, 0, NULL, NULL, NULL, 1910973767276908545, '0xab7c4d'),
       (1911058816542261250,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1907060874282024961/2025-04-13_VDq7abvZx5EURTJY.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1907060874282024961/2025-04-13_VDq7abvZx5EURTJY_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1907060874282024961/2025-04-13_VDq7abvZx5EURTJY.webp',
        'shousi2', '', 14198, 370, 400, 0.93, 'webp', 1903710021580038146, '2025-04-12 22:08:22', '2025-04-13 15:41:02',
        '2025-04-13 15:41:01', 0, 0, NULL, NULL, NULL, 1907060874282024961, '0xe0e0e0'),
       (1911067825626185729,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1907060874282024961/2025-04-12_pQVdy0ywgdR9LPb4.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1907060874282024961/2025-04-12_pQVdy0ywgdR9LPb4_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/space/1907060874282024961/2025-04-12_pQVdy0ywgdR9LPb4.webp',
        'shousi2', NULL, 40692, 834, 902, 0.92, 'webp', 1903710021580038146, '2025-04-12 22:44:10',
        '2025-04-12 22:44:10', '2025-04-12 22:44:10', 0, 0, NULL, NULL, NULL, 1907060874282024961, '0xe0e0e0'),
       (1911265430448664578,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_s5VzELr4at3rsIDG.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_s5VzELr4at3rsIDG_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_s5VzELr4at3rsIDG.webp',
        '二次元1', NULL, 55868, 474, 790, 0.6, 'webp', 1903709990332473345, '2025-04-13 11:49:22',
        '2025-04-13 11:49:22', '2025-04-13 11:49:48', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:49', NULL, '0x975e5e'),
       (1911265434089320450,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_tKwP7rEfbjfU4pwM.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_tKwP7rEfbjfU4pwM_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_tKwP7rEfbjfU4pwM.webp',
        '二次元2', NULL, 48474, 474, 948, 0.5, 'webp', 1903709990332473345, '2025-04-13 11:49:23',
        '2025-04-13 11:49:23', '2025-04-13 11:49:46', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:47', NULL, '0x786c56'),
       (1911265437004361730,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_edSm8xkQ3cz5Sco3.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_edSm8xkQ3cz5Sco3_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_edSm8xkQ3cz5Sco3.webp',
        '二次元3', NULL, 64204, 474, 842, 0.56, 'webp', 1903709990332473345, '2025-04-13 11:49:24',
        '2025-04-13 11:49:24', '2025-04-13 11:49:45', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:46', NULL, '0x902a46'),
       (1911265440875704321,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_DjLQZwwoBB8sh6wj.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_DjLQZwwoBB8sh6wj_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_DjLQZwwoBB8sh6wj.webp',
        '二次元4', NULL, 28960, 474, 669, 0.71, 'webp', 1903709990332473345, '2025-04-13 11:49:25',
        '2025-04-13 11:49:25', '2025-04-13 11:49:44', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:44', NULL, '0xceb196'),
       (1911265444398919681,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_lTVbOeDpprQPUyXp.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_lTVbOeDpprQPUyXp_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_lTVbOeDpprQPUyXp.webp',
        '二次元5', NULL, 37374, 474, 711, 0.67, 'webp', 1903709990332473345, '2025-04-13 11:49:26',
        '2025-04-13 11:49:26', '2025-04-13 11:49:42', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:42', NULL, '0xcc9fb8'),
       (1911265448098295810,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_5BRVrxTZGdFpd0N2.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_5BRVrxTZGdFpd0N2_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_5BRVrxTZGdFpd0N2.webp',
        '二次元6', NULL, 46612, 474, 842, 0.56, 'webp', 1903709990332473345, '2025-04-13 11:49:27',
        '2025-04-13 11:49:27', '2025-04-13 11:49:40', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:41', NULL, '0x35531a'),
       (1911265452061913090,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_CP82bXHaaYL0dVLj.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_CP82bXHaaYL0dVLj_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_CP82bXHaaYL0dVLj.webp',
        '二次元7', NULL, 25296, 474, 265, 1.79, 'webp', 1903709990332473345, '2025-04-13 11:49:28',
        '2025-04-13 11:49:28', '2025-04-13 11:49:39', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:40', NULL, '0xb19577'),
       (1911265455903895554,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_2MA1GAmGMZGBLO3H.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_2MA1GAmGMZGBLO3H_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_2MA1GAmGMZGBLO3H.webp',
        '二次元8', NULL, 56318, 474, 842, 0.56, 'webp', 1903709990332473345, '2025-04-13 11:49:28',
        '2025-04-13 11:49:28', '2025-04-13 11:49:38', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:38', NULL, '0x95c8cb'),
       (1911265459804598274,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_hhXVw67sZrN4KLxV.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_hhXVw67sZrN4KLxV_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_hhXVw67sZrN4KLxV.webp',
        '二次元9', NULL, 30452, 474, 829, 0.57, 'webp', 1903709990332473345, '2025-04-13 11:49:29',
        '2025-04-13 11:49:29', '2025-04-13 11:49:36', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:37', NULL, '0x4b6582'),
       (1911265463772409857,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_N3JoWaHMP4F8blRI.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_N3JoWaHMP4F8blRI_thumbnail.',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_N3JoWaHMP4F8blRI.webp',
        '二次元10', NULL, 45392, 474, 843, 0.56, 'webp', 1903709990332473345, '2025-04-13 11:49:30',
        '2025-04-13 11:49:30', '2025-04-13 11:49:35', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 11:49:36', NULL, '0xaa7488'),
       (1911268362086805505,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_BVj3zkZFLeCbLCMT.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_BVj3zkZFLeCbLCMT.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_BVj3zkZFLeCbLCMT.webp',
        'test', '', 5650, 1918, 461, 4.16, 'webp', 1903709990332473345, '2025-04-13 12:01:01', '2025-04-13 12:01:11',
        '2025-04-13 12:07:46', 0, 1, '管理员操作通过', 1903709990332473345, '2025-04-13 12:07:46', NULL, '0xe0e0e0'),
       (1911269710228385793,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_5rEtbfAp6AVT9R3Q.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_5rEtbfAp6AVT9R3Q_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_5rEtbfAp6AVT9R3Q.webp',
        '寿司', '小小寿司', 40692, 834, 902, 0.92, 'webp', 1903709990332473345, '2025-04-13 12:06:23',
        '2025-04-13 12:06:40', '2025-04-13 12:07:43', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 12:07:44', NULL, '0xe0e0e0'),
       (1911269943016452097,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_xuTfjFmxf2x5VrKz.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_xuTfjFmxf2x5VrKz_thumbnail.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_xuTfjFmxf2x5VrKz.webp',
        'XqT3Nsto-psc', NULL, 247472, 762, 762, 1, 'webp', 1903709990332473345, '2025-04-13 12:07:18',
        '2025-04-13 12:07:18', '2025-04-13 12:07:42', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 12:07:42', NULL, '0x3e3c42'),
       (1911269954529816577,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903709990332473345/2025-04-13_AdVndfBk9XLpvjPZ.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_AdVndfBk9XLpvjPZ_thumbnail.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903709990332473345/2025-04-13_AdVndfBk9XLpvjPZ.webp',
        '美女', '哇，是美女，好看好看好看好看好看好好看看好看好看好看好看', 247472, 762, 762, 1, 'webp',
        1903709990332473345, '2025-04-13 12:07:21', '2025-04-13 12:22:47', '2025-04-13 12:50:57', 0, 1,
        '管理员操作通过', 1903709990332473345, '2025-04-13 12:50:58', NULL, '0x3e3c42'),
       (1911310985145118722,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903710021580038146/2025-04-13_c8tDsAa6xg2V7Sd9.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_c8tDsAa6xg2V7Sd9_thumbnail.jfif',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_c8tDsAa6xg2V7Sd9.webp',
        '我的图片', '', 247472, 762, 762, 1, 'webp', 1903710021580038146, '2025-04-13 14:50:23', '2025-04-13 14:50:31',
        '2025-04-13 14:50:30', 0, 0, NULL, NULL, NULL, NULL, '0x3e3c42'),
       (1911322740537733122,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903710021580038146/2025-04-13_gCOztrCYGkBRWWhx.jpg',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_gCOztrCYGkBRWWhx.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_gCOztrCYGkBRWWhx.webp',
        '小黄鸡', '', 7716, 200, 200, 1, 'webp', 1903710021580038146, '2025-04-13 15:37:06', '2025-04-13 15:37:16',
        '2025-04-13 15:37:15', 0, 0, NULL, NULL, NULL, NULL, '0xd8be71'),
       (1911322884209422338,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903710021580038146/2025-04-13_lKkunX8frzohNW67.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_lKkunX8frzohNW67.webp',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_lKkunX8frzohNW67.webp',
        'gYNay1Y0-weather', NULL, 2826, 300, 300, 1, 'webp', 1903710021580038146, '2025-04-13 15:37:40',
        '2025-04-13 15:37:40', '2025-04-13 15:38:57', 0, 1, '管理员操作通过', 1903709990332473345,
        '2025-04-13 15:38:57', NULL, '0xe0e0e0'),
       (1911322894426746882,
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1903710021580038146/2025-04-13_hkTam4h92gl8mj7t.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_hkTam4h92gl8mj7t_thumbnail.png',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com/public/1903710021580038146/2025-04-13_hkTam4h92gl8mj7t.webp',
        '晴天！', '', 2858, 300, 300, 1, 'webp', 1903710021580038146, '2025-04-13 15:37:43', '2025-04-13 15:41:21',
        '2025-04-13 15:42:33', 0, 2, '不喜欢', 1903709990332473345, '2025-04-13 15:42:34', NULL, '0xe0e0e0');
/*!40000 ALTER TABLE `picture`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `picture_category`
--

DROP TABLE IF EXISTS `picture_category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `picture_category`
(
    `id`         bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `categoryId` bigint DEFAULT NULL COMMENT '分类id',
    `pictureId`  bigint NOT NULL COMMENT '图片id',
    PRIMARY KEY (`id`),
    KEY `idx_categoryId` (`categoryId`),
    KEY `idx_pictureId` (`pictureId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1911395372058615810
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='图片分类关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture_category`
--

LOCK
    TABLES `picture_category` WRITE;
/*!40000 ALTER TABLE `picture_category`
    DISABLE KEYS */;
INSERT INTO `picture_category`
VALUES (1905904559425888258, 1905816009393168385, 1905902832446427137),
       (1905905843403005953, 1905816009393168385, 1905905574011248641),
       (1905906725477720066, 1905816036106690562, 1905906462574551042),
       (1905908911251189762, 1, 1905907080919818242),
       (1905909122086268931, 1, 1905908991643414530),
       (1906180065496969217, 1, 1906180065291448321),
       (1906181505216954370, 1, 1906181505158234114),
       (1906186737397284866, 1, 1906186737342758913),
       (1906188572384313347, 1, 1906188572384313346),
       (1906189710705836035, 1, 1906189710705836034),
       (1906191635836846081, 1, 1906191635799097345),
       (1906257413441359874, 1, 1906257413261004801),
       (1906257587446255617, 1, 1906257587383341058),
       (1906257674301902850, 1, 1906257674238988289),
       (1906257678345211905, 1, 1906257678282297345),
       (1906257681927147522, 1, 1906257681927147521),
       (1906257685894959106, 1, 1906257685832044546),
       (1906257690257035267, 1, 1906257690257035266),
       (1906257693755084802, 1, 1906257693679587329),
       (1906257697232162819, 1, 1906257697232162818),
       (1906257700625354754, 1, 1906257700625354753),
       (1906257704261816322, 1, 1906257704194707458),
       (1906257707776643075, 1, 1906257707776643074),
       (1906258415670300674, 1, 1906258415670300673),
       (1906258418799251459, 1, 1906258418799251458),
       (1906258423157133314, 1, 1906258423157133313),
       (1906258426483216386, 1, 1906258426411913217),
       (1906258430392307715, 1, 1906258430392307714),
       (1906258433915523075, 1, 1906258433915523074),
       (1906258437044473858, 1, 1906258437044473857),
       (1906258440496386051, 1, 1906258440496386050),
       (1906258443436593155, 1, 1906258443436593154),
       (1906258446238388227, 1, 1906258446238388226),
       (1906279413014630401, 1, 1906279412779749378),
       (1906279417091493890, 1, 1906279416915333122),
       (1906279420266582017, 1, 1906279420207861761),
       (1906279423647191042, 1, 1906279423584276481),
       (1906279427778580481, 1, 1906279427719860226),
       (1906279431096274946, 1, 1906279431071109121),
       (1906279436980883458, 1, 1906279436955717633),
       (1906279439426162689, 1, 1906279439400996865),
       (1906279443066818562, 1, 1906279443041652737),
       (1906279445575012355, 1, 1906279445575012354),
       (1906285034518663170, 1, 1906285034459942914),
       (1906285036963942401, 1, 1906285036934582273),
       (1906285040663318529, 1, 1906285040592015362),
       (1906285042529783809, 1, 1906285042500423681),
       (1906285046082359298, 1, 1906285046082359297),
       (1906285049429413890, 1, 1906285049429413889),
       (1906285052931657730, 1, 1906285052868743169),
       (1906285055926390785, 1, 1906285055859281921),
       (1906285059508326402, 1, 1906285059478966273),
       (1906285062901518338, 1, 1906285062834409473),
       (1906285139946688513, 1, 1906285139921522689),
       (1906285142131920897, 1, 1906285142102560770),
       (1906285145571250178, 1, 1906285145537695745),
       (1906285147081199618, 1, 1906285147056033794),
       (1906285149383872514, 1, 1906285149354512386),
       (1906285153024528386, 1, 1906285152999362562),
       (1906285154043744257, 1, 1906285154014384130),
       (1906285157566959618, 1, 1906285157537599489),
       (1906285159546671106, 1, 1906285159521505281),
       (1906285161585102849, 1, 1906285161559937026),
       (1906367132625543170, 1, 1906367132239667202),
       (1906555977707077633, 1, 1906555977530916865),
       (1906556216522358787, 1, 1906556216522358786),
       (1906558285157257217, 1, 1906558285039816705),
       (1906559690949545986, 1, 1906559690920185857),
       (1906559755051094017, 1, 1906559755021733890),
       (1906697259414781953, 1, 1906697259326701569),
       (1906705071704469506, 1, 1906705071666720769),
       (1906730854917459970, 1, 1906730854888099841),
       (1906731002787647490, 1, 1906731002745704450),
       (1907086283992244227, 1905816096496279553, 1907079632056823810),
       (1907822433434349573, 1905816009393168385, 1907822413846949889),
       (1907822562161733635, 1905816036106690562, 1907822493840715777),
       (1907822771428143106, 1905816009393168385, 1907822727144681473),
       (1908131534328619010, 1905816009393168385, 1908131495241900033),
       (1908132052543266820, 1905816009393168385, 1908132009371295746),
       (1908132458400899076, 1905816036106690562, 1908132399810666498),
       (1908166444577763330, 1905815752248778753, 1908092609119723521),
       (1908166444602929155, 1905815752248778753, 1908132194293964802),
       (1908785859832889346, 1905816072257396738, 1908785781810446337),
       (1908789163375788035, 1, 1908789163375788034),
       (1908789167477817347, 1, 1908789167477817346),
       (1908789171701481475, 1, 1908789171701481474),
       (1908789175405051907, 1, 1908789175405051906),
       (1908789181579067395, 1, 1908789181579067394),
       (1908789185228111875, 1, 1908789185228111874),
       (1908789189258838018, 1, 1908789189258838017),
       (1908789192794636290, 1, 1908789192731721730),
       (1908789196301074435, 1, 1908789196301074434),
       (1908789200101113859, 1, 1908789200101113858),
       (1908885771270537217, 1905816036106690562, 1908091838676369409),
       (1908887604508540931, 1905816009393168385, 1908887158427533313),
       (1910174489386233858, 1, 1910174489348485122),
       (1910288920870948865, 1, 1910174209311584257),
       (1910289015364423681, 1, 1910195356644241409),
       (1910291569435525122, 1, 1910291544827543554),
       (1910293130698080257, 1905816036106690562, 1910293082807517186),
       (1910303805424054274, 1, 1910303805046566913),
       (1910303810251698178, 1, 1910303810251698177),
       (1910303814290812929, 1, 1910303814223704066),
       (1910303818778718209, 1, 1910303818715803650),
       (1910303822427762690, 1, 1910303822427762689),
       (1910303826085195778, 1, 1910303826013892610),
       (1910303830300471298, 1, 1910303830300471297),
       (1910303834012430338, 1, 1910303833945321473),
       (1910303837736972290, 1, 1910303837736972289),
       (1910303841537011713, 1, 1910303841520234497),
       (1910304059552739330, 1, 1910304059552739329),
       (1910304063591854081, 1, 1910304063524745218),
       (1910304067047960578, 1, 1910304067047960577),
       (1910304069837172738, 1, 1910304069837172737),
       (1910304073435885571, 1, 1910304073435885570),
       (1910356852514775042, 1905816036106690562, 1910356792926298113),
       (1910357210339266563, 1905816009393168385, 1910352861634617346),
       (1910974326406991873, 1, 1910974326331494402),
       (1910974729320222722, 1, 1910974729236336642),
       (1910975337624326146, 1905816036106690562, 1910975278287507458),
       (1911058579849297921, 1, 1911058579769606146),
       (1911058849794703363, 1905816009393168385, 1911058816542261250),
       (1911067825710071809, 1, 1911067825626185729),
       (1911265430738071553, 1, 1911265430448664578),
       (1911265434118680578, 1, 1911265434089320450),
       (1911265437033721858, 1, 1911265437004361730),
       (1911265440905064449, 1, 1911265440875704321),
       (1911265444398919682, 1, 1911265444398919681),
       (1911265448127655938, 1, 1911265448098295810),
       (1911265452061913091, 1, 1911265452061913090),
       (1911265455929061377, 1, 1911265455903895554),
       (1911265459804598275, 1, 1911265459804598274),
       (1911265463772409858, 1, 1911265463772409857),
       (1911268401483902979, 1905816009393168385, 1911268362086805505),
       (1911269781535748097, 1905816009393168385, 1911269710228385793),
       (1911269943058395138, 1, 1911269943016452097),
       (1911273834776915970, 1905816036106690562, 1911269954529816577),
       (1911311013867712513, 1905816009393168385, 1911310985145118722),
       (1911322778198388737, 1905816009393168385, 1911322740537733122),
       (1911322884238782466, 1, 1911322884209422338),
       (1911323807270875138, 1905816036106690562, 1911322894426746882),
       (1911395372058615809, 1905816196589150259, 1911019182936346626);
/*!40000 ALTER TABLE `picture_category`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `picture_tag`
--

DROP TABLE IF EXISTS `picture_tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `picture_tag`
(
    `id`        bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `tagId`     bigint DEFAULT NULL COMMENT '标签id',
    `pictureId` bigint NOT NULL COMMENT '图片id',
    PRIMARY KEY (`id`),
    KEY `idx_tagId` (`tagId`),
    KEY `idx_pictureId` (`pictureId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1911395372058615812
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='图片标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture_tag`
--

LOCK
    TABLES `picture_tag` WRITE;
/*!40000 ALTER TABLE `picture_tag`
    DISABLE KEYS */;
INSERT INTO `picture_tag`
VALUES (1905904559425888257, 1905273137617108993, 1905902832446427137),
       (1905905843340091393, 1905273080708792321, 1905905574011248641),
       (1905906725448359938, 1905273204189102081, 1905906462574551042),
       (1905908911251189761, 1, 1905907080919818242),
       (1905909122086268930, 1, 1905908991643414530),
       (1906180065555689473, 1, 1906180065291448321),
       (1906181505275674626, 1, 1906181505158234114),
       (1906186737397284867, 1, 1906186737342758913),
       (1906188572413673474, 1, 1906188572384313346),
       (1906189710705836036, 1, 1906189710705836034),
       (1906191635857817601, 1, 1906191635799097345),
       (1906257413575577601, 1, 1906257413261004801),
       (1906257587446255618, 1, 1906257587383341058),
       (1906257674301902851, 1, 1906257674238988289),
       (1906257678345211906, 1, 1906257678282297345),
       (1906257681927147523, 1, 1906257681927147521),
       (1906257685894959107, 1, 1906257685832044546),
       (1906257690357698562, 1, 1906257690257035266),
       (1906257693755084803, 1, 1906257693679587329),
       (1906257697232162820, 1, 1906257697232162818),
       (1906257700696657922, 1, 1906257700625354753),
       (1906257704261816323, 1, 1906257704194707458),
       (1906257707776643076, 1, 1906257707776643074),
       (1906258415733215234, 1, 1906258415670300673),
       (1906258418799251460, 1, 1906258418799251458),
       (1906258423157133315, 1, 1906258423157133313),
       (1906258426483216387, 1, 1906258426411913217),
       (1906258430455222274, 1, 1906258430392307714),
       (1906258433970049026, 1, 1906258433915523074),
       (1906258437111582721, 1, 1906258437044473857),
       (1906258440563494913, 1, 1906258440496386050),
       (1906258443499507714, 1, 1906258443436593154),
       (1906258446301302785, 1, 1906258446238388226),
       (1906279413039796225, 1, 1906279412779749378),
       (1906279417120854018, 1, 1906279416915333122),
       (1906279420295942146, 1, 1906279420207861761),
       (1906279423672356866, 1, 1906279423584276481),
       (1906279427807940610, 1, 1906279427719860226),
       (1906279431096274947, 1, 1906279431071109121),
       (1906279437010243586, 1, 1906279436955717633),
       (1906279439455522818, 1, 1906279439400996865),
       (1906279443100372993, 1, 1906279443041652737),
       (1906279445629538305, 1, 1906279445575012354),
       (1906285034606743553, 1, 1906285034459942914),
       (1906285036993302529, 1, 1906285036934582273),
       (1906285040663318530, 1, 1906285040592015362),
       (1906285042559143938, 1, 1906285042500423681),
       (1906285046149468162, 1, 1906285046082359297),
       (1906285049488134145, 1, 1906285049429413889),
       (1906285052931657731, 1, 1906285052868743169),
       (1906285055955750913, 1, 1906285055859281921),
       (1906285059537686529, 1, 1906285059478966273),
       (1906285062943461378, 1, 1906285062834409473),
       (1906285139976048642, 1, 1906285139921522689),
       (1906285142161281026, 1, 1906285142102560770),
       (1906285145600610306, 1, 1906285145537695745),
       (1906285147110559746, 1, 1906285147056033794),
       (1906285149413232641, 1, 1906285149354512386),
       (1906285153049694209, 1, 1906285152999362562),
       (1906285154073104386, 1, 1906285154014384130),
       (1906285157596319745, 1, 1906285157537599489),
       (1906285159576031233, 1, 1906285159521505281),
       (1906285161585102850, 1, 1906285161559937026),
       (1906367132822675457, 1, 1906367132239667202),
       (1906555977795158018, 1, 1906555977530916865),
       (1906556216585273345, 1, 1906556216522358786),
       (1906558285215977473, 1, 1906558285039816705),
       (1906559690949545987, 1, 1906559690920185857),
       (1906559755051094018, 1, 1906559755021733890),
       (1906697259473502209, 1, 1906697259326701569),
       (1906705071771578370, 1, 1906705071666720769),
       (1906730854984568834, 1, 1906730854888099841),
       (1906731002854756353, 1, 1906731002745704450),
       (1907086283992244225, 1905273137617108993, 1907079632056823810),
       (1907086283992244226, 1905273204189102081, 1907079632056823810),
       (1907822433434349570, 1905273256517238785, 1907822413846949889),
       (1907822433434349571, 1905273080708792321, 1907822413846949889),
       (1907822433434349572, 1905273137617108993, 1907822413846949889),
       (1907822562161733633, 1905273137617108993, 1907822493840715777),
       (1907822562161733634, 1905273204189102081, 1907822493840715777),
       (1907822771365228545, 1905273137617108993, 1907822727144681473),
       (1907822771365228546, 1905273241820397570, 1907822727144681473),
       (1908131534328619009, 1905273289111175170, 1908131495241900033),
       (1908132052543266817, 1905273304051286018, 1908132009371295746),
       (1908132052543266818, 1905273256517238785, 1908132009371295746),
       (1908132052543266819, 1905273289111175170, 1908132009371295746),
       (1908132458400899074, 1905273304051286018, 1908132399810666498),
       (1908132458400899075, 1905273289111175170, 1908132399810666498),
       (1908166444602929153, 1905273289111175170, 1908092609119723521),
       (1908166444602929154, 1905273080708792321, 1908092609119723521),
       (1908166444602929156, 1905273289111175170, 1908132194293964802),
       (1908166444602929157, 1905273080708792321, 1908132194293964802),
       (1908785859807723521, 1905273256517238785, 1908785781810446337),
       (1908789163463868418, 1, 1908789163375788034),
       (1908789167477817348, 1, 1908789167477817346),
       (1908789171768590338, 1, 1908789171701481474),
       (1908789175405051908, 1, 1908789175405051906),
       (1908789181662953473, 1, 1908789181579067394),
       (1908789185295220737, 1, 1908789185228111874),
       (1908789189317558274, 1, 1908789189258838017),
       (1908789192794636291, 1, 1908789192731721730),
       (1908789196301074436, 1, 1908789196301074434),
       (1908789200101113860, 1, 1908789200101113858),
       (1908885771207622657, 1905273137617108993, 1908091838676369409),
       (1908887604508540930, 1, 1908887158427533313),
       (1910174489407205377, 1, 1910174489348485122),
       (1910288920808034306, 1, 1910174209311584257),
       (1910289015301509122, 1, 1910195356644241409),
       (1910291569360027649, 1, 1910291544827543554),
       (1910293130630971393, 1905273137617108993, 1910293082807517186),
       (1910303805558272002, 1, 1910303805046566913),
       (1910303810318807042, 1, 1910303810251698177),
       (1910303814290812930, 1, 1910303814223704066),
       (1910303818778718210, 1, 1910303818715803650),
       (1910303822427762691, 1, 1910303822427762689),
       (1910303826085195779, 1, 1910303826013892610),
       (1910303830300471299, 1, 1910303830300471297),
       (1910303834012430339, 1, 1910303833945321473),
       (1910303837799886849, 1, 1910303837736972289),
       (1910303841570566146, 1, 1910303841520234497),
       (1910304059552739331, 1, 1910304059552739329),
       (1910304063591854082, 1, 1910304063524745218),
       (1910304067047960579, 1, 1910304067047960577),
       (1910304069837172739, 1, 1910304069837172737),
       (1910304073435885572, 1, 1910304073435885570),
       (1910356852447666178, 1905273289111175170, 1910356792926298113),
       (1910356852447666179, 1905273137617108993, 1910356792926298113),
       (1910357210339266561, 1905273080708792321, 1910352861634617346),
       (1910357210339266562, 1905273204189102081, 1910352861634617346),
       (1910974326469906433, 1, 1910974326331494402),
       (1910974729320222723, 1, 1910974729236336642),
       (1910975337603354625, 1905273304051286018, 1910975278287507458),
       (1910975337603354626, 1905273256517238785, 1910975278287507458),
       (1910975337611743233, 1905273080708792321, 1910975278287507458),
       (1911058579882852354, 1, 1911058579769606146),
       (1911058849794703361, 1905273304051286018, 1911058816542261250),
       (1911058849794703362, 1905273204189102081, 1911058816542261250),
       (1911067825772986369, 1, 1911067825626185729),
       (1911265430826151937, 1, 1911265430448664578),
       (1911265434148040706, 1, 1911265434089320450),
       (1911265437033721859, 1, 1911265437004361730),
       (1911265440905064450, 1, 1911265440875704321),
       (1911265444428279809, 1, 1911265444398919681),
       (1911265448127655939, 1, 1911265448098295810),
       (1911265452061913092, 1, 1911265452061913090),
       (1911265455929061378, 1, 1911265455903895554),
       (1911265459804598276, 1, 1911265459804598274),
       (1911265463839518722, 1, 1911265463772409857),
       (1911268401483902978, 1905273204189102081, 1911268362086805505),
       (1911269781518970882, 1905273080708792321, 1911269710228385793),
       (1911269781518970883, 1905273137617108993, 1911269710228385793),
       (1911269943066783746, 1, 1911269943016452097),
       (1911273834760138754, 1905273080708792321, 1911269954529816577),
       (1911273834764333058, 1905273137617108993, 1911269954529816577),
       (1911311013855129602, 1905273204189102081, 1911310985145118722),
       (1911311013855129603, 1905273289111175170, 1911310985145118722),
       (1911322778143862785, 1905273304051286018, 1911322740537733122),
       (1911322778143862786, 1905273256517238785, 1911322740537733122),
       (1911322884268142593, 1, 1911322884209422338),
       (1911323807258292225, 1905273080708792321, 1911322894426746882),
       (1911323807262486529, 1905273204189102081, 1911322894426746882),
       (1911395372058615810, 1905273401602408481, 1911019182936346626),
       (1911395372058615811, 1905273401602408482, 1911019182936346626);
/*!40000 ALTER TABLE `picture_tag`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `space`
--

DROP TABLE IF EXISTS `space`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `space`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `spaceName`  varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '空间名称',
    `spaceLevel` int                                     DEFAULT '0' COMMENT '空间级别：0-普通版 1-专业版 2-旗舰版',
    `maxSize`    bigint                                  DEFAULT '0' COMMENT '空间图片的最大总大小',
    `maxCount`   bigint                                  DEFAULT '0' COMMENT '空间图片的最大数量',
    `totalSize`  bigint                                  DEFAULT '0' COMMENT '当前空间下图片的总大小',
    `totalCount` bigint                                  DEFAULT '0' COMMENT '当前空间下的图片数量',
    `userId`     bigint   NOT NULL COMMENT '创建用户 id',
    `createTime` datetime NOT NULL                       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editTime`   datetime NOT NULL                       DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `updateTime` datetime NOT NULL                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL                       DEFAULT '0' COMMENT '是否删除',
    `spaceType`  int      NOT NULL                       DEFAULT '0' COMMENT '空间类型：0-私有 1-团队',
    PRIMARY KEY (`id`),
    KEY `idx_userId` (`userId`),
    KEY `idx_spaceName` (`spaceName`),
    KEY `idx_spaceLevel` (`spaceLevel`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1911349927739420674
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='空间';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `space`
--

LOCK
    TABLES `space` WRITE;
/*!40000 ALTER TABLE `space`
    DISABLE KEYS */;
INSERT INTO `space`
VALUES (1, '寿司的旗舰版空间', 2, 10485760000, 10000, 496006, 9, 1903709990332473345, '2025-04-01 19:04:12',
        '2025-04-01 19:04:12', '2025-04-10 11:19:55', 0, 0),
       (1907060874282024961, '寿司二号空间', 1, 1048576000, 1000, 54890, 2, 1903710021580038146, '2025-04-01 21:21:58',
        '2025-04-01 21:21:58', '2025-04-13 15:41:01', 0, 0),
       (1910171393268461569, '寿司4号的私有旗舰版空间', 2, 10485760000, 10000, 40692, 1, 1910171180218789889,
        '2025-04-10 11:22:04', '2025-04-10 11:22:04', '2025-04-10 11:33:15', 0, 0),
       (1910171837726273537, '寿司4号的团队旗舰版空间', 2, 10485760000, 10000, 289290, 3, 1910171180218789889,
        '2025-04-10 11:23:50', '2025-04-10 11:23:50', '2025-04-10 23:40:24', 0, 1),
       (1910173079852625922, '寿司5号的旗舰版空间', 2, 10485760000, 10000, 0, 0, 1910172945542623234,
        '2025-04-10 11:28:46', '2025-04-10 11:28:46', '2025-04-10 11:28:46', 0, 1),
       (1910173485232107522, '寿司3号的私有普通空间', 0, 104857600, 100, 0, 0, 1903710037518393346,
        '2025-04-10 11:30:22', '2025-04-10 11:30:22', '2025-04-10 11:32:00', 0, 1),
       (1910973767276908545, '帅帅帅', 0, 104857600, 100, 35914, 4, 1910676648250912770, '2025-04-12 16:30:24',
        '2025-04-12 16:30:24', '2025-04-12 22:47:00', 0, 1),
       (1911019101789147137, '我的', 0, 104857600, 100, 13514, 1, 1910676648250912770, '2025-04-12 19:30:33',
        '2025-04-12 19:30:33', '2025-04-12 19:31:00', 0, 0),
       (1911325071358603265, '帅帅帅', 2, 10485760000, 10000, 0, 0, 1903709990332473345, '2025-04-13 15:46:22',
        '2025-04-13 15:46:22', '2025-04-13 15:46:22', 0, 1),
       (1911325333984948226, '二号寿司', 0, 104857600, 100, 0, 0, 1903710021580038146, '2025-04-13 15:47:24',
        '2025-04-13 15:47:24', '2025-04-13 15:47:24', 0, 1),
       (1911349927739420673, '我的vip空间', 1, 1048576000, 1000, 0, 0, 1910172945542623234, '2025-04-13 17:25:08',
        '2025-04-13 17:25:08', '2025-04-13 17:25:08', 0, 0);
/*!40000 ALTER TABLE `space`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `space_user`
--

DROP TABLE IF EXISTS `space_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `space_user`
(
    `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `spaceId`      bigint   NOT NULL COMMENT '空间 id',
    `userId`       bigint   NOT NULL COMMENT '用户 id',
    `spaceRole`    varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT 'viewer' COMMENT '空间角色：viewer/editor/admin',
    `inviteStatus` tinyint  NOT NULL                       DEFAULT '0' COMMENT '邀请状态：0-待定 1-同意 2-拒绝',
    `createUserId` bigint   NOT NULL COMMENT '创建人id',
    `createTime`   datetime NOT NULL                       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime NOT NULL                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_spaceId_userId` (`spaceId`, `userId`),
    KEY `idx_spaceId` (`spaceId`),
    KEY `idx_userId` (`userId`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='空间用户关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `space_user`
--

LOCK
    TABLES `space_user` WRITE;
/*!40000 ALTER TABLE `space_user`
    DISABLE KEYS */;
INSERT INTO `space_user`
VALUES (1, 1910171837726273537, 1910171180218789889, 'admin', 1, 1910171180218789889, '2025-04-10 11:23:50',
        '2025-04-10 11:24:16'),
       (2, 1910173079852625922, 1910172945542623234, 'admin', 1, 1910172945542623234, '2025-04-10 11:28:46',
        '2025-04-10 11:28:59'),
       (3, 1910173485232107522, 1903710037518393346, 'admin', 0, 1903710037518393346, '2025-04-10 11:30:22',
        '2025-04-10 11:30:22'),
       (4, 1910171837726273537, 1903709990332473345, 'viewer', 1, 1910171180218789889, '2025-04-10 22:40:38',
        '2025-04-10 22:59:09'),
       (5, 1910171837726273537, 1903710021580038146, 'editor', 1, 1910171180218789889, '2025-04-10 23:06:59',
        '2025-04-10 23:37:14'),
       (6, 1910973767276908545, 1910676648250912770, 'admin', 1, 1910676648250912770, '2025-04-12 16:30:25',
        '2025-04-12 16:32:10'),
       (7, 1910973767276908545, 1903710021580038146, 'editor', 1, 1910676648250912770, '2025-04-12 16:31:34',
        '2025-04-12 16:37:33'),
       (8, 1911325071358603265, 1903709990332473345, 'admin', 1, 1903709990332473345, '2025-04-13 15:46:22',
        '2025-04-13 16:13:18'),
       (9, 1911325333984948226, 1903710021580038146, 'admin', 1, 1903710021580038146, '2025-04-13 15:47:24',
        '2025-04-13 16:13:18'),
       (11, 1910973767276908545, 1910172945542623234, 'viewer', 2, 1910676648250912770, '2025-04-13 18:19:46',
        '2025-04-13 18:19:46'),
       (12, 1910973767276908545, 1910171180218789889, 'viewer', 0, 1910676648250912770, '2025-04-13 18:20:12',
        '2025-04-13 18:20:12');
/*!40000 ALTER TABLE `space_user`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `tagName`    varchar(256) NOT NULL COMMENT '标签名称',
    `totalNum`   int                   DEFAULT '0' COMMENT '标签使用次数',
    `editTime`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `createTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1905273401602408571
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK
    TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag`
    DISABLE KEYS */;
INSERT INTO `tag`
VALUES (1, '默认', 116, '2025-03-29 16:18:47', '2025-03-29 16:18:47', '2025-04-13 20:25:29', 0),
       (1905273080708792321, '搞笑', 9, '2025-03-27 22:57:55', '2025-03-27 22:57:55', '2025-04-13 20:25:29', 0),
       (1905273137617108993, '美女', 10, '2025-03-27 22:58:08', '2025-03-27 22:58:08', '2025-04-13 12:22:46', 0),
       (1905273204189102081, '生活', 8, '2025-03-27 22:58:24', '2025-03-27 22:58:24', '2025-04-13 20:25:29', 0),
       (1905273222342049793, '简历', 0, '2025-03-27 22:58:29', '2025-03-27 22:58:29', '2025-03-28 09:33:11', 0),
       (1905273241820397570, '动物', 1, '2025-03-27 22:58:33', '2025-03-27 22:58:33', '2025-04-13 20:25:29', 0),
       (1905273256517238785, 'Java', 5, '2025-03-27 22:58:37', '2025-03-27 22:58:37', '2025-04-13 15:37:15', 0),
       (1905273272078106625, 'Python', 0, '2025-03-27 22:58:40', '2025-03-27 22:58:40', '2025-03-27 22:58:40', 0),
       (1905273289111175170, '科学', 7, '2025-03-27 22:58:45', '2025-03-27 22:58:45', '2025-04-13 20:21:32', 0),
       (1905273304051286018, '创意', 5, '2025-03-27 22:58:48', '2025-03-27 22:58:48', '2025-04-13 15:37:15', 0),
       (1905273318223839234, '校园', 0, '2025-03-27 22:58:51', '2025-03-27 22:58:51', '2025-03-28 10:08:13', 0),
       (1905273331603668993, '帅哥', 0, '2025-03-27 22:58:55', '2025-03-27 22:58:55', '2025-03-27 22:58:55', 0),
       (1905273359671951361, '高清', 0, '2025-03-27 22:59:01', '2025-03-27 22:59:01', '2025-03-28 09:33:11', 0),
       (1905273383810170881, '搞笑', 0, '2025-03-27 22:59:07', '2025-03-27 22:59:07', '2025-03-28 09:33:11', 0),
       (1905273401602408449, '艺术', 0, '2025-03-27 22:59:11', '2025-03-27 22:59:11', '2025-03-27 22:59:11', 0),
       (1905273401602408450, '春节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408451, '清明节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408452, '端午节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408453, '中秋节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408454, '元旦', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408455, '情人节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408456, '七夕', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408457, '圣诞节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408458, '母亲节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408459, '父亲节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408460, '儿童节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408461, '国庆节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408462, '妇女节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408463, '劳动节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408464, '万圣节', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408465, '春天', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408466, '夏天', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408467, '秋天', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408468, '冬天', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408469, '雨天', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408470, '晴天', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408471, '雪景', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408472, '台风', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408473, '雾霾', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408474, '彩虹', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408475, '美女', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408476, '帅哥', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 20:25:29', 0),
       (1905273401602408477, '萌娃', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408478, '宝妈', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408479, '奶爸', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 20:25:43', 0),
       (1905273401602408480, '学生党', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 20:25:43', 0),
       (1905273401602408481, '上班族', 1, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 20:25:43', 0),
       (1905273401602408482, '退休生活', 1, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 20:25:43', 0),
       (1905273401602408483, '单身贵族', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408484, '热恋情侣', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408485, '健身打卡', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408486, '减肥日记', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408487, '美食探店', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408488, '旅行摄影', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408489, '读书笔记', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408490, '电影推荐', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408491, '音乐分享', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408492, '宠物日常', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408493, '手工DIY', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408494, '园艺种植', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408495, '游戏攻略', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408496, '汉服文化', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408497, 'Cosplay', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408498, '夜跑族', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408499, '露营生活', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408500, '做饭教程', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408501, '收纳技巧', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408502, '理财规划', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408503, '时间管理', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408504, '穿搭指南', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408505, '急救知识', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408506, '育儿经验', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408507, '家居改造', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408508, '汽车保养', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408509, '数码测评', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408510, '火锅', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408511, '烧烤', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408512, '奶茶', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408513, '咖啡', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408514, '烘焙', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408515, '川菜', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408516, '日料', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408517, '西餐', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408518, '甜点', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408519, '素食', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408520, '酒文化', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408521, '养生茶', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408522, '街边小吃', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408523, '网红零食', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408524, '瑜伽', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408525, '篮球', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408526, '足球', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408527, '羽毛球', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408528, '游泳', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408529, '马拉松', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408530, '骑行', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408531, '滑板', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408532, '滑雪', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408533, '攀岩', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408534, '太极拳', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408535, '广场舞', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408536, '冥想', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408537, '中医养生', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408538, '考研', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408539, '公务员', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408540, '四六级', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408541, '高考', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408542, '留学申请', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408543, '职场技能', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408544, '语言学习', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408545, '编程入门', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408546, '考研英语', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408547, '教师资格证', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408548, '成人教育', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408549, '幼儿早教', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408550, '博士日常', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408551, '同学聚会', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408552, '家庭日常', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408553, '同事相处', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408554, '网友面基', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408555, '相亲故事', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408556, '闺蜜时光', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408557, '兄弟情谊', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408558, '邻里关系', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408559, '网友互动', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408560, '粉丝交流', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408561, '吃瓜群众', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408562, '表情包', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408563, '沙雕日常', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408564, '治愈系', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408565, '毒鸡汤', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408566, '凡尔赛', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408567, '社恐', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408568, '佛系青年', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408569, '躺平族', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0),
       (1905273401602408570, '内卷', 0, '2025-04-13 18:35:34', '2025-04-13 18:35:34', '2025-04-13 18:35:34', 0);
/*!40000 ALTER TABLE `tag`
    ENABLE KEYS */;
UNLOCK
    TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user`
(
    `id`            bigint                                  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `userAccount`   varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
    `userPassword`  varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `email`         varchar(256) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '用户邮箱',
    `userName`      varchar(256) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '用户昵称',
    `userAvatar`    varchar(1024) COLLATE utf8mb4_unicode_ci         DEFAULT NULL COMMENT '用户头像',
    `userProfile`   varchar(512) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '用户简介',
    `userRole`      varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin',
    `vipExpireTime` datetime                                         DEFAULT NULL COMMENT '会员过期时间',
    balance         int                                     null     DEFAULT 0 comment '积分（用于兑换vip）',
    `vipCode`       varchar(128) COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '会员兑换码',
    `vipNumber`     bigint                                           DEFAULT NULL COMMENT '会员编号',
    `shareCode`     varchar(20) COLLATE utf8mb4_unicode_ci           DEFAULT NULL COMMENT '分享码',
    `inviteUser`    bigint                                           DEFAULT NULL COMMENT '邀请用户 id',
    `editTime`      datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    `createTime`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`    datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`      tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_userAccount` (`userAccount`),
    KEY `idx_userName` (`userName`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1910676648250912771
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

create table member
(
    id         bigint                             not null primary key auto_increment comment '会员主键id',
    vipCode    varchar(64)                        not null comment '会员码',
    userId     bigint                             not null comment '用户id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    editTime   datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
) comment '会员表';

alter table member
    add index member_vipcode_index (vipCode);
--
-- Dumping data for table `user`
--

LOCK
    TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES (1903709990332473345, 'shousi1', 'cfa3fdf292d118ecd9dd6cd7ec11ce17', NULL, '寿司大大大',
        'https://shousi.oss-cn-beijing.aliyuncs.com/XqT3Nsto-psc.jfif', NULL, 'admin', NULL, NULL, NULL, NULL, NULL,
        '2025-03-23 15:26:45', '2025-03-23 15:26:45', '2025-04-10 23:07:36', 0),
       (1903710021580038146, 'shousi2', 'cfa3fdf292d118ecd9dd6cd7ec11ce17', NULL, '二号寿司',
        'https://shousi.oss-cn-beijing.aliyuncs.com/XqT3Nsto-psc.jfif', NULL, 'vip', '2026-04-13 17:03:07',
        'I7M9N2P5Q8R1S4T7', NULL, NULL, NULL, '2025-03-23 15:26:53', '2025-03-23 15:26:53', '2025-04-13 17:03:07', 0),
       (1903710037518393346, 'shousi3', 'cfa3fdf292d118ecd9dd6cd7ec11ce17', NULL, '三号寿司',
        'https://shousi.oss-cn-beijing.aliyuncs.com/XqT3Nsto-psc.jfif', NULL, 'user', NULL, NULL, NULL, NULL, NULL,
        '2025-03-23 15:26:56', '2025-03-23 15:26:56', '2025-04-10 23:07:36', 0),
       (1910171180218789889, 'shousi4', '8004be65030273ff3fc00fd9b438b11a', NULL, '寿司',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910171180218789889/2025-04-11_T0bWOrSC6v3bJ4BX.png',
        '哈哈哈', 'admin', NULL, NULL, NULL, NULL, NULL, '2025-04-10 11:21:13', '2025-04-10 11:21:13',
        '2025-04-11 13:05:06', 0),
       (1910172945542623234, 'shousi5', 'cfa3fdf292d118ecd9dd6cd7ec11ce17', NULL, '五号寿司', NULL, NULL, 'vip',
        '2026-04-13 17:23:43', 'R3T6U9V2W5X8Y1Z4', NULL, NULL, NULL, '2025-04-10 11:28:14', '2025-04-10 11:28:14',
        '2025-04-13 17:23:43', 0),
       (1910676648250912770, '1416239139', 'cfa3fdf292d118ecd9dd6cd7ec11ce17', '1416239139@qq.com', '1416239139',
        'https://shousi-1324630026.cos.ap-beijing.myqcloud.com//public/1910676648250912770/2025-04-11_FVcjLKHcDA7pNhDo.png',
        NULL, 'user', NULL, NULL, NULL, NULL, NULL, '2025-04-11 20:49:46', '2025-04-11 20:49:46', '2025-04-11 20:49:46',
        0);
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK
    TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-04-13 21:27:52
