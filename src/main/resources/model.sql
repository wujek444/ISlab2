CREATE IF NOT EXISTS DATABASE `is` /*!40100 DEFAULT CHARACTER SET latin1 */;

CREATE TABLE IF NOT EXISTS `laptop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `manufacturer` varchar(100) DEFAULT NULL,
  `matrixSize` varchar(100) DEFAULT NULL,
  `resolution` varchar(100) DEFAULT NULL,
  `matrixCoating` varchar(100) DEFAULT NULL,
  `touchPad` varchar(3) DEFAULT NULL,
  `cpuFamily` varchar(100) DEFAULT NULL,
  `coresCount` varchar(50) DEFAULT NULL,
  `clockSpeed` varchar(45) DEFAULT NULL,
  `ram` varchar(45) DEFAULT NULL,
  `driveCapacity` varchar(45) DEFAULT NULL,
  `driveType` varchar(45) DEFAULT NULL,
  `gpu` varchar(45) DEFAULT NULL,
  `gpuMemory` varchar(45) DEFAULT NULL,
  `os` varchar(45) DEFAULT NULL,
  `opticalDrive` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
