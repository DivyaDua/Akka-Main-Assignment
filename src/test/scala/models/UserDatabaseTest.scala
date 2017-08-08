package models

import org.scalatest.FunSuite

import scala.collection.mutable.{ListBuffer, Map}

class UserDatabaseTest extends FunSuite {

  val userDatabaseObject = new UserDatabase

  test("testing add account method"){
    assert(userDatabaseObject.addAccount("prince", UserAccount(1000, "Prince Dua",
      "mzn", "prince", 100.0)) === Map("prince" -> UserAccount(1000, "Prince Dua", "mzn","prince",100.0),
      "divya" -> UserAccount(900,"Divya","Muzaffarnagar","divya",100.0),
      "neha" -> UserAccount(901,"Neha","Muzaffarnagar","neha",1000.0))
    )
  }

  test("testing deposit salary method"){
     assert(userDatabaseObject.depositSalary(900, "Divya" , 2000) === true)
  }

  test("testing deposit salary method for negative case"){
    assert(userDatabaseObject.depositSalary(902, "Divya" , 2000) === false)
  }

  test("testing get linked billers method"){
    assert(userDatabaseObject.getLinkedBillers(900) ===  ListBuffer(Biller("phone", "PhoneBiller", 900),
      Biller("electricity", "ElectricityBiller", 900)))
  }

  test("testing get linked billers method for negative case"){
    assert(userDatabaseObject.getLinkedBillers(901) ===  ListBuffer())
  }

  test("testing link billers method"){
    assert(userDatabaseObject.linkBillers(900, Biller("car", "CarBiller", 900)) === Map(900 -> ListBuffer(Biller("phone", "PhoneBiller", 900),
      Biller("electricity", "ElectricityBiller", 900),Biller("car", "CarBiller", 900)) ))
  }

  test("testing pay bill method"){
    assert(userDatabaseObject.payBill(900,200,"car") == "Bill paid")
  }

  test("testing pay bill method for negative case"){
    assert(userDatabaseObject.payBill(903,200,"car") == "No such account exists")
  }

}
