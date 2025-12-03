-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: clarkedb
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS ClarkeDB;
USE ClarkeDB;

CREATE USER IF NOT EXISTS 'clarke'@'localhost' IDENTIFIED BY 'cudeca';
GRANT ALL ON ClarkeDB.* TO 'clarke'@'localhost';

--
-- Table structure for table `clasifica`
--

DROP TABLE IF EXISTS `clasifica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clasifica` (
  `ID_ETIQUETA` int NOT NULL,
  `ID_EVENTO` int NOT NULL,
  PRIMARY KEY (`ID_ETIQUETA`,`ID_EVENTO`),
  KEY `CLASIFICA_EVENTO_FK_idx` (`ID_EVENTO`),
  CONSTRAINT `CLASIFICA_ETIQUETAS_FK` FOREIGN KEY (`ID_ETIQUETA`) REFERENCES `etiquetas` (`ID_ETIQUETA`),
  CONSTRAINT `CLASIFICA_EVENTO_FK` FOREIGN KEY (`ID_EVENTO`) REFERENCES `evento` (`ID_EVENTO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clasifica`
--

LOCK TABLES `clasifica` WRITE;
/*!40000 ALTER TABLE `clasifica` DISABLE KEYS */;
INSERT INTO `clasifica` VALUES (1,1),(7,1),(9,2),(14,2),(8,3),(15,3),(12,4),(13,4),(5,5),(6,5),(9,6),(11,7),(15,7),(7,8),(13,8),(2,9),(14,9),(9,10),(10,11),(3,13),(12,13),(12,16),(13,16),(1,17),(4,17),(7,17);
/*!40000 ALTER TABLE `clasifica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrada`
--

DROP TABLE IF EXISTS `entrada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrada` (
  `ID_ENTRADA` int NOT NULL AUTO_INCREMENT,
  `ID_EVENTO` int NOT NULL,
  `Precio` double NOT NULL,
  `Cantidad` int NOT NULL,
  `Informacion` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`ID_ENTRADA`,`ID_EVENTO`),
  UNIQUE KEY `ID_ENTRADA_UNIQUE` (`ID_ENTRADA`),
  KEY `ENTRADA_EVENTO_FK_idx` (`ID_EVENTO`),
  CONSTRAINT `ENTRADA_EVENTO_FK` FOREIGN KEY (`ID_EVENTO`) REFERENCES `evento` (`ID_EVENTO`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrada`
--

LOCK TABLES `entrada` WRITE;
/*!40000 ALTER TABLE `entrada` DISABLE KEYS */;
INSERT INTO `entrada` VALUES (1,1,25,300,'Entrada general'),(2,1,45,80,'Entrada VIP'),(3,1,60,20,'Zona palco'),(4,2,10,500,'Inscripción básica'),(5,2,20,150,'Inscripción premium'),(6,3,2,1000,'Participación simple'),(7,3,5,400,'Pack de 3 participaciones'),(8,4,8,200,'Entrada general'),(9,4,15,100,'Acceso completo'),(10,5,30,250,'Entrada general'),(11,5,55,60,'VIP primera fila'),(12,6,12,400,'Inscripción básica'),(13,6,25,160,'Inscripción avanzada'),(14,7,1.5,1200,'Rifa estándar'),(15,7,4,500,'Pack especial'),(16,8,18,80,'Taller básico'),(17,8,30,40,'Taller avanzado'),(18,9,20,500,'Entrada general'),(19,9,35,120,'Entrada preferente'),(20,10,15,350,'Inscripción general'),(21,10,30,100,'Inscripción elite'),(22,11,3,700,'Boleto navideño'),(23,11,7,250,'Pack navidad'),(24,13,22,200,'Entrada normal'),(25,13,40,80,'VIP'),(26,16,5,300,'Acceso general'),(27,16,12,100,'Acceso completo'),(28,17,28,180,'Entrada general'),(29,17,50,50,'VIP'),(30,17,70,15,'Zona premium');
/*!40000 ALTER TABLE `entrada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etiquetas`
--

DROP TABLE IF EXISTS `etiquetas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etiquetas` (
  `ID_ETIQUETA` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_ETIQUETA`),
  UNIQUE KEY `ID_ETIQUETA_UNIQUE` (`ID_ETIQUETA`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etiquetas`
--

LOCK TABLES `etiquetas` WRITE;
/*!40000 ALTER TABLE `etiquetas` DISABLE KEYS */;
INSERT INTO `etiquetas` VALUES (1,'rock'),(2,'pop'),(3,'indie'),(4,'electrónica'),(5,'clásica'),(6,'formal'),(7,'casual'),(8,'benéfico'),(9,'deportivo'),(10,'navidad'),(11,'gastronómico'),(12,'cultural'),(13,'arte'),(14,'familia'),(15,'tradicional');
/*!40000 ALTER TABLE `etiquetas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evento`
--

DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evento` (
  `ID_EVENTO` int NOT NULL AUTO_INCREMENT,
  `ID_TIPO_EVENTO` int NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Fecha` date NOT NULL,
  `Lugar` varchar(45) DEFAULT NULL,
  `Descripcion` varchar(200) DEFAULT NULL,
  `Recaudacion` double NOT NULL,
  `Objetivo` double NOT NULL,
  `Informacion` varchar(75) DEFAULT NULL,
  `Imagen` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID_EVENTO`),
  UNIQUE KEY `ID_EVENTO_UNIQUE` (`ID_EVENTO`),
  KEY `EVENTO_TIPO_EVENTO_FK_idx` (`ID_TIPO_EVENTO`),
  CONSTRAINT `EVENTO_TIPO_EVENTO_FK` FOREIGN KEY (`ID_TIPO_EVENTO`) REFERENCES `tipo_evento` (`ID_TIPO_EVENTO`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evento`
--

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
INSERT INTO `evento` VALUES (1,3,'Concierto en la Malagueta','2025-02-10','La Malagueta','Presentación musical al aire libre.',5000,10000,NULL,NULL),(2,1,'Carrera Paseo Marítimo','2025-03-02','Paseo Marítimo Pablo Ruiz Picasso','Carrera popular de 5 km junto al mar.',2000,2500,NULL,NULL),(3,2,'Rifa Solidaria en Teatinos','2025-04-01','Teatinos','Rifa benéfica organizada por asociaciones locales.',1500,3000,NULL,NULL),(4,4,'Festival Cultural Soho','2025-05-12','Barrio del Soho','Actividades culturales y talleres artísticos.',6000,6500,NULL,NULL),(5,3,'Concierto en el Cervantes','2025-06-20','Teatro Cervantes','Actuación de banda local.',5000,9000,NULL,NULL),(6,1,'Carrera del Parque del Oeste','2025-07-05','Parque del Oeste','Competición deportiva para todas las edades.',5200,4000,NULL,NULL),(7,2,'Rifa del Mercado de Atarazanas','2025-08-15','Mercado Central de Atarazanas','Rifa de productos locales.',3500,3000,NULL,NULL),(8,4,'Taller Creativo en el Muelle Uno','2025-09-09','Muelle Uno','Taller artístico abierto al público.',400,9000,NULL,NULL),(9,3,'Concierto en Plaza de la Merced','2025-10-03','Plaza de la Merced','Evento musical gratuito.',510,200,NULL,NULL),(10,1,'Carrera Montes de Málaga','2025-11-14','Montes de Málaga','Carrera de montaña de 10 km.',5000,900,NULL,NULL),(11,2,'Rifa de Navidad en El Palo','2025-12-18','El Palo','Rifa tradicional del barrio.',800,2000,NULL,NULL),(12,4,'Encuentro Cultural Pedregalejo','2025-02-27','Pedregalejo','Reunión vecinal con actividades.',600,5000,NULL,NULL),(13,3,'Concierto en el Auditorio Eduardo Ocón','2025-03-21','Parque de Málaga','Presentación de artistas emergentes.',250,6500,NULL,NULL),(14,1,'Carrera Río Guadalhorce','2025-04-06','Río Guadalhorce','',800,9000,NULL,NULL),(15,2,'Rifa en el Barrio de Huelin','2025-05-30','Huelin','Rifa comunitaria con premios locales.',100,2000,NULL,NULL),(16,4,'Feria Artesanal del Centro Histórico','2025-06-11','Centro Histórico','Exposición y venta de artesanías.',300,1000,NULL,NULL),(17,3,'Concierto en Gibralfaro','2025-07-25','Castillo de Gibralfaro','Concierto nocturno con vistas panorámicas.',250,600,NULL,NULL),(18,1,'Carrera Playa de la Misericordia','2025-08-02','Playa de la Misericordia','Carrera playera de 3 km.',150,900,NULL,NULL),(19,2,'Rifa del Puerto de Málaga','2025-09-19','Puerto de Málaga','Rifa de recaudación solidaria.',900,950,NULL,NULL),(20,4,'Encuentro de Arte en la Térmica','2025-10-27','La Térmica','Actividades culturales y exposiciones.',400,400,NULL,NULL);
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patrocinador`
--

DROP TABLE IF EXISTS `patrocinador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patrocinador` (
  `ID_PATROCINADOR` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Imagen` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID_PATROCINADOR`),
  UNIQUE KEY `ID_PATROCINADOR_UNIQUE` (`ID_PATROCINADOR`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patrocinador`
--

LOCK TABLES `patrocinador` WRITE;
/*!40000 ALTER TABLE `patrocinador` DISABLE KEYS */;
INSERT INTO `patrocinador` VALUES (1,'Cervezas Victoria',NULL),(2,'Unicaja Banco',NULL),(3,'Ayuntamiento de Málaga',NULL),(4,'Coca-Cola Málaga',NULL),(5,'El Corte Inglés Málaga',NULL),(6,'Muelle Uno',NULL),(7,'Diario SUR',NULL),(8,'San Miguel',NULL),(9,'Puerto de Málaga',NULL),(10,'Universidad de Málaga',NULL),(11,'Centro Comercial Larios',NULL),(12,'Aeropuerto Málaga-Costa del Sol',NULL);
/*!40000 ALTER TABLE `patrocinador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patrocinio`
--

DROP TABLE IF EXISTS `patrocinio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patrocinio` (
  `ID_PATROCINADOR` int NOT NULL,
  `ID_EVENTO` int NOT NULL,
  PRIMARY KEY (`ID_PATROCINADOR`,`ID_EVENTO`),
  KEY `PATROCINIO_EVENTO_FK_idx` (`ID_EVENTO`),
  CONSTRAINT `PATROCINIO_EVENTO_FK` FOREIGN KEY (`ID_EVENTO`) REFERENCES `evento` (`ID_EVENTO`),
  CONSTRAINT `PATROCINIO_PATROCINADOR_FK` FOREIGN KEY (`ID_PATROCINADOR`) REFERENCES `patrocinador` (`ID_PATROCINADOR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patrocinio`
--

LOCK TABLES `patrocinio` WRITE;
/*!40000 ALTER TABLE `patrocinio` DISABLE KEYS */;
INSERT INTO `patrocinio` VALUES (1,1),(4,1),(2,2),(8,2),(3,3),(10,4),(1,5),(7,5),(2,6),(11,7),(6,8),(4,9),(2,10),(9,10),(1,13),(12,13),(1,17),(4,17);
/*!40000 ALTER TABLE `patrocinio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registrado`
--

DROP TABLE IF EXISTS `registrado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registrado` (
  `DNI_USUARIO` varchar(9) NOT NULL,
  `Telefono` varchar(30) NOT NULL,
  `Direccion_postal` varchar(45) NOT NULL,
  PRIMARY KEY (`DNI_USUARIO`),
  UNIQUE KEY `DNI_USUARIO_UNIQUE` (`DNI_USUARIO`),
  CONSTRAINT `REGISTRADO_USUARIO_FK` FOREIGN KEY (`DNI_USUARIO`) REFERENCES `usuario` (`DNI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registrado`
--

LOCK TABLES `registrado` WRITE;
/*!40000 ALTER TABLE `registrado` DISABLE KEYS */;
INSERT INTO `registrado` VALUES ('11112222U','609000111','Camino de Suárez 38, Málaga'),('11223344K','600111222','Calle Carretería 18, Málaga'),('12345678A','611223344','Calle Larios 12, Málaga'),('22223333V','610111222','Calle La Unión 25, Málaga'),('22334455L','601222333','Calle Ollerías 70, Málaga'),('23456789B','622334455','Avenida Andalucía 45, Málaga'),('33445566M','602333444','Calle Mármoles 15, Málaga'),('34567890C','633445566','Calle Victoria 22, Málaga'),('44556677N','603444555','Avenida Juan XXIII 89, Málaga'),('45678901D','644556677','Paseo del Parque 8, Málaga'),('55667788P','604555666','Calle Victoria Kent 27, Málaga'),('56789012E','655667788','Calle Granada 30, Málaga'),('66778899Q','605666777','Calle Ingeniero de la Torre Acosta 5'),('67890123F','666778899','Avenida Velázquez 102, Málaga'),('77889900R','606777888','Avenida de la Aurora 91, Málaga'),('78901234G','677889900','Calle Pacífico 14, Málaga');
/*!40000 ALTER TABLE `registrado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket` (
  `ID_TICKET` int NOT NULL AUTO_INCREMENT,
  `DNI_USUARIO` varchar(9) NOT NULL,
  `ID_ENTRADA` int NOT NULL,
  `Dni_asistente` varchar(9) DEFAULT NULL,
  `Pago_extra` varchar(45) DEFAULT NULL,
  `Informacion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID_TICKET`,`DNI_USUARIO`,`ID_ENTRADA`),
  UNIQUE KEY `ID_TICKET_UNIQUE` (`ID_TICKET`),
  KEY `TICKET_USUARIO_FK_idx` (`DNI_USUARIO`),
  KEY `TICKET_ENTRADA_FK_idx` (`ID_ENTRADA`),
  CONSTRAINT `TICKET_ENTRADA_FK` FOREIGN KEY (`ID_ENTRADA`) REFERENCES `entrada` (`ID_ENTRADA`),
  CONSTRAINT `TICKET_USUARIO_FK` FOREIGN KEY (`DNI_USUARIO`) REFERENCES `usuario` (`DNI`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
INSERT INTO `ticket` VALUES (1,'99001122T',5,'99001122T',NULL,NULL),(2,'90123456J',12,'77324113G','10',NULL),(3,'89012345H',7,'45778899L',NULL,NULL),(4,'88990011S',18,'88990011S','20',NULL),(5,'78901234G',1,NULL,NULL,NULL),(6,'77889900R',9,'11220033P','5',NULL),(7,'77778888A',14,'77778888A',NULL,NULL),(8,'67890123F',22,NULL,NULL,NULL),(9,'66778899Q',30,'66778899Q','2',NULL),(10,'66667777Z',16,NULL,NULL,NULL),(11,'56789012E',4,'56789012E',NULL,NULL),(12,'55667788P',21,'99887755H',NULL,NULL),(13,'55556666Y',3,NULL,'4',NULL),(14,'45678901D',10,'45678901D','1',NULL),(15,'44556677N',27,NULL,NULL,NULL),(16,'44445555X',6,'88442211M',NULL,NULL),(17,'34567890C',25,NULL,'3',NULL),(18,'33445566M',8,'33445566M',NULL,NULL),(19,'33334444W',11,'11229988Z','9',NULL),(20,'23456789B',15,NULL,NULL,NULL),(21,'22334455L',19,'88997766Y','4',NULL),(22,'22223333V',2,'22223333V',NULL,NULL),(23,'12345678A',23,NULL,NULL,NULL),(24,'11223344K',29,'44553322L',NULL,NULL),(25,'11112222U',13,'11112222U','5',NULL),(26,'99001122T',24,NULL,'5',NULL),(27,'90123456J',30,'77770000T',NULL,NULL),(28,'89012345H',17,NULL,NULL,NULL),(29,'88990011S',28,'55443322X',NULL,NULL),(30,'78901234G',20,NULL,NULL,NULL),(31,'77889900R',26,'77889900R',NULL,NULL),(32,'77778888A',3,'99880011S',NULL,NULL),(33,'67890123F',14,NULL,'2',NULL),(34,'66778899Q',7,NULL,NULL,NULL),(35,'66667777Z',10,'66667777Z','6',NULL),(36,'56789012E',1,'56443322T',NULL,NULL),(37,'55667788P',22,NULL,NULL,NULL),(38,'55556666Y',9,'55556666Y','8',NULL),(39,'45678901D',5,NULL,'1',NULL),(40,'44556677N',27,'88994433G',NULL,NULL);
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_evento`
--

DROP TABLE IF EXISTS `tipo_evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_evento` (
  `ID_TIPO_EVENTO` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`ID_TIPO_EVENTO`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_evento`
--

LOCK TABLES `tipo_evento` WRITE;
/*!40000 ALTER TABLE `tipo_evento` DISABLE KEYS */;
INSERT INTO `tipo_evento` VALUES (1,'CARRERA'),(2,'RIFA'),(3,'CONCIERTO'),(4,'OTRO');
/*!40000 ALTER TABLE `tipo_evento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `DNI` varchar(9) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Apellidos` varchar(45) NOT NULL,
  `Email` varchar(45) NOT NULL,
  `Spam` tinyint(1) NOT NULL,
  PRIMARY KEY (`DNI`),
  UNIQUE KEY `DNI_UNIQUE` (`DNI`),
  UNIQUE KEY `Email_UNIQUE` (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('11112222U','Tomás','Acosta Jurado','tomas.acosta@example.com',1),('11223344K','Isabel','Gil Vargas','isabel.gil@example.com',1),('12345678A','Carlos','Gómez Ruiz','carlos.gomez@example.com',0),('22223333V','Ainhoa','Benítez Ramos','ainhoa.benitez@example.com',1),('22334455L','David','Cano Rivas','david.cano@example.com',0),('23456789B','María','López Sánchez','maria.lopez@example.com',0),('33334444W','Rafael','Domínguez Peña','rafael.dominguez@example.com',0),('33445566M','Patricia','Santos Lara','patricia.santos@example.com',0),('34567890C','Javier','Martín Torres','javier.martin@example.com',1),('44445555X','Sonia','Carrasco Real','sonia.carrasco@example.com',1),('44556677N','Hugo','Reyes Marín','hugo.reyes@example.com',0),('45678901D','Laura','Jiménez Pérez','laura.jimenez@example.com',1),('55556666Y','Gonzalo','Márquez Doria','gonzalo.marquez@example.com',0),('55667788P','Elena','Ramos Montes','elena.ramos@example.com',0),('56789012E','Sergio','Navarro Díaz','sergio.navarro@example.com',0),('66667777Z','Teresa','Palacios Muñoz','teresa.palacios@example.com',0),('66778899Q','Álvaro','Crespo Iglesias','alvaro.crespo@example.com',0),('67890123F','Lucía','Romero Vega','lucia.romero@example.com',1),('77778888A','Jorge','Fuentes Alba','jorge.fuentes@example.com',0),('77889900R','Nuria','Soto Lozano','nuria.soto@example.com',1),('78901234G','Alejandro','Serrano León','alejandro.serrano@example.com',1),('88990011S','Iván','Estévez Rubio','ivan.estevez@example.com',0),('89012345H','Paula','Molina Castro','paula.molina@example.com',1),('90123456J','Miguel','Hernández Prieto','miguel.hernandez@example.com',1),('99001122T','Cristina','Paredes Navas','cristina.paredes@example.com',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-03 20:51:23
