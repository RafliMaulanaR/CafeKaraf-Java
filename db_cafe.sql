-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 24 Mei 2018 pada 09.04
-- Versi Server: 10.1.19-MariaDB
-- PHP Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_cafe`
--

-- --------------------------------------------------------

--
-- Stand-in structure for view `struk`
--
CREATE TABLE `struk` (
`id_transaksi` varchar(20)
,`nama_menu` varchar(100)
,`harga_menu` int(10)
,`jumlah_beli` int(10)
,`total_beli` int(10)
,`bayar` int(10)
,`kembalian` int(10)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `strukk`
--
CREATE TABLE `strukk` (
`id_kasir` varchar(50)
,`id_transaksi` varchar(20)
,`nama_menu` varchar(20)
,`harga_menu` int(6)
,`jumlah_beli` int(10)
,`total_beli` int(10)
,`bayar` int(10)
,`kembalian` int(10)
);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_fix`
--

CREATE TABLE `tb_fix` (
  `id_transaksi` varchar(20) NOT NULL,
  `total_beli` int(10) NOT NULL,
  `bayar` int(10) NOT NULL,
  `kembalian` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_fix`
--

INSERT INTO `tb_fix` (`id_transaksi`, `total_beli`, `bayar`, `kembalian`) VALUES
('PR0001', 136000, 137000, 1000),
('PR0002', 288000, 300000, 12000),
('PR0003', 134000, 135000, 1000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_kasir`
--

CREATE TABLE `tb_kasir` (
  `id_kasir` varchar(50) NOT NULL,
  `nama_kasir` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(25) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `keterangan` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_kasir`
--

INSERT INTO `tb_kasir` (`id_kasir`, `nama_kasir`, `username`, `password`, `alamat`, `keterangan`) VALUES
('KS001', 'Rafli Maulana R', 'RafliMr', 'rafli', 'SeSEupan', 'Masuk'),
('KS002', 'RMR', 'rmr', 'rafliizki', 'CiawiBendungan', 'keluar'),
('KS003', 'Rafli', 'rizki', 'rizkirafli', 'Bogor', 'keluar');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_keranjang`
--

CREATE TABLE `tb_keranjang` (
  `id_transaksi` varchar(50) NOT NULL,
  `id_menu` varchar(10) NOT NULL,
  `nama_menu` varchar(20) NOT NULL,
  `harga_menu` int(6) NOT NULL,
  `jumlah_beli` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Trigger `tb_keranjang`
--
DELIMITER $$
CREATE TRIGGER `batal` AFTER DELETE ON `tb_keranjang` FOR EACH ROW UPDATE tb_menu
SET stok = stok + OLD.jumlah_beli
WHERE id_menu = OLD.id_menu
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `beli` AFTER INSERT ON `tb_keranjang` FOR EACH ROW BEGIN
UPDATE tb_menu SET stok = stok - new.jumlah_beli
WHERE id_menu = new.id_menu;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `beli2` AFTER UPDATE ON `tb_keranjang` FOR EACH ROW BEGIN
UPDATE tb_menu SET stok = stok - new.jumlah_beli + old.jumlah_beli
WHERE id_menu = new.id_menu;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_menu`
--

CREATE TABLE `tb_menu` (
  `id_menu` varchar(10) NOT NULL,
  `menu` enum('Makanan','Minuman') NOT NULL,
  `nama_menu` varchar(20) NOT NULL,
  `harga_menu` int(6) NOT NULL,
  `stok` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_menu`
--

INSERT INTO `tb_menu` (`id_menu`, `menu`, `nama_menu`, `harga_menu`, `stok`) VALUES
('MN001', 'Minuman', 'Jus Mango', 15000, 11),
('MN002', 'Minuman', 'pop es', 2000, 24),
('MN003', 'Makanan', 'Nasi Bakar', 10000, 45),
('MN004', 'Makanan', 'Ayam Goreng', 20000, 49),
('MN005', 'Makanan', 'Bakso', 15000, 55),
('MN006', 'Makanan', 'Mie Ayam', 16000, 54),
('MN007', 'Makanan', 'Ayam Bakar Spesial', 25000, 40),
('MN008', 'Makanan', 'Ayam Chrispy', 24000, 40),
('MN009', 'Makanan', 'Ikan Kakap Bakar', 26000, 40),
('MN010', 'Minuman', 'Es Jeruk', 10000, 60),
('MN011', 'Minuman', 'Es Kelapa', 12000, 55),
('MN012', 'Minuman', 'Es Buah Compleate', 22000, 55),
('MN013', 'Minuman', 'Capucino Cingcau', 13000, 60),
('MN014', 'Minuman', 'Buble Gum Juice', 14000, 50),
('MN015', 'Minuman', 'Es Teh Manis', 5000, 60),
('MN016', 'Minuman', 'Air Putih Spesial', 2000, 100),
('MN017', 'Makanan', 'Nasi GorengCafekaraf', 24000, 98);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `id_kasir` varchar(20) DEFAULT NULL,
  `id_transaksi` varchar(20) DEFAULT NULL,
  `id_menu` varchar(10) NOT NULL,
  `nama_menu` varchar(100) NOT NULL,
  `harga_menu` int(10) NOT NULL,
  `jumlah_beli` int(10) NOT NULL,
  `total` int(10) NOT NULL,
  `tgl_beli` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`id_kasir`, `id_transaksi`, `id_menu`, `nama_menu`, `harga_menu`, `jumlah_beli`, `total`, `tgl_beli`) VALUES
('KS001', 'PR0001', 'MN003', 'Nasi Bakar', 10000, 4, 40000, '2018-05-23 13:21:35'),
('KS001', 'PR0001', 'MN008', 'Ayam Chrispy', 24000, 4, 96000, '2018-05-23 13:21:42'),
('KS001', 'PR0002', 'MN006', 'Mie Ayam', 16000, 1, 16000, '2018-05-23 13:22:38'),
('KS001', 'PR0002', 'MN007', 'Ayam Bakar Spesial', 25000, 10, 250000, '2018-05-23 13:22:45'),
('KS001', 'PR0002', 'MN012', 'Es Buah Compleate', 22000, 1, 22000, '2018-05-23 13:23:14'),
('KS001', 'PR0003', 'MN004', 'Ayam Goreng', 20000, 1, 20000, '2018-05-23 13:25:12'),
('KS001', 'PR0003', 'MN012', 'Es Buah Compleate', 22000, 3, 66000, '2018-05-23 13:25:22'),
('KS001', 'PR0003', 'MN017', 'Nasi GorengCafekaraf', 24000, 2, 48000, '2018-05-23 13:25:32');

-- --------------------------------------------------------

--
-- Struktur untuk view `struk`
--
DROP TABLE IF EXISTS `struk`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `struk`  AS  select `tb_transaksi`.`id_transaksi` AS `id_transaksi`,`tb_transaksi`.`nama_menu` AS `nama_menu`,`tb_transaksi`.`harga_menu` AS `harga_menu`,`tb_transaksi`.`jumlah_beli` AS `jumlah_beli`,`tb_fix`.`total_beli` AS `total_beli`,`tb_fix`.`bayar` AS `bayar`,`tb_fix`.`kembalian` AS `kembalian` from (`tb_transaksi` join `tb_fix` on((`tb_transaksi`.`id_transaksi` = `tb_fix`.`id_transaksi`))) ;

-- --------------------------------------------------------

--
-- Struktur untuk view `strukk`
--
DROP TABLE IF EXISTS `strukk`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `strukk`  AS  select `tb_kasir`.`id_kasir` AS `id_kasir`,`tb_transaksi`.`id_transaksi` AS `id_transaksi`,`tb_menu`.`nama_menu` AS `nama_menu`,`tb_menu`.`harga_menu` AS `harga_menu`,`tb_transaksi`.`jumlah_beli` AS `jumlah_beli`,`tb_fix`.`total_beli` AS `total_beli`,`tb_fix`.`bayar` AS `bayar`,`tb_fix`.`kembalian` AS `kembalian` from (((`tb_kasir` join `tb_transaksi` on((`tb_transaksi`.`id_kasir` = `tb_kasir`.`id_kasir`))) join `tb_menu` on((`tb_menu`.`id_menu` = `tb_transaksi`.`id_menu`))) join `tb_fix` on((`tb_fix`.`id_transaksi` = `tb_transaksi`.`id_transaksi`))) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_menu`
--
ALTER TABLE `tb_menu`
  ADD PRIMARY KEY (`id_menu`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
