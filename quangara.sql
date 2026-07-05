-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 05, 2026 at 07:11 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quangara`
--

-- --------------------------------------------------------

--
-- Table structure for table `dich_vu`
--

CREATE TABLE `dich_vu` (
  `ma` varchar(20) NOT NULL,
  `ten` varchar(255) NOT NULL,
  `don_gia` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `invoices`
--

CREATE TABLE `invoices` (
  `invoice_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `payment_date` datetime DEFAULT NULL,
  `total_part_cost` double NOT NULL DEFAULT 0,
  `total_labor_cost` double NOT NULL DEFAULT 0,
  `vat_rate` double NOT NULL DEFAULT 0.1,
  `total_amount` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `linh_kien`
--

CREATE TABLE `linh_kien` (
  `ma` varchar(20) NOT NULL,
  `ten` varchar(255) NOT NULL,
  `don_gia` double NOT NULL DEFAULT 0,
  `so_luong_ton` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mechanics`
--

CREATE TABLE `mechanics` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `specialization` varchar(255) DEFAULT NULL,
  `salary` double NOT NULL DEFAULT 0,
  `status` varchar(50) NOT NULL DEFAULT 'Đang rảnh'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `owners`
--

CREATE TABLE `owners` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `owners`
--

INSERT INTO `owners` (`id`, `name`, `phone`, `address`) VALUES
(1, 'Manh Quynh', '0987654321', 'HCM');

-- --------------------------------------------------------

--
-- Table structure for table `repair_orders`
--

CREATE TABLE `repair_orders` (
  `order_id` int(11) NOT NULL,
  `license_plate` varchar(20) NOT NULL,
  `entry_date` datetime DEFAULT NULL,
  `exit_date` datetime DEFAULT NULL,
  `mechanic_id` int(11) NOT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'RECEIVING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `repair_order_details`
--

CREATE TABLE `repair_order_details` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `ma_hang_muc` varchar(20) NOT NULL,
  `so_luong` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `vehicles`
--

CREATE TABLE `vehicles` (
  `license_plate` varchar(20) NOT NULL,
  `brand` varchar(100) DEFAULT NULL,
  `model` varchar(100) DEFAULT NULL,
  `production_year` int(11) DEFAULT NULL,
  `owner_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicles`
--

INSERT INTO `vehicles` (`license_plate`, `brand`, `model`, `production_year`, `owner_id`) VALUES
('12345', 'BMW', 'Bugatti Chiron', 2017, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dich_vu`
--
ALTER TABLE `dich_vu`
  ADD PRIMARY KEY (`ma`);

--
-- Indexes for table `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`invoice_id`),
  ADD UNIQUE KEY `order_id` (`order_id`);

--
-- Indexes for table `linh_kien`
--
ALTER TABLE `linh_kien`
  ADD PRIMARY KEY (`ma`);

--
-- Indexes for table `mechanics`
--
ALTER TABLE `mechanics`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `phone` (`phone`);

--
-- Indexes for table `owners`
--
ALTER TABLE `owners`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `phone` (`phone`);

--
-- Indexes for table `repair_orders`
--
ALTER TABLE `repair_orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `fk_order_vehicle` (`license_plate`),
  ADD KEY `fk_order_mechanic` (`mechanic_id`);

--
-- Indexes for table `repair_order_details`
--
ALTER TABLE `repair_order_details`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_order_item` (`order_id`,`ma_hang_muc`);

--
-- Indexes for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`license_plate`),
  ADD KEY `fk_vehicle_owner` (`owner_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `invoices`
--
ALTER TABLE `invoices`
  MODIFY `invoice_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mechanics`
--
ALTER TABLE `mechanics`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `owners`
--
ALTER TABLE `owners`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `repair_orders`
--
ALTER TABLE `repair_orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `repair_order_details`
--
ALTER TABLE `repair_order_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `fk_invoice_order` FOREIGN KEY (`order_id`) REFERENCES `repair_orders` (`order_id`) ON DELETE CASCADE;

--
-- Constraints for table `repair_orders`
--
ALTER TABLE `repair_orders`
  ADD CONSTRAINT `fk_order_mechanic` FOREIGN KEY (`mechanic_id`) REFERENCES `mechanics` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_order_vehicle` FOREIGN KEY (`license_plate`) REFERENCES `vehicles` (`license_plate`) ON DELETE CASCADE;

--
-- Constraints for table `repair_order_details`
--
ALTER TABLE `repair_order_details`
  ADD CONSTRAINT `fk_detail_order` FOREIGN KEY (`order_id`) REFERENCES `repair_orders` (`order_id`) ON DELETE CASCADE;

--
-- Constraints for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD CONSTRAINT `fk_vehicle_owner` FOREIGN KEY (`owner_id`) REFERENCES `owners` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
