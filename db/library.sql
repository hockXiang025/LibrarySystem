-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2025-08-24 11:38:39
-- 服务器版本： 10.4.32-MariaDB
-- PHP 版本： 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `library`
--

-- --------------------------------------------------------

--
-- 表的结构 `book`
--

CREATE TABLE `book` (
  `isbn` varchar(100) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `author` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `book`
--

INSERT INTO `book` (`isbn`, `title`, `author`, `status`) VALUES
('123', 'ERD', 'Miss Tang', 'borrowed'),
('456', 'Social', 'Alva', 'borrowed');

-- --------------------------------------------------------

--
-- 表的结构 `borrowedbook`
--

CREATE TABLE `borrowedbook` (
  `user_id` varchar(100) DEFAULT NULL,
  `isbn` varchar(100) DEFAULT NULL,
  `borrow_date` varchar(100) DEFAULT NULL,
  `return_date` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `borrowedbook`
--

INSERT INTO `borrowedbook` (`user_id`, `isbn`, `borrow_date`, `return_date`) VALUES
('U0002', '123', '2025-08-24', '2025-09-13'),
('U0001', '456', '2025-08-24', '2025-09-13');

-- --------------------------------------------------------

--
-- 表的结构 `returnedbook`
--

CREATE TABLE `returnedbook` (
  `user_id` varchar(100) DEFAULT NULL,
  `isbn` varchar(100) DEFAULT NULL,
  `borrow_date` varchar(100) DEFAULT NULL,
  `return_date` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `returnedbook`
--

INSERT INTO `returnedbook` (`user_id`, `isbn`, `borrow_date`, `return_date`) VALUES
('U0001', '123', '2025-08-24', '2025-08-24 17:28:08'),
('U0002', '456', '2025-08-24', '2025-08-24 17:28:17');

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `id` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `users`
--

INSERT INTO `users` (`id`, `name`) VALUES
('U0001', 'JOHN'),
('U0002', 'EMA');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
