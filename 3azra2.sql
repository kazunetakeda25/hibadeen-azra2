-- phpMyAdmin SQL Dump
-- version 4.9.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Feb 12, 2021 at 06:06 AM
-- Server version: 5.6.49-cll-lve
-- PHP Version: 7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `3azra2`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity_logs`
--

CREATE TABLE `activity_logs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `agent_id` int(11) NOT NULL,
  `started_at` datetime DEFAULT NULL,
  `ended_at` datetime DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `activity_logs`
--

INSERT INTO `activity_logs` (`id`, `agent_id`, `started_at`, `ended_at`, `duration`, `created_at`, `updated_at`) VALUES
(8, 1, '2020-12-23 19:17:41', NULL, NULL, '2020-12-24 02:17:41', '2020-12-24 02:17:41'),
(11, 2, '2021-02-11 12:14:28', '2021-02-12 05:00:26', 1006, '2021-02-11 19:14:28', '2021-02-12 12:00:26'),
(12, 2, '2021-02-12 05:00:45', NULL, NULL, '2021-02-12 12:00:45', '2021-02-12 12:00:45');

-- --------------------------------------------------------

--
-- Table structure for table `app_agents`
--

CREATE TABLE `app_agents` (
  `id` int(10) UNSIGNED NOT NULL,
  `first_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `last_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `nick_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `birthday` date NOT NULL DEFAULT '2000-01-01',
  `gender` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `country` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `profile_image` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `availability` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `room_type` tinyint(4) NOT NULL DEFAULT '0',
  `call_type` tinyint(4) NOT NULL DEFAULT '0',
  `performance_price` int(11) NOT NULL DEFAULT '0',
  `bio` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `about` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `interests` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `audio` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `idcard_image` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `coinbalance` int(11) NOT NULL DEFAULT '0',
  `device_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `app_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `approved` tinyint(4) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `app_agents`
--

INSERT INTO `app_agents` (`id`, `first_name`, `last_name`, `nick_name`, `phone`, `email`, `password`, `birthday`, `gender`, `country`, `profile_image`, `availability`, `status`, `room_type`, `call_type`, `performance_price`, `bio`, `about`, `interests`, `audio`, `idcard_image`, `coinbalance`, `device_token`, `app_token`, `approved`, `created_at`, `updated_at`) VALUES
(1, 'nancy', 'roert', 'jery', '3495748', 'jery@gmail.com', '$2y$10$C7ptFYzF1eSugZpBRZzxsOVq5ZThVxVtWxSbhbkcl2O9Z83n4cary', '2000-01-25', 'Female', '966', '81053f211bd9c6dd108ccac30e0b2000.jpeg', 1, 1, 2, 2, 10, '', '', '', '', '', 510, '123123', 'hVUwnt4M9vSEtAizhFCt', 1, '2020-12-23 13:49:29', '2020-12-24 02:17:41'),
(2, 'agent1', 'agen1', 'agen1', '123456789', 'agen1@gmail.com', '$2y$10$7z13YDqogCwW/PUezB6J3.lcQ8QD3bOD4I.esaiOQvrSOzjY14uI2', '2021-02-25', 'Male', '1', '', 0, 1, 2, 2, 0, '', '', '', '', '', 0, '', 'SVBEaZNhRm6tWvjgYzHH', 1, '2021-02-11 16:14:59', '2021-02-12 12:07:41');

-- --------------------------------------------------------

--
-- Table structure for table `app_users`
--

CREATE TABLE `app_users` (
  `id` int(10) UNSIGNED NOT NULL,
  `first_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nick_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `profile_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `bio` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `coinbalance` int(11) NOT NULL DEFAULT '0',
  `device_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `app_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `app_users`
--

INSERT INTO `app_users` (`id`, `first_name`, `last_name`, `nick_name`, `phone`, `email`, `password`, `birthday`, `gender`, `country`, `profile_image`, `status`, `bio`, `coinbalance`, `device_token`, `app_token`, `created_at`, `updated_at`) VALUES
(1, 'tony', 'smith', 'tom', '2944883', 'tom@gmail.com', '$2y$10$q.b14QjO8nABTSaYL8HBB.2MyJZARKVYDUYn0HBrSqDV2sRLfVrNS', '2000/01/04', 'Male', '966', '1608705585rdQ.jpeg', 1, '', 54, 'cFiM7iCXQvu1mZMYXS-2F5:APA91bGRkdk1iAeXdH32tzFI8FG3pIjHKzIc7r_KalZN63tRfBNcqSIVRXy5xFdC4a1kjzwmQ8WQkAjOIylTPFQr3BuBBs8JXmog9Bb-U-asXNNPXyombeaphov8It_N1sw9rgdUzq21', 'Evvs3LpZ5ItTZTAvpgdO', '2020-12-23 13:39:12', '2020-12-26 14:51:51'),
(2, 'seo1', 'seo1', 'seo1', '123456789', 'seo1@gmail.com', '$2y$10$8Rmhw.RjwmbpgiVE0nTmmO6sbL50Cfq/tAVXO8wSW3E.ws4VhtXA2', '2021-02-24', 'Male', '1', '', 1, '', 98, 'dgEaaaM5QVW3UwOCvUUs8S:APA91bFobMtkfozOInH-UY0fmCn2R6nv6nrp3UOEc-rirlrUISuQTswRP9QG1MjvdTkx_YpWxHYEImcNhLnuXtEiOWIxlfMnwVYJKUkttZKZB4JHGmbdbGGpEUk2D_XhboTGcEJm2WzZ', 'jL3eP3gicWZ9ACY1kKQ9', '2021-02-11 16:06:21', '2021-02-12 11:55:48'),
(3, 'seo2', 'seo2', 'seo2', '123456788', 'seo2@gmail.com', '$2y$10$hbRvzh2IOqnC1vvaqAcWxeJ2N4mw9TQZXlnqqihiBb1ENisTWM5sO', '2021-02-10', 'Male', 'JP', '', 1, '', 0, 'd-OhNO4USvmxcWIu2WsyJe:APA91bEb1zXq2C2nmkbc57E8dmswKM4MlKyedKHGV4U7PoorLo0zPc0zdZQ7yle3DzUOFO7RqErMqy_qa79_whkLTMkV-Wc4Est4kYhVtXPhDiQIv_veJdWbyGWKE1p7eH6epxq9OUkJ', 'ZlDUhk3uSQNAiMZnfzOx', '2021-02-11 16:18:20', '2021-02-12 12:05:45');

-- --------------------------------------------------------

--
-- Table structure for table `calls`
--

CREATE TABLE `calls` (
  `id` int(10) UNSIGNED NOT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `room_type` int(11) DEFAULT NULL,
  `started_at` datetime DEFAULT NULL,
  `ended_at` datetime DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `pricepermin` int(11) NOT NULL DEFAULT '0',
  `totalprice` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `calls`
--

INSERT INTO `calls` (`id`, `agent_id`, `user_id`, `room_type`, `started_at`, `ended_at`, `duration`, `pricepermin`, `totalprice`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 2, '2020-12-23 14:08:11', '2020-12-23 14:08:23', 1, 10, 10, '2020-12-23 21:08:11', '2020-12-23 21:08:23'),
(2, 1, 1, 2, '2020-12-23 14:14:01', '2020-12-23 14:14:09', 1, 10, 10, '2020-12-23 21:14:01', '2020-12-23 21:14:09'),
(3, 1, 1, 2, '2020-12-23 14:14:17', '2020-12-23 14:14:24', 1, 10, 10, '2020-12-23 21:14:17', '2020-12-23 21:14:24'),
(4, 1, 1, 2, '2020-12-23 14:38:21', '2020-12-23 14:41:37', 4, 10, 40, '2020-12-23 21:38:21', '2020-12-23 21:41:37'),
(5, 1, 1, 2, '2020-12-23 14:46:26', '2020-12-23 14:47:25', 1, 10, 10, '2020-12-23 21:46:26', '2020-12-23 21:47:25'),
(6, 1, 1, 2, '2020-12-23 14:55:24', '2020-12-23 14:55:42', 1, 10, 10, '2020-12-23 21:55:24', '2020-12-23 21:55:42'),
(7, 1, 1, 2, '2020-12-23 15:01:29', '2020-12-23 15:03:28', 2, 10, 20, '2020-12-23 22:01:29', '2020-12-23 22:03:28'),
(8, 1, 1, 2, '2020-12-23 15:06:11', '2020-12-23 15:07:10', 1, 10, 10, '2020-12-23 22:06:11', '2020-12-23 22:07:10'),
(9, 1, 1, 2, '2020-12-23 15:07:28', '2020-12-23 15:07:30', 1, 10, 10, '2020-12-23 22:07:28', '2020-12-23 22:07:30'),
(10, 1, 1, 2, '2020-12-23 15:14:06', '2020-12-23 15:15:05', 1, 10, 10, '2020-12-23 22:14:06', '2020-12-23 22:15:05'),
(11, 1, 1, 2, '2020-12-23 15:54:05', '2020-12-23 15:54:08', 1, 10, 10, '2020-12-23 22:54:05', '2020-12-23 22:54:08'),
(12, 1, 1, 2, '2020-12-23 16:06:44', NULL, NULL, 10, NULL, '2020-12-23 23:06:44', '2020-12-23 23:06:44'),
(13, 1, 1, 2, '2020-12-23 16:07:42', '2020-12-23 16:07:45', 1, 10, 10, '2020-12-23 23:07:42', '2020-12-23 23:07:45'),
(14, 1, 1, 2, '2020-12-23 16:08:06', '2020-12-23 16:08:19', 1, 10, 10, '2020-12-23 23:08:06', '2020-12-23 23:08:19'),
(15, 1, 1, 2, '2020-12-23 16:09:45', '2020-12-23 16:09:53', 1, 10, 10, '2020-12-23 23:09:45', '2020-12-23 23:09:53'),
(16, 1, 1, 2, '2020-12-23 16:10:07', '2020-12-23 16:10:12', 1, 10, 10, '2020-12-23 23:10:07', '2020-12-23 23:10:12'),
(17, 1, 1, 2, '2020-12-23 16:30:22', '2020-12-23 16:31:21', 1, 10, 10, '2020-12-23 23:30:22', '2020-12-23 23:31:21'),
(18, 1, 1, 2, '2020-12-23 16:32:13', '2020-12-23 16:32:15', 1, 10, 10, '2020-12-23 23:32:13', '2020-12-23 23:32:15'),
(19, 1, 1, 2, '2020-12-23 16:47:33', '2020-12-23 16:47:36', 1, 10, 10, '2020-12-23 23:47:33', '2020-12-23 23:47:36'),
(20, 1, 1, 2, '2020-12-23 17:29:20', '2020-12-23 17:29:25', 1, 10, 10, '2020-12-24 00:29:20', '2020-12-24 00:29:25'),
(21, 1, 1, 2, '2020-12-23 17:38:20', '2020-12-23 17:39:18', 1, 10, 10, '2020-12-24 00:38:20', '2020-12-24 00:39:18'),
(22, 2, 2, 2, '2021-02-12 04:55:08', '2021-02-12 04:55:48', 1, 7, 7, '2021-02-12 11:55:08', '2021-02-12 11:55:48'),
(23, 2, 2, 2, '2021-02-12 05:03:27', '2021-02-12 05:04:11', 1, 0, 0, '2021-02-12 12:03:27', '2021-02-12 12:04:11'),
(24, 2, 3, 2, '2021-02-12 05:06:58', '2021-02-12 05:07:41', 1, 0, 0, '2021-02-12 12:06:58', '2021-02-12 12:07:41');

-- --------------------------------------------------------

--
-- Table structure for table `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `connection` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `exception` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `gallery`
--

CREATE TABLE `gallery` (
  `photo_id` int(10) UNSIGNED NOT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `file` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `gallery`
--

INSERT INTO `gallery` (`photo_id`, `agent_id`, `file`, `created_at`, `updated_at`) VALUES
(1, 1, '1608706214jM9.jpeg', '2020-12-23 13:50:14', '2020-12-23 13:50:14');

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '2014_10_12_000000_create_users_table', 1),
(2, '2014_10_12_100000_create_password_resets_table', 1),
(3, '2019_08_19_000000_create_failed_jobs_table', 1),
(4, '2020_09_02_082321_create_app_users_table', 1),
(5, '2020_09_10_110052_create_app_agents_table', 1),
(6, '2020_09_24_124123_create_public_play_table', 1),
(7, '2020_09_24_131940_create_public_visit_table', 1),
(8, '2020_10_28_124123_create_calls_table', 1),
(9, '2020_10_28_124123_create_request_agent_room_table', 1),
(10, '2020_10_29_124123_create_notification_agent_table', 1),
(11, '2020_10_29_124123_create_notification_user_table', 1),
(12, '2020_10_29_124123_create_notifications_table', 1),
(13, '2020_11_10_024309_create_reviews_table', 1),
(14, '2020_11_10_024605_create_gallery_table', 1),
(15, '2020_11_20_050517_create_activity_logs_table', 1),
(16, '2020_12_17_190525_create_payment_histories_table', 1);

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '1',
  `agent_id` int(11) NOT NULL DEFAULT '1',
  `room_type` int(11) NOT NULL DEFAULT '1',
  `callback` int(11) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notification_agent`
--

CREATE TABLE `notification_agent` (
  `id` int(10) UNSIGNED NOT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `type` enum('coingift','cointip') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'coingift',
  `title` text COLLATE utf8mb4_unicode_ci,
  `content` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `notification_agent`
--

INSERT INTO `notification_agent` (`id`, `agent_id`, `type`, `title`, `content`, `created_at`, `updated_at`) VALUES
(1, 1, 'coingift', 'Received Coin Gift', 'Received 20 coin gift from tony smith', '2020-12-23 13:53:05', '2020-12-23 13:53:05'),
(2, 1, 'coingift', 'Received Coin Gift', 'Received 10 coin gift from tony smith', '2020-12-23 14:00:39', '2020-12-23 14:00:39'),
(3, 1, 'coingift', 'Received Coin Gift', 'Received 50 coin gift from tony smith', '2020-12-23 16:26:11', '2020-12-23 16:26:11'),
(4, 1, 'coingift', 'Received Coin Gift', 'Received 100 coin gift from tony smith', '2020-12-23 17:32:21', '2020-12-23 17:32:21'),
(5, 1, 'coingift', 'Received Coin Gift', 'Received 100 coin gift from tony smith', '2020-12-23 21:36:37', '2020-12-23 21:36:37'),
(6, 1, 'coingift', 'Received Coin Gift', 'Received 100 coin gift from tony smith', '2020-12-23 21:37:33', '2020-12-23 21:37:33'),
(7, 1, 'coingift', 'Received Coin Gift', 'Received 100 coin gift from tony smith', '2020-12-23 21:37:48', '2020-12-23 21:37:48'),
(8, 1, 'coingift', 'Received Coin Gift', 'Received 30 coin gift from tony smith', '2020-12-23 21:38:01', '2020-12-23 21:38:01');

-- --------------------------------------------------------

--
-- Table structure for table `notification_user`
--

CREATE TABLE `notification_user` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `type` enum('11','22') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '11',
  `title` text COLLATE utf8mb4_unicode_ci,
  `content` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment_histories`
--

CREATE TABLE `payment_histories` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `type` enum('buycoin','coingift','cointip') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'buycoin',
  `coin` int(11) NOT NULL DEFAULT '0',
  `agent_id` int(11) NOT NULL DEFAULT '0',
  `money` double NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `payment_histories`
--

INSERT INTO `payment_histories` (`id`, `user_id`, `type`, `coin`, `agent_id`, `money`, `created_at`, `updated_at`) VALUES
(1, 1, 'buycoin', 20, 0, 18.99, '2020-12-23 13:40:57', '2020-12-23 13:40:57'),
(2, 1, 'buycoin', 20, 0, 18.99, '2020-12-23 13:44:33', '2020-12-23 13:44:33'),
(3, 1, 'coingift', 20, 1, 0, '2020-12-23 13:53:05', '2020-12-23 13:53:05'),
(4, 1, 'buycoin', 20, 0, 18.99, '2020-12-23 14:00:20', '2020-12-23 14:00:20'),
(5, 1, 'coingift', 10, 1, 0, '2020-12-23 14:00:39', '2020-12-23 14:00:39'),
(6, 1, 'buycoin', 100, 0, 59.99, '2020-12-23 16:25:41', '2020-12-23 16:25:41'),
(7, 1, 'coingift', 50, 1, 0, '2020-12-23 16:26:11', '2020-12-23 16:26:11'),
(8, 1, 'buycoin', 100, 0, 59.99, '2020-12-23 17:30:11', '2020-12-23 17:30:11'),
(9, 1, 'buycoin', 100, 0, 59.99, '2020-12-23 17:30:51', '2020-12-23 17:30:51'),
(10, 1, 'buycoin', 100, 0, 59.99, '2020-12-23 17:31:14', '2020-12-23 17:31:14'),
(11, 1, 'buycoin', 100, 0, 59.99, '2020-12-23 17:31:21', '2020-12-23 17:31:21'),
(12, 1, 'buycoin', 100, 0, 59.99, '2020-12-23 17:31:25', '2020-12-23 17:31:25'),
(13, 1, 'coingift', 100, 1, 0, '2020-12-23 17:32:21', '2020-12-23 17:32:21'),
(14, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 21:04:20', '2020-12-23 21:04:20'),
(15, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 21:06:21', '2020-12-23 21:06:21'),
(16, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 21:08:35', '2020-12-23 21:08:35'),
(17, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 21:14:40', '2020-12-23 21:14:40'),
(18, 1, 'coingift', 100, 1, 0, '2020-12-23 21:36:37', '2020-12-23 21:36:37'),
(19, 1, 'coingift', 100, 1, 0, '2020-12-23 21:37:33', '2020-12-23 21:37:33'),
(20, 1, 'coingift', 100, 1, 0, '2020-12-23 21:37:48', '2020-12-23 21:37:48'),
(21, 1, 'coingift', 30, 1, 0, '2020-12-23 21:38:01', '2020-12-23 21:38:01'),
(22, 1, 'buycoin', 30, 0, 27.99, '2020-12-23 21:45:41', '2020-12-23 21:45:41'),
(23, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 21:48:56', '2020-12-23 21:48:56'),
(24, 1, 'buycoin', 10, 0, 9.99, '2020-12-23 21:55:10', '2020-12-23 21:55:10'),
(25, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 21:56:10', '2020-12-23 21:56:10'),
(26, 1, 'buycoin', 10, 0, 9.99, '2020-12-23 22:01:07', '2020-12-23 22:01:07'),
(27, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 22:03:42', '2020-12-23 22:03:42'),
(28, 1, 'buycoin', 20, 0, 18.99, '2020-12-23 22:05:16', '2020-12-23 22:05:16'),
(29, 1, 'buycoin', 1, 0, 0.99, '2020-12-23 22:07:45', '2020-12-23 22:07:45'),
(30, 1, 'buycoin', 20, 0, 18.99, '2020-12-23 22:13:53', '2020-12-23 22:13:53'),
(31, 1, 'buycoin', 10, 0, 9.99, '2020-12-23 22:53:44', '2020-12-23 22:53:44'),
(32, 1, 'buycoin', 10, 0, 9.99, '2020-12-23 22:53:52', '2020-12-23 22:53:52'),
(33, 1, 'buycoin', 20, 0, 18.99, '2020-12-23 23:07:56', '2020-12-23 23:07:56'),
(34, 1, 'buycoin', 20, 0, 18.99, '2020-12-23 23:30:06', '2020-12-23 23:30:06'),
(35, 1, 'buycoin', 10, 0, 9.99, '2020-12-23 23:32:01', '2020-12-23 23:32:01'),
(36, 1, 'buycoin', 20, 0, 18.99, '2020-12-24 00:29:01', '2020-12-24 00:29:01'),
(37, 1, 'buycoin', 10, 0, 9.99, '2020-12-24 00:38:07', '2020-12-24 00:38:07'),
(38, 1, 'buycoin', 50, 0, 35.99, '2020-12-26 14:38:14', '2020-12-26 14:38:14'),
(39, 2, 'buycoin', 5, 0, 4.99, '2021-02-11 16:21:06', '2021-02-11 16:21:06'),
(40, 2, 'buycoin', 100, 0, 59.99, '2021-02-11 16:21:58', '2021-02-11 16:21:58');

-- --------------------------------------------------------

--
-- Table structure for table `public_play`
--

CREATE TABLE `public_play` (
  `id` int(10) UNSIGNED NOT NULL,
  `channel` text COLLATE utf8mb4_unicode_ci,
  `agent_id` int(11) DEFAULT NULL,
  `started_at` datetime DEFAULT NULL,
  `ended_at` datetime DEFAULT NULL,
  `duration` int(11) NOT NULL DEFAULT '0',
  `pricepermin` int(11) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `public_play`
--

INSERT INTO `public_play` (`id`, `channel`, `agent_id`, `started_at`, `ended_at`, `duration`, `pricepermin`, `created_at`, `updated_at`) VALUES
(1, 'W2YId', 1, '2020-12-23 10:22:19', NULL, 0, 8, '2020-12-23 17:22:19', '2020-12-23 17:22:19'),
(2, 'IajQA', 1, '2020-12-23 10:23:16', '2020-12-23 10:24:21', 1, 8, '2020-12-23 17:23:16', '2020-12-23 17:24:21'),
(3, 'qmI3r', 1, '2020-12-23 10:24:26', '2020-12-23 14:06:56', 223, 8, '2020-12-23 17:24:26', '2020-12-23 21:06:56');

-- --------------------------------------------------------

--
-- Table structure for table `public_visit`
--

CREATE TABLE `public_visit` (
  `id` int(10) UNSIGNED NOT NULL,
  `play_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `entered_at` datetime NOT NULL,
  `leaved_at` datetime DEFAULT NULL,
  `pricepermin` int(11) NOT NULL DEFAULT '0',
  `totalprice` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `public_visit`
--

INSERT INTO `public_visit` (`id`, `play_id`, `user_id`, `entered_at`, `leaved_at`, `pricepermin`, `totalprice`, `created_at`, `updated_at`) VALUES
(5, 1, 1, '2020-12-23 14:06:09', '2020-12-23 14:06:14', 8, 8, '2020-12-23 21:06:09', '2020-12-23 21:06:14');

-- --------------------------------------------------------

--
-- Table structure for table `request_agent_room`
--

CREATE TABLE `request_agent_room` (
  `id` int(10) UNSIGNED NOT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `old_type` int(11) DEFAULT NULL,
  `new_type` int(11) DEFAULT NULL,
  `call_type` int(11) DEFAULT NULL,
  `performance_price` int(11) DEFAULT NULL,
  `approved` tinyint(4) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `request_agent_room`
--

INSERT INTO `request_agent_room` (`id`, `agent_id`, `old_type`, `new_type`, `call_type`, `performance_price`, `approved`, `created_at`, `updated_at`) VALUES
(1, 1, 2, 1, 2, 8, 1, '2020-12-23 17:21:08', '2020-12-23 17:22:12'),
(2, 1, 1, 2, 1, 10, 1, '2020-12-23 21:07:07', '2020-12-23 21:07:39'),
(3, 1, 2, 1, 2, 0, 1, '2020-12-24 01:22:18', '2020-12-24 01:23:09'),
(4, 1, 1, 2, 2, 10, 1, '2020-12-24 01:23:22', '2020-12-24 01:23:32');

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id` int(10) UNSIGNED NOT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `star` int(11) DEFAULT NULL,
  `spicy` int(11) DEFAULT NULL,
  `review` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `email_verified_at`, `password`, `remember_token`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin@admin.com', '2020-12-22 17:25:29', '$2y$10$nSJg6pJkhJzuJQEvevGd5uR1beLNXeyAutVTc5wQJQMuonva8ZBqO', NULL, '2020-12-22 17:25:29', '2020-12-22 17:25:29'),
(2, 'eliza', 'eliza@mail.com', '2020-12-22 17:25:29', '$2y$10$GrrQwnRw5cZu4DwhYG1TtOamYrpheCjyfZg8zeoplJFHnq9BlvVOm', NULL, '2020-12-22 17:25:29', '2020-12-22 17:25:29');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity_logs`
--
ALTER TABLE `activity_logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_agents`
--
ALTER TABLE `app_agents`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_users`
--
ALTER TABLE `app_users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `calls`
--
ALTER TABLE `calls`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `gallery`
--
ALTER TABLE `gallery`
  ADD PRIMARY KEY (`photo_id`);

--
-- Indexes for table `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notification_agent`
--
ALTER TABLE `notification_agent`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notification_user`
--
ALTER TABLE `notification_user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `password_resets`
--
ALTER TABLE `password_resets`
  ADD KEY `password_resets_email_index` (`email`(191));

--
-- Indexes for table `payment_histories`
--
ALTER TABLE `payment_histories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `public_play`
--
ALTER TABLE `public_play`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `public_visit`
--
ALTER TABLE `public_visit`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `request_agent_room`
--
ALTER TABLE `request_agent_room`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity_logs`
--
ALTER TABLE `activity_logs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `app_agents`
--
ALTER TABLE `app_agents`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `app_users`
--
ALTER TABLE `app_users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `calls`
--
ALTER TABLE `calls`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `gallery`
--
ALTER TABLE `gallery`
  MODIFY `photo_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notification_agent`
--
ALTER TABLE `notification_agent`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `notification_user`
--
ALTER TABLE `notification_user`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payment_histories`
--
ALTER TABLE `payment_histories`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `public_play`
--
ALTER TABLE `public_play`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `public_visit`
--
ALTER TABLE `public_visit`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `request_agent_room`
--
ALTER TABLE `request_agent_room`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
