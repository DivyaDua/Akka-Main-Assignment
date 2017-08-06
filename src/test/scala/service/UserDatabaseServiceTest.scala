package service

import models.UserAccount
import org.scalatest.FunSuite

import scala.collection.mutable.Map

class UserDatabaseServiceTest extends FunSuite {

  val userAccountMapTest = Map("divya" -> UserAccount(900,"Divya", "Muzaffarnagar", "divya", 100),
    "neha" -> UserAccount(901,"Neha", "Muzaffarnagar", "neha", 1000),"prince" -> UserAccount(902,"Prince", "Muzaffarnagar", "prince", 1000))

  test("testing Add account method"){
    assert(UserDatabaseService.addAccount("prince", UserAccount(902,"Prince", "Muzaffarnagar", "prince", 1000)) === userAccountMapTest)
  }

  test("testing check username uniqueness method - negative case"){
    assert(UserDatabaseService.checkForUserNameUniqueness("divya") === false)
  }

  test("testing check username uniqueness method - positive case"){
    assert(UserDatabaseService.checkForUserNameUniqueness("divyadua") === true)
  }

}
