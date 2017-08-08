package actors

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import models.{Biller, UserAccount, UserDatabase}
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Map}

class UserDatabaseActorTest extends TestKit(ActorSystem("test-system")) with FunSuiteLike
  with BeforeAndAfterAll with ImplicitSender with MockitoSugar{

  val userDatabase: UserDatabase = mock[UserDatabase]
  val ref: ActorRef = system.actorOf(UserDatabaseActor.props(userDatabase))

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  test("testing receive method for creating account case"){

    when (userDatabase.userAccountMap) thenReturn mutable.Map("divya" -> UserAccount(900,"Divya", "Muzaffarnagar", "divya", 100),
      "neha" -> UserAccount(901,"Neha", "Muzaffarnagar", "neha", 1000))

    when(userDatabase.addAccount("prince",
      UserAccount(1, "Prince Dua", "mzn", "prince", 100))) thenReturn
      mutable.Map("prince" -> UserAccount(1, "Prince Dua", "mzn", "prince", 100),
        "divya" -> UserAccount(900,"Divya", "Muzaffarnagar", "divya", 100),
      "neha" -> UserAccount(901,"Neha", "Muzaffarnagar", "neha", 1000))

    ref ! ("prince", List("Prince Dua", "mzn", "prince", "100"))

    expectMsgPF(){
      case (username: String, resultMsg: String) =>
        assert(username == "prince" &&
          resultMsg == "Account Created")
    }
  }

  test("testing receive method for username already exists case"){

    when (userDatabase.userAccountMap) thenReturn mutable.Map("divya" -> UserAccount(900,"Divya", "Muzaffarnagar", "divya", 100),
      "neha" -> UserAccount(901,"Neha", "Muzaffarnagar", "neha", 1000))

    ref ! ("divya", List("Divya Dua", "mzn", "divya", "100"))

    expectMsgPF(){
      case (username: String, resultMsg: String) =>
        assert(username == "divya" &&
          resultMsg == "Username already exists")
    }
  }

  test("testing link billers case"){

    when(userDatabase.linkBillers(900L, Biller("car", "CarBiller", 900))) thenReturn
      mutable.Map(900L -> ListBuffer(Biller("phone", "PhoneBiller", 900),
      Biller("electricity", "ElectricityBiller", 900),Biller("car", "CarBiller", 900)))

    ref ! (900L,Biller("car", "CarBiller", 900))
    expectMsg("Task of linking is done")
  }

  test("testing deposit salary case"){

    when(userDatabase.depositSalary(900L, "Divya", 1000.0)) thenReturn true

    ref ! (900L, "Divya", 1000.0)
    expectMsg(true)
  }

  test("testing get linked billers category") {

    when(userDatabase.getLinkedBillers(900L)) thenReturn ListBuffer(Biller("phone", "PhoneBiller", 900),
      Biller("electricity", "ElectricityBiller", 900))

    ref ! 900L
    expectMsgPF(){
      case billersCategory: ListBuffer[String] =>
        assert(billersCategory === ListBuffer("phone", "electricity"))
    }
  }

  test("testing pay bill case"){

    when(userDatabase.payBill(900L, 200.0, "phone")) thenReturn "Bill paid"

    ref ! (900L, 200.0, "phone")
    expectMsg("Bill paid")
  }




}
