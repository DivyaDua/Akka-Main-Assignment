package models

import scala.collection.mutable.Map

trait UserDatabase {

  val userAccountMap = Map("divya" -> UserAccount(900,"Divya", "Muzaffarnagar", "divya", 100),
   "neha" -> UserAccount(901,"Neha", "Muzaffarnagar", "neha", 1000))

}
